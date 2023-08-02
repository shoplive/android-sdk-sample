package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.databinding.ActivityHybridShortformBinding
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.network.ShopLiveNetwork
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformFullTypeHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData

class HybridShortformActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context, url: String): Intent {
            return Intent(context, HybridShortformActivity::class.java).apply {
                putExtra("url", url)
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
    }

    private val binding: ActivityHybridShortformBinding by lazy {
        ActivityHybridShortformBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.webView.settings.setSupportZoom(false)
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.defaultTextEncodingName = "UTF-8"
        binding.webView.settings.setSupportMultipleWindows(true)
        binding.webView.settings.textZoom = 100
        binding.webView.settings.domStorageEnabled = true // Required
        binding.webView.settings.javaScriptEnabled = true // Required

        // Required
        ShopLiveShortform.connectBridgeInterface(this, binding.webView)
        ShopLiveShortform.receiveBridgeInterface(this, binding.webView)

        intent.getStringExtra("url")?.let {
            binding.webView.loadUrl(it)
        }

        binding.webView.webViewClient = object : WebViewClient() {
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
                    this@HybridShortformActivity,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onShare(activity: Activity, data: ShopLiveShortformShareData) {
                // Do sharing
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ShopLiveCommon.clearAuth()
        ShopLiveNetwork.clearShortsConfig()
    }
}