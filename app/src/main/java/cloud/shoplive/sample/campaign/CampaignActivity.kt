package cloud.shoplive.sample.campaign

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.R
import cloud.shoplive.sample.base.ToolbarActivity
import com.google.android.material.textfield.TextInputEditText

class CampaignActivity: ToolbarActivity() {

    lateinit var etAccessKey: TextInputEditText
    lateinit var etCampaignKey: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etAccessKey = findViewById(R.id.etAccessKey)
        etCampaignKey = findViewById(R.id.etCampaignKey)

        etAccessKey.setText(CampaignSettings.accessKey(this))
        etCampaignKey.setText(CampaignSettings.campaignKey(this))
    }

    override fun layout(): Int {
        return R.layout.activity_campaign
    }

    override fun toolbarTitle(): String {
        return getString(R.string.title_campaign)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.campaign_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_save -> {
                val accessKey = etAccessKey.text.toString()
                val campaignKey = etCampaignKey.text.toString()

                CampaignSettings.accessKey(this, accessKey)
                CampaignSettings.campaignKey(this, campaignKey)

                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}