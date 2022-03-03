package com.example.if3210_64

import android.content.Intent
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)

        val webView : WebView = findViewById<WebView>(R.id.article_layout)
        val weburl = getIntent().getStringExtra("url").toString()
        webView.loadUrl(weburl)
    }
}