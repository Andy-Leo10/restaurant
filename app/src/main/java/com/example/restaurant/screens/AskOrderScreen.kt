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
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AskOrderScreen(onBackClick: () -> Unit = {}) {
    var showWebView by remember { mutableStateOf(false) }

    if (showWebView) {
        // WebView Screen
        Box(modifier = Modifier.fillMaxSize()) {
            // WebView
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true // Enable JavaScript
                        settings.domStorageEnabled = true // Enable DOM storage
                        settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" // Custom user-agent
                        // webViewClient = WebViewClient()

                        // Add a JavaScript interface to communicate with Kotlin
                        addJavascriptInterface(object {
                            @JavascriptInterface
                            fun onMyButtonClicked(searchValue: String) {
                                // Log the search value in your program
                                Log.d("WebView", "Search button clicked with value: $searchValue")
                            }
                        }, "AndroidBridge")

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
            
                                // Inject JavaScript to write into the input box
                                view?.evaluateJavascript(
                                    """
                                    document.getElementById('searchInput').value = 'Hello World';
                                    """.trimIndent(),
                                    null
                                )
            
                                // Inject JavaScript to listen for the search button click
                                view?.evaluateJavascript(
                                    """
                                    document.querySelector('button[type="submit"]').addEventListener('click', function() {
                                        var searchValue = document.getElementById('searchInput').value;
                                        AndroidBridge.onMyButtonClicked(searchValue);
                                    });
                                    """.trimIndent(),
                                    null
                                )
                            }
                        }

                        loadUrl("https://www.wikipedia.org/") // https://zox-peru-web.lovable.app/
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Floating Action Button to close WebView
            FloatingActionButton(
                onClick = { showWebView = false },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close WebView")
            }
        }
    } else {
        // Main Screen
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.padding(start = 8.dp)
                        )
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
                        onClick = { showWebView = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Menu")
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