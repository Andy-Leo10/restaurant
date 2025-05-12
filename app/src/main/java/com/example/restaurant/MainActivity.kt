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

// components
import com.example.restaurant.components.ScreenSizeComponent
// robot_utils
import com.example.restaurant.robot_utils.RobotApiManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RobotApiManager.connectToServer(this, this)

        setContent {
            RestaurantTheme {
                AppNavigation()
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
