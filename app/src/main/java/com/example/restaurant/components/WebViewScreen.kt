package com.example.restaurant.components

import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp

@Composable
fun WebViewScreen(
    url: String,
    onClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true // Enable JavaScript
                    settings.domStorageEnabled = true // Enable DOM storage
                    settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" // Custom user-agent
                    webViewClient = WebViewClient() // simple instance of WebViewClient if you don't need to inject JavaScript

                    /*
                    ATTENTION: This is where you can add your custom JavaScript code to interact with the webpage.
                    You can inject JavaScript to manipulate the DOM, listen for events, etc.
                    - Example: Inject JavaScript to fill in a search box and listen for a button click
                    - Note: Make sure the IDs and classes match those in the actual webpage you are loading.
                    */

                    // // Add a JavaScript interface to communicate with Kotlin
                    // addJavascriptInterface(object {
                    //     @JavascriptInterface
                    //     fun onMyButtonClicked(searchValue: String) {
                    //         // Log the search value in your program
                    //         Log.d("WebView", "Search button clicked with value: $searchValue")
                    //     }
                    // }, "AndroidBridge")

                    // webViewClient = object : WebViewClient() {
                    //     override fun onPageFinished(view: WebView?, url: String?) {
                    //         super.onPageFinished(view, url)

                    //         // Inject JavaScript to write into the input box
                    //         view?.evaluateJavascript(
                    //             """
                    //             document.getElementById('searchInput').value = 'Hello World';
                    //             """.trimIndent(),
                    //             null
                    //         )

                    //         // Inject JavaScript to listen for the search button click
                    //         view?.evaluateJavascript(
                    //             """
                    //             document.querySelector('button[type="submit"]').addEventListener('click', function() {
                    //                 var searchValue = document.getElementById('searchInput').value;
                    //                 AndroidBridge.onMyButtonClicked(searchValue);
                    //             });
                    //             """.trimIndent(),
                    //             null
                    //         )
                    //     }
                    // }

                    /*
                    END OF ATTENTION
                    */

                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        // Floating Action Button to close WebView
        FloatingActionButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Close WebView")
        }
    }
}