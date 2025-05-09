package com.example.restaurant
import com.example.restaurant.SettingsScreen
import com.example.restaurant.AskOrderScreen
import com.example.restaurant.DeliverOrderScreen

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurant.ui.theme.RestaurantTheme

import com.ainirobot.coreservice.IRobotSettingApi
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.ApiListener
import android.util.Log

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
        try {
            RobotApi.getInstance().connectServer(this, object : ApiListener {
                override fun handleApiDisabled() {
                    // Handle API disabled scenario
                    Log.e("MainActivity", "API is disabled")
                }
    
                override fun handleApiConnected() {
                    // Server is connected, set the callback for receiving requests
                    Log.i("MainActivity", "API connected successfully")
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
