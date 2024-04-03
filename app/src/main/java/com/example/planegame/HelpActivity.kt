package com.example.planegame

import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)
        val webView = findViewById<WebView>(R.id.webview)

        webView.webViewClient = WebViewClient()

        webView.apply {
            loadUrl("https://rinchan01.github.io/help-web/")
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }
    }

}
