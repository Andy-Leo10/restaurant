package com.example.restaurant

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
import com.example.restaurant.ui.theme.RestaurantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantTheme {
                var showWebView by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showWebView) {
                        WebViewScreen(url = "https://www.rappi.com.pe/")
                    } else {
                        ButtonScreen(
                            modifier = Modifier.padding(innerPadding),
                            onButtonClick = { showWebView = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonScreen(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(onClick = onButtonClick) {
            Text("Open WebView")
        }
    }
}

@Composable
fun WebViewScreen(url: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }, modifier = Modifier.fillMaxSize())
}