package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.databinding.ActivityHybridShortformBinding
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.network.ShopLiveNetwork
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

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
        val shopliveBridgeInterface = ShopLiveAppBridgeInterface(this, binding.webView)
        binding.webView.addJavascriptInterface(shopliveBridgeInterface, "ShopLiveBridgeInterface")

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
        ShopLiveShortform.setHandler(ShortformSampleData.handler)
    }

    override fun onDestroy() {
        super.onDestroy()
        ShopLiveCommon.clearAuth()
        ShopLiveNetwork.clearShortsConfig()
    }
}

internal class ShopLiveAppBridgeInterface(
    private val activity: Activity,
    private val webView: WebView
) {
    @JavascriptInterface
    fun onReceiveShopliveShortsEvent(shopliveEvent: String, payload: String?) {
        shopliveEvent.let {
            val eventObj = JSONObject(shopliveEvent)
            val cmdName = eventObj.getString("name")
            val metadata = eventObj.getJSONObject("metadata")
            when (cmdName) {
                "PLAY_SHORTFORM_DETAIL" -> {
                    payload?.fromJson<ShopLiveShortformMetaData>()?.let {
                        ShopLiveShortform.play(
                            activity,
                            ShopLiveShortformCollectionData().apply {
                                shortsId = it.shorts?.shortsId
                                handler = ShortformSampleData.handler
                            }
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

@Keep
private data class ShopLiveShortformMetaData(val shorts: ShopLiveShortformData?)

@Keep
private data class ShopLiveShortformData(val shortsId: String?)

private inline fun <reified T> String.fromJson(): T? {
    return try {
        GsonBuilder()
            .serializeNulls().create().fromJson(
                this,
                object : TypeToken<T>() {}.type
            )
    } catch (e: Exception) {
        null
    }
}