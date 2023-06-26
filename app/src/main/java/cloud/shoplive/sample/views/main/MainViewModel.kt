package cloud.shoplive.sample.views.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveUser

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

//    fun playFromDeeplink(accessKey: String, campaignKey: String) {
//        _liveInfo.value = CampaignInfo(accessKey, campaignKey)
//    }

    fun getSdkVersion() {
        _sdkVersion.value = ShopLive.getVersion()
    }

    fun loadCampaignData(context: Context) {
        _campaignInfo.value = CampaignInfo(CampaignSettings.accessKey(context), CampaignSettings.campaignKey(context))
    }

    fun loadUserData(context: Context) {
        _shopliveUser.value = CampaignSettings.user(context)
    }
}