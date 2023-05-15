package cloud.shoplive.sample.shortform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.databinding.ActivityShortformProductBinding
import cloud.shoplive.sdk.shorts.ShopLiveShortform

class ShortformProductActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context, url: String): Intent {
            return Intent(context, ShortformProductActivity::class.java).apply {
                putExtra("url", url)
            }
        }
    }

    private val binding: ActivityShortformProductBinding by lazy {
        ActivityShortformProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(false)
            webView.settings.domStorageEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true

            ShopLiveShortform.connectBridgeInterface(this@ShortformProductActivity, webView)
            ShopLiveShortform.receiveBridgeInterface(this@ShortformProductActivity, webView)

            webView.loadUrl(intent.getStringExtra("url") ?: let {
                Toast.makeText(this@ShortformProductActivity, "Url is empty.", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return
            })
        }
    }
}