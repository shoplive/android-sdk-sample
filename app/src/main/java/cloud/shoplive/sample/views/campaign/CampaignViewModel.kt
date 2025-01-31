package cloud.shoplive.sample.views.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtil
import javax.inject.Inject

class CampaignViewModel @Inject constructor(private val preferencesUtil: PreferencesUtil) : ViewModel() {

    private val _campaignInfo: MutableLiveData<CampaignInfo> = MutableLiveData()
    val campaignInfo: LiveData<CampaignInfo>
        get() = _campaignInfo


    fun loadCampaign() {
        _campaignInfo.value =
            CampaignInfo(
                preferencesUtil.accessKey,
                preferencesUtil.campaignKey
            )
    }

    fun saveCampaign(campaignInfo: CampaignInfo?) {
        campaignInfo ?: return

        if (campaignInfo.accessKey?.isNotEmpty() == true || campaignInfo.campaignKey?.isNotEmpty() == true) {
            preferencesUtil.accessKey = campaignInfo.accessKey
            preferencesUtil.campaignKey = campaignInfo.campaignKey
        }
    }
}