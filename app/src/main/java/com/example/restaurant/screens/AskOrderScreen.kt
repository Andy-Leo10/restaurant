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

@Composable
fun AskOrderScreen(onBackClick: () -> Unit = {}) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image depending on orientation
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Return button in the top-left corner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBackClick) {
                    Text("←") // Replace with an icon if desired
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Select table to ask order",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1x2 Grid layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image on the left
                Image(
                    painter = painterResource(id = R.drawable.icon_0), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Button on the right
                Button(
                    onClick = { /* Add your button action here */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Menu")
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