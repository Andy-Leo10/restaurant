package com.example.restaurant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

@Composable
fun ScreenSizeComponent() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthPx = (screenWidthDp * density).toInt()
    val screenHeightPx = (screenHeightDp * density).toInt()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Screen INFO:")
        Text("Screen size: ${screenWidthDp}dp x ${screenHeightDp}dp")
        Text("Screen size: ${screenWidthPx}px x ${screenHeightPx}px")
    }
}