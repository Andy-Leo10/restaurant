package com.example.restaurant
import com.example.restaurant.ui.theme.RestaurantTheme

import android.os.Bundle
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
import android.util.Log
import androidx.compose.ui.platform.LocalConfiguration 
import android.content.res.Configuration 
import androidx.compose.foundation.shape.RoundedCornerShape

// components
import com.example.restaurant.components.ScreenSizeComponent
// robot_utils
import com.example.restaurant.robot_utils.RobotApiManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (isPortrait) {
            // Grid of 3x1 for portrait mode
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .fillMaxWidth(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Settings")
                }
                Spacer(modifier = Modifier.height(8.dp)) 
                Button(
                    onClick = onNavigateToAskOrder,
                    modifier = Modifier
                        .fillMaxWidth(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Ask Order")
                }
                Spacer(modifier = Modifier.height(8.dp)) 
                Button(
                    onClick = onNavigateToDeliverOrder,
                    modifier = Modifier
                        .fillMaxWidth(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)   
                ) {
                    Text("Deliver Order")
                }
            }
        } else {
            // Grid of 1x3 for landscape mode
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateToSettings,
                    modifier = Modifier
                        .fillMaxHeight(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)    
                ) {
                    Text("Settings")
                }
                Spacer(modifier = Modifier.width(16.dp)) 
                Button(
                    onClick = onNavigateToAskOrder,
                    modifier = Modifier
                        .fillMaxHeight(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)    
                ) {
                    Text("Ask Order")
                }
                Spacer(modifier = Modifier.width(16.dp)) 
                Button(
                    onClick = onNavigateToDeliverOrder,
                    modifier = Modifier
                        .fillMaxHeight(0.5f) 
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp)    
                ) {
                    Text("Deliver Order")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 284)
@Composable
fun TabletPreview() {
    RestaurantTheme {
        MainScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 568)
@Composable
fun PhonePreview() {
    RestaurantTheme {
        MainScreen()
    }
}
