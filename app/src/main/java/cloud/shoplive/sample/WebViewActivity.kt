package cloud.shoplive.sample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WebViewActivity"

        fun buildIntent(context: Context, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra("url", url)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val url = intent.getStringExtra("url")
        Log.d(TAG, "url >> $url")

        url?.let {
            val fragment = WebViewDialogFragment()
            fragment.arguments = Bundle().apply {
                putString("url", it)
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.webView_container, fragment)
                .commit()
        }
    }
}