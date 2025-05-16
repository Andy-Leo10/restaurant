package com.example.restaurant
import com.example.restaurant.ui.theme.RestaurantTheme

import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*

// components
import com.example.restaurant.components.TileButton
// robot_utils
import com.example.restaurant.robot_utils.RobotApiManager
// screens
import com.example.restaurant.screens.MainScreen
import com.example.restaurant.screens.AskOrderScreen
import com.example.restaurant.screens.DeliverOrderScreen
import com.example.restaurant.screens.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Enable immersive mode
        enableImmersiveMode()
        // Connect to the robot API
        RobotApiManager.connectToServer(this, this)

        setContent {
            RestaurantTheme {
                AppNavigation()
            }
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

@Preview(showBackground = true, widthDp = 500, heightDp = 284)
@Composable
fun AppNavigation_TabletPreview() {
    RestaurantTheme {
        AppNavigation()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 568)
@Composable
fun AppNavigation_PhonePreview() {
    RestaurantTheme {
        AppNavigation()
    }
}
