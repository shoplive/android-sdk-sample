package cloud.shoplive.sample.views.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveUser
import javax.inject.Inject

class MainViewModel @Inject constructor(private val preferences : PreferencesUtil) : ViewModel() {

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

    fun getUserData() = preferences.user

    fun getAuthType() = preferences.authType

    fun getJWT() = preferences.jwt

    fun getAccessKey() = preferences.accessKey

    fun getCampaignKey() = preferences.campaignKey

    fun setAccessKey(value: String) {
        preferences.accessKey = value
    }

    fun setCampaignKey(value: String) {
        preferences.campaignKey = value
    }

    fun playFromDeeplink(accessKey: String, campaignKey: String) {
        _deeplinkInfo.value = CampaignInfo(accessKey, campaignKey)
    }

    fun getSdkVersion() {
        _sdkVersion.value = ShopLive.getSDKVersion()
    }

    fun loadCampaignData() {
        _campaignInfo.value =
            CampaignInfo(preferences.accessKey, preferences.campaignKey)
    }

    fun loadUserData() {
        _shopliveUser.value =
            preferences.user
    }
}