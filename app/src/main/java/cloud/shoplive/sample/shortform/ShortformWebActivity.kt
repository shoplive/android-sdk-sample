package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.R
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.network.ShopLiveNetwork
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformFullTypeHandler

class ShortformWebActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context, url: String): Intent {
            return Intent(context, ShortformWebActivity::class.java).apply {
                putExtra("url", url)
            }
        }
    }

    private val webView by lazy {
        findViewById<WebView>(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortform_webview)

        webView.settings.setSupportZoom(false)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.defaultTextEncodingName = "UTF-8"
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.textZoom = 100
        webView.settings.domStorageEnabled = true // Required
        webView.settings.javaScriptEnabled = true // Required

        // Required
        ShopLiveShortform.connectBridgeInterface(this, webView)
        ShopLiveShortform.receiveBridgeInterface(this, webView)

        intent.getStringExtra("url")?.let {
            webView.loadUrl(it)
        }

        webView.webViewClient = object : WebViewClient() {
            // Required
            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                ShopLiveShortform.updateVisitedHistory(view, url, isReload)
            }
        }

        // Optional
        ShopLiveShortform.setHandler(object : ShopLiveShortformFullTypeHandler() {
            override fun onError(error: ShopLiveCommonError) {
                Toast.makeText(
                    this@ShortformWebActivity,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onShare(activity: Activity, data: ShopLiveShortformData?, url: String?) {
                // sharing
            }
        })
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ShopLiveCommon.clearAuth()
        ShopLiveNetwork.clearShortsConfig()
    }
}