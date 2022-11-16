package cloud.shoplive.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _liveInfo: MutableLiveData<DeeplinkInfo> = MutableLiveData()
    val liveInfo: LiveData<DeeplinkInfo>
        get() = _liveInfo

    fun playFromDeeplink(accessKey: String, campaignKey: String) {
        _liveInfo.value = DeeplinkInfo(accessKey, campaignKey)
    }
}