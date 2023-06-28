package cloud.shoplive.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.views.main.MainActivity

class SchemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.data
        val accessKey = data?.getQueryParameter(MainActivity.ACCESS_KEY) ?: return
        val campaignKey = data.getQueryParameter(MainActivity.CAMPAIGN_KEY)

        if (accessKey.isNotEmpty() && !campaignKey.isNullOrEmpty()) {
            startActivity(MainActivity.buildIntentFromDeeplink(this, accessKey, campaignKey))
            finish()
        }
    }
}