package cloud.shoplive.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SchemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.data
        val accessKey = data?.getQueryParameter("ak") ?: return
        val campaignKey = data.getQueryParameter("ck")

        if (accessKey.isNotEmpty() && !campaignKey.isNullOrEmpty()) {
            /*
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("accessKey", accessKey)
            intent.putExtra("campaignKey", campaignKey)
            startActivity(intent)
            */
            startActivity(MainActivity.buildIntentFromDeeplink(this, accessKey, campaignKey))
            finish()
        }
    }
}