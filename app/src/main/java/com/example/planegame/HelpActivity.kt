package com.example.planegame

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)
        val myWebView: WebView = findViewById(R.id.webview)
        // enable javascript
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://rinchan01.github.io/help-web/")
    }

}
