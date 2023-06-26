package cloud.shoplive.sample.views.campaign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.ActivityCampaignBinding

class CampaignActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, CampaignActivity::class.java)
        }
    }

    private val binding: ActivityCampaignBinding by lazy {
        ActivityCampaignBinding.inflate(layoutInflater)
    }

    private val viewModel: CampaignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Campaign"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.campaignInfo.observe(this) {
            binding.etAccessKey.setText(it.accessKey)
            binding.etCampaignKey.setText(it.campaignKey)
        }

        viewModel.loadCampaign(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.campaign_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_save -> {
                viewModel.saveCampaign(
                    this,
                    CampaignInfo(
                        binding.etAccessKey.text.toString(),
                        binding.etCampaignKey.text.toString()
                    )
                )
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}