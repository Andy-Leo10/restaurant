package com.example.restaurant.screens
import com.example.restaurant.ui.theme.RestaurantTheme
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import com.example.restaurant.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
// components
import com.example.restaurant.components.WebViewScreen
import com.example.restaurant.components.TileButton

@Composable
fun AskOrderScreen(onBackClick: () -> Unit = {}) {
    var showWebView by remember { mutableStateOf(false) }

    if (showWebView) {
        WebViewScreen(
            url = "https://zox-peru-web.lovable.app/",
            onClose = { showWebView = false }
        )
    } 
    else {
        // AskOrderScreen
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image depending on orientation
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Return button in the top-left corner and title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f), // vertically 20% of the screen
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                        Text(
                            text = "Select table to ask order",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                    // Image on the top and button on the bottom
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f), // vertically 80% of the screen
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        // Image on the top
                        Image(
                            painter = painterResource(id = R.drawable.icon_0), // Replace with your image resource
                            contentDescription = null,
                            modifier = Modifier
                                .weight(0.5f), // vertically 50% of the screen
                            contentScale = ContentScale.Fit
                        )
                        // Button on the bottom inside a Box
                        Box(
                            modifier = Modifier
                                .weight(0.5f), // vertically 50% of the screen
                            contentAlignment = Alignment.Center // Center the button inside the Box
                        ) {
                        TileButton(
                                text = "View Menu",
                                imageResId = R.drawable.v_mirror,
                                onClick = {showWebView = true},
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Return button in the top-left corner and title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f), // vertically 20% of the screen
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                        Text(
                            text = "Select table to ask order",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                    // 1x2 Grid layout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f), // vertically 80% of the screen
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image on the left
                        Image(
                            painter = painterResource(id = R.drawable.icon_0), // Replace with your image resource
                            contentDescription = null,
                            modifier = Modifier
                                .weight(0.5f), // horizontally 50% of the screen
                            contentScale = ContentScale.Fit
                        )
                        // Button on the right inside a Box
                        Box(
                            modifier = Modifier
                                .weight(0.5f), // horizontally 50% of the screen
                            contentAlignment = Alignment.Center // Center the button inside the Box
                        ) {
                        TileButton(
                                text = "View Menu",
                                imageResId = R.drawable.v_mirror,
                                onClick = {showWebView = true},
                                modifier = Modifier
                                    .fillMaxWidth(0.66f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 284)
@Composable
fun AskOrderScreen_TabletPreview() {
    RestaurantTheme {
        AskOrderScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 568)
@Composable
fun AskOrderScreen_PhonePreview() {
    RestaurantTheme {
        AskOrderScreen()
    }
}