package com.wkwn.newwws.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import com.wkwn.newwws.R
import kotlinx.android.synthetic.main.activity_browser.*
import android.widget.ProgressBar
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast


class BrowserActivity : AppCompatActivity() {
    private lateinit var url: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        url = Uri.parse(intent.getStringExtra("url"))
        webView.loadUrl(url.toString())

        browser_toolbar.setNavigationOnClickListener { finish() }
        browser_toolbar.subtitle = url.toString()

        browser_toolbar.inflateMenu(R.menu.browser)
        browser_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh -> {
                    webView.reload()
                    true
                }
                R.id.copyLink -> {
                    val clipboard = this@BrowserActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("", url.toString())
                    clipboard.primaryClip = clip
                    Toast.makeText(this@BrowserActivity, R.string.okCopy, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.openInBrowser -> {
                    val browser = Intent(Intent.ACTION_VIEW).setData(url)
                    finish()
                    startActivity(browser)
                    true
                }
                else -> false
            }
        }

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        @SuppressLint("SetJavaScriptEnabled")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress < 100 && browser_pb.visibility == ProgressBar.GONE) {
                    browser_pb.visibility = ProgressBar.VISIBLE
                }
                browser_pb.progress = progress
                if (progress == 100) {
                    browser_pb.visibility = ProgressBar.GONE
                    browser_toolbar.title = webView.title
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            finish()
    }
}