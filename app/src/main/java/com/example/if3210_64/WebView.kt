package com.example.if3210_64

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class WebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)

        val webView: WebView = findViewById<WebView>(R.id.article_layout)
        val weburl = getIntent().getStringExtra("url").toString()
        webView.loadUrl(weburl)
    }
}