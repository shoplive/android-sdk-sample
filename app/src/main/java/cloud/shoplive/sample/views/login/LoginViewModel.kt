package cloud.shoplive.sample.views.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sdk.ShopLiveUser

class LoginViewModel : ViewModel() {

    private var _done: MutableLiveData<String> = MutableLiveData()
    val done: LiveData<String>
        get() = _done

    fun saveUser(context: Context, id: String) {
        CampaignSettings.authType(context, CampaignSettings.UserType.USER.ordinal)
        CampaignSettings.user(context, ShopLiveUser().apply { userId = id })
        _done.value = id
    }
}