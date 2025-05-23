package com.example.restaurant.robot_utils

import android.util.Log
import android.os.RemoteException
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import kotlinx.coroutines.*

import com.ainirobot.coreservice.client.ApiListener
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.StatusListener
import com.ainirobot.coreservice.client.Definition

// robot_utils
// import com.example.restaurant.robot_utils.rotateRobotAsync
// import com.example.restaurant.robot_utils.rotateRobotSync
import com.ainirobot.coreservice.client.listener.CommandListener

object RobotApiManager {
    public fun connectToServer(context: Context, lifecycleOwner: LifecycleOwner) {
        // Attempt to connect to the server and handle the connection status
        try {
            RobotApi.getInstance().connectServer(context, object : ApiListener {
                override fun handleApiDisabled() {
                    // Handle API disabled scenario
                    Log.e("RobotApiManager", "API is disabled")
                }
    
                override fun handleApiConnected() {
                    // Server is connected, set the callback for receiving requests
                    Log.i("RobotApiManager", "API connected successfully")
                    registerStatusListeners(lifecycleOwner)
                    testRotationCommands()
                    // // Start the first rotation
                    // rotateRobotAsync("right", reqId = 1, speed = 10.0F, angle = 30F) 
                    // rotateRobotAsync("left", reqId = 1, speed = 10.0F, angle = 30F) { success ->
                    //     Log.i("RobotApiManager", "Second rotation completed: $success")
                    // }

                    // // Start a coroutine to perform multiple rotations in sequence
                    // CoroutineScope(Dispatchers.Main).launch {
                    //     val directions = listOf("right", "left", "right", "left", "right", "left", "right", "left", "right", "left")
                    //     for ((index, direction) in directions.withIndex()) {
                    //         val success = rotateRobotSync(direction, reqId = index + 1, speed = 10.0F, angle = 15F)
                    //         if (!success) {
                    //             Log.e("RobotApiManager", "Rotation $direction failed at step ${index + 1}")
                    //             break
                    //         }
                    //     }
                    // }
                }
    
                override fun handleApiDisconnected() {
                    // Handle server disconnection
                    Log.e("RobotApiManager", "API disconnected")
                }
            })
        } catch (e: Exception) {
            // Log the exception to help debug the issue
            Log.e("RobotApiManager", "Failed to connect to server: ${e.message}", e)
        }
    }    

    fun testRotationCommands() {
        val reqId = 999
        val speed = 10.0F
        val angle = 30.0F

        val rotateListener = object : CommandListener() {
            override fun onResult(result: Int, message: String) {
                Log.i("RobotApiTest", "Test rotation result: $result, message: $message")
                if (result != 0) {
                    Log.e("RobotApiTest", "Rotation command failed with result=$result, message=$message")
                }   
            }
        }

        try {
            Log.i("RobotApiTest", "Calling turnLeft with speed=$speed, angle=$angle")
            RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener)

            Log.i("RobotApiTest", "Calling turnRight with speed=$speed, angle=$angle")
            RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener)
        } catch (e: Exception) {
            Log.e("RobotApiTest", "Exception during testRotationCommands: ${e.message}", e)
        }
    }

    public fun registerStatusListeners(lifecycleOwner: LifecycleOwner) {
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
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    RobotApi.getInstance().unregisterStatusListener(statusListener)
                    Log.i("RobotApiManager", "StatusListener unregistered")
                    super.onDestroy(owner)
                }
            })
        } catch (e: Exception) {
            Log.e("RobotApiManager", "Failed to register status listeners: ${e.message}", e)
        }
    }

    public suspend fun rotateRobotSync(direction: String, reqId: Int, speed: Float, angle: Float? = null): Boolean {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val rotateListener = object : CommandListener() {
                    override fun onResult(result: Int, message: String) {
                        if ("succeed".equals(message, ignoreCase = true)) {
                            Log.i("BasicMotion", "Rotation $direction succeeded")
                            continuation.resume(true) {}
                        } else {
                            Log.e("BasicMotion", "Rotation $direction failed: $message")
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
                            Log.e("BasicMotion", "Invalid direction: $direction")
                            continuation.resume(false) {}
                        }
                    }
                } catch (e: Exception) {
                    Log.e("BasicMotion", "Failed to execute rotation: ${e.message}", e)
                    continuation.resume(false) {}
                }
            }
        }
    }

    public fun rotateRobotAsync(direction: String, reqId: Int, speed: Float, angle: Float? = null, onComplete: (Boolean) -> Unit = {}) {
        val rotateListener = object : CommandListener() {
            override fun onResult(result: Int, message: String) {
                if ("succeed".equals(message, ignoreCase = true)) {
                    Log.i("BasicMotion", "Rotation $direction succeeded")
                    onComplete(true)
                } else {
                    Log.e("BasicMotion", "Rotation $direction failed: $message")
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
                    Log.e("BasicMotion", "Invalid direction: $direction")
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            Log.e("BasicMotion", "Failed to execute rotation: ${e.message}", e)
            onComplete(false)
        }
    }

    // public fun setOrderCallback() {
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