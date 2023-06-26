package cloud.shoplive.sample.views.campaign

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.CampaignSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CampaignViewModel : ViewModel() {

    private val _campaignInfo: MutableLiveData<CampaignInfo> = MutableLiveData()
    val campaignInfo: LiveData<CampaignInfo>
        get() = _campaignInfo

    suspend fun loadCampaign(context: Context) {
        _campaignInfo.value =
            withContext(Dispatchers.IO) {
                CampaignInfo(
                    CampaignSettings.accessKey(context),
                    CampaignSettings.campaignKey(context)
                )
            }
    }

    suspend fun saveCampaign(context: Context, campaignInfo: CampaignInfo?) {
        campaignInfo ?: return

        if (campaignInfo.accessKey?.isNotEmpty() == true && campaignInfo.campaignKey?.isNotEmpty() == true) {
            withContext(Dispatchers.IO) {
                CampaignSettings.setAccessKey(context, campaignInfo.accessKey)
                CampaignSettings.setCampaignKey(context, campaignInfo.campaignKey)
            }
        }
    }
}