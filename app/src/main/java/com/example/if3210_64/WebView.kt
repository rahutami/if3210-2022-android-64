package com.example.if3210_64

import android.content.Intent
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.title = "Second Activity"
        }

        actionBar!!.title = "Second Activity"

        actionBar.setDisplayHomeAsUpEnabled(true)

        val webView : WebView = findViewById<WebView>(R.id.article_layout)
        val weburl = getIntent().getStringExtra("url").toString()
        webView.loadUrl(weburl)
    }
}