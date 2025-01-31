package cloud.shoplive.sample.views.campaign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.R
import cloud.shoplive.sample.data.SharedPreferenceStorage
import cloud.shoplive.sample.databinding.ActivityCampaignBinding
import cloud.shoplive.sample.shortform.ShortformViewModel
import kotlinx.coroutines.launch

class CampaignActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, CampaignActivity::class.java)
        }
    }

    private val binding: ActivityCampaignBinding by lazy {
        ActivityCampaignBinding.inflate(layoutInflater)
    }

    private val viewModel: CampaignViewModel by viewModels {
        viewModelFactory {
            initializer {
                CampaignViewModel(PreferencesUtilImpl(SharedPreferenceStorage(this@CampaignActivity)))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_campaign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.campaignInfo.observe(this) {
            binding.etAccessKey.setText(it.accessKey)
            binding.etCampaignKey.setText(it.campaignKey)
        }

        viewModel.loadCampaign()
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