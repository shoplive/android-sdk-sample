package cloud.shoplive.sample.views.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.DeeplinkInfo

class FirstViewModel : ViewModel() {

    private val _liveInfo: MutableLiveData<DeeplinkInfo> = MutableLiveData()
    val liveInfo: LiveData<DeeplinkInfo>
        get() = _liveInfo

    fun playFromDeeplink(accessKey: String, campaignKey: String) {
        _liveInfo.value = DeeplinkInfo(accessKey, campaignKey)
    }
}