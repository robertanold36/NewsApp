package com.robert.lookout.ui.main.viewModel

import android.os.Build
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.robert.lookout.R
import kotlinx.android.synthetic.main.container_web_view.*


class ContainerWebView : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_web_view)

        val title = intent.getStringExtra("article_name")
        val articleLink = intent.getStringExtra("article")



        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(articleLink)
        }
    }
}