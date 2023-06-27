package cloud.shoplive.sample.views.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.SchemeActivity
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _campaignInfo: MutableLiveData<CampaignInfo> = MutableLiveData()
    val campaignInfo: LiveData<CampaignInfo>
        get() = _campaignInfo

    private val _shopliveUser: MutableLiveData<ShopLiveUser> = MutableLiveData()
    val shopliveUser: LiveData<ShopLiveUser>
        get() = _shopliveUser

    private val _sdkVersion: MutableLiveData<String> = MutableLiveData()
    val sdkVersion: LiveData<String>
        get() = _sdkVersion

    private val _deeplinkInfo: MutableLiveData<CampaignInfo> = MutableLiveData()
    val deeplinkInfo: LiveData<CampaignInfo>
        get() = _deeplinkInfo


    fun playFromDeeplink(data: Intent) {
        _deeplinkInfo.value = CampaignInfo(
            data.getStringExtra(SchemeActivity.ACCESS_KEY),
            data.getStringExtra(SchemeActivity.CAMPAIGN_KEY)
        )
    }

    fun getSdkVersion() {
        _sdkVersion.value = ShopLive.getVersion()
    }

    suspend fun loadCampaignData(context: Context) {
        _campaignInfo.value = withContext(Dispatchers.IO) {
            CampaignInfo(CampaignSettings.accessKey(context), CampaignSettings.campaignKey(context))
        }
    }

    suspend fun loadUserData(context: Context) {
        _shopliveUser.value = withContext(Dispatchers.IO) {
            CampaignSettings.user(context)
        }
    }
}