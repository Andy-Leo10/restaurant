package com.example.restaurant

import android.os.Bundle
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurant.ui.theme.RestaurantTheme

import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.ApiListener
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.StatusListener
import com.ainirobot.coreservice.client.listener.CommandListener
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.connectToServer()

        setContent {
            RestaurantTheme {
                AppNavigation()
            }
        }
    }

    private fun connectToServer() {
        // Attempt to connect to the server and handle the connection status
        try {
            RobotApi.getInstance().connectServer(this, object : ApiListener {
                override fun handleApiDisabled() {
                    // Handle API disabled scenario
                    Log.e("MainActivity", "API is disabled")
                }
    
                override fun handleApiConnected() {
                    // Server is connected, set the callback for receiving requests
                    Log.i("MainActivity", "API connected successfully")
                    registerStatusListeners()

                    // // Start the first rotation
                    // rotateRobotAsync("right", reqId = 1, speed = 10.0F, angle = 30F) 
                    // rotateRobotAsync("left", reqId = 1, speed = 10.0F, angle = 30F) { success ->
                    //     Log.i("MainActivity", "Second rotation completed: $success")
                    // }

                    // Start a coroutine to perform multiple rotations in sequence
                    CoroutineScope(Dispatchers.Main).launch {
                        val directions = listOf("right", "left", "right", "left", "right", "left", "right", "left", "right", "left")
                        for ((index, direction) in directions.withIndex()) {
                            val success = rotateRobotSync(direction, reqId = index + 1, speed = 10.0F, angle = 15F)
                            if (!success) {
                                Log.e("MainActivity", "Rotation $direction failed at step ${index + 1}")
                                break
                            }
                        }
                    }
                }
    
                override fun handleApiDisconnected() {
                    // Handle server disconnection
                    Log.e("MainActivity", "API disconnected")
                }
            })
        } catch (e: Exception) {
            // Log the exception to help debug the issue
            Log.e("MainActivity", "Failed to connect to server: ${e.message}", e)
        }
    }    

    private fun registerStatusListeners() {
        // Register listeners for various status updates from the robot
        try {
            val statusListener = object : StatusListener() {
                override fun onStatusUpdate(type: String, data: String) {
                    try {
                        when (type) {
                            Definition.STATUS_POSE -> Log.i("StatusListener", "Robot Pose: $data")
                            Definition.STATUS_POSE_ESTIMATE -> Log.i("StatusListener", "Positioning Status: $data")
                            Definition.STATUS_BATTERY -> Log.i("StatusListener", "Battery Info: $data")
                            else -> Log.w("StatusListener", "Unknown status type: $type")
                        }
                    } catch (e: RemoteException) {
                        Log.e("StatusListener", "Error handling status update: ${e.message}", e)
                    }
                }
            }

            // Register listeners for the desired status types
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_POSE, statusListener)
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_POSE_ESTIMATE, statusListener)
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_BATTERY, statusListener)

            // Unregister the listener when no longer needed (e.g., in onDestroy)
            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    RobotApi.getInstance().unregisterStatusListener(statusListener)
                    super.onDestroy(owner)
                }
            })
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to register status listeners: ${e.message}", e)
        }
    }

    private fun rotateRobotAsync(direction: String, reqId: Int, speed: Float, angle: Float? = null, onComplete: (Boolean) -> Unit = {}) {
        val rotateListener = object : CommandListener() {
            override fun onResult(result: Int, message: String) {
                if ("succeed".equals(message, ignoreCase = true)) {
                    Log.i("MainActivity", "Rotation $direction succeeded")
                    onComplete(true)
                } else {
                    Log.e("MainActivity", "Rotation $direction failed: $message")
                    onComplete(false)
                }
            }
        }

        try {
            when (direction) {
                "right" -> {
                    if (angle != null) {
                        RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener)
                    } else {
                        RobotApi.getInstance().turnLeft(reqId, speed, rotateListener)
                    }
                }
                "left" -> {
                    if (angle != null) {
                        RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener)
                    } else {
                        RobotApi.getInstance().turnRight(reqId, speed, rotateListener)
                    }
                }
                else -> {
                    Log.e("MainActivity", "Invalid direction: $direction")
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to execute rotation: ${e.message}", e)
            onComplete(false)
        }
    }

    private suspend fun rotateRobotSync(direction: String, reqId: Int, speed: Float, angle: Float? = null): Boolean {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val rotateListener = object : CommandListener() {
                    override fun onResult(result: Int, message: String) {
                        if ("succeed".equals(message, ignoreCase = true)) {
                            Log.i("MainActivity", "Rotation $direction succeeded")
                            continuation.resume(true) {}
                        } else {
                            Log.e("MainActivity", "Rotation $direction failed: $message")
                            continuation.resume(false) {}
                        }
                    }
                }
    
                try {
                    when (direction) {
                        "left" -> {
                            if (angle != null) {
                                RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener)
                            } else {
                                RobotApi.getInstance().turnLeft(reqId, speed, rotateListener)
                            }
                        }
                        "right" -> {
                            if (angle != null) {
                                RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener)
                            } else {
                                RobotApi.getInstance().turnRight(reqId, speed, rotateListener)
                            }
                        }
                        else -> {
                            Log.e("MainActivity", "Invalid direction: $direction")
                            continuation.resume(false) {}
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Failed to execute rotation: ${e.message}", e)
                    continuation.resume(false) {}
                }
            }
        }
    }

    // private fun setOrderCallback() {
    //     // Set the callback for receiving order requests from the server
    //     try {
    //         RobotApi.getInstance().setRequestCallback(object : IRobotSettingApi.RequestCallback {
    //             override fun onRequestReceived(request: String) {
    //                 // Handle incoming requests from the server
    //                 Log.i("MainActivity", "Request received: $request")
    //                 // Add logic here to process the order
    //             }
    //         })
    //         Log.i("MainActivity", "Order callback set successfully")
    //     } catch (e: Exception) {
    //         Log.e("MainActivity", "Failed to set order callback: ${e.message}", e)
    //     }
    // }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("MainScreen") }

    when (currentScreen) {
        "MainScreen" -> MainScreen(
            onNavigateToSettings = { currentScreen = "SettingsScreen" },
            onNavigateToAskOrder = { currentScreen = "AskOrderScreen" },
            onNavigateToDeliverOrder = { currentScreen = "DeliverOrderScreen" }
        )
        "SettingsScreen" -> SettingsScreen(onBackClick = { currentScreen = "MainScreen" })
        "AskOrderScreen" -> AskOrderScreen(onBackClick = { currentScreen = "MainScreen" })
        "DeliverOrderScreen" -> DeliverOrderScreen(onBackClick = { currentScreen = "MainScreen" })
    }
}

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAskOrder: () -> Unit = {},
    onNavigateToDeliverOrder: () -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onNavigateToSettings) {
                Text("Settings")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToAskOrder) {
                Text("Ask Order")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToDeliverOrder) {
                Text("Deliver Order")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RestaurantTheme {
        MainScreen()
    }
}
