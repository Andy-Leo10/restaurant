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
fun DeliverOrderScreen(onBackClick: () -> Unit = {}) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Deliver Order Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackClick) {
                Text("Back to Main Screen")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 284)
@Composable
fun DeliverOrderScreen_TabletPreview() {
    RestaurantTheme {
        DeliverOrderScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 568)
@Composable
fun DeliverOrderScreen_PhonePreview() {
    RestaurantTheme {
        DeliverOrderScreen()
    }
}