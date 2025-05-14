package com.example.restaurant
import com.example.restaurant.ui.theme.RestaurantTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration 
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

// components
import com.example.restaurant.components.TileButton
// robot_utils
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
        // RobotApiManager.connectToServer(this, this)

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

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAskOrder: () -> Unit = {},
    onNavigateToDeliverOrder: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image depending if portrait or landscape
        if (isPortrait) {
            Image(
                painter = painterResource(id = R.drawable.v_table), 
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.h_table), 
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Foreground content
        if (isPortrait) {
            // Grid of 3x1 for portrait mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Padding around the grid
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TileButton(
                    text = "Settings",
                    imageResId = R.drawable.v_rectangle,
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TileButton(
                    text = "Ask Order",
                    imageResId = R.drawable.v_rectangle,
                    onClick = onNavigateToAskOrder,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TileButton(
                    text = "Deliver Order",
                    imageResId = R.drawable.v_rectangle,
                    onClick = onNavigateToDeliverOrder,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                )
            }
        } else {
            // Grid of 1x3 for landscape mode
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Padding around the grid
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TileButton(
                    text = "Settings",
                    imageResId = R.drawable.h_rectangle,
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TileButton(
                    text = "Ask Order",
                    imageResId = R.drawable.h_rectangle,
                    onClick = onNavigateToAskOrder,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TileButton(
                    text = "Deliver Order",
                    imageResId = R.drawable.h_rectangle,
                    onClick = onNavigateToDeliverOrder,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 284)
@Composable
fun MainScreen_TabletPreview() {
    RestaurantTheme {
        MainScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 568)
@Composable
fun MainScreen_PhonePreview() {
    RestaurantTheme {
        MainScreen()
    }
}
