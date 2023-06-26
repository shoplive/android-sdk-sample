package cloud.shoplive.sample.views.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sdk.ShopLiveUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private var _done: MutableLiveData<String> = MutableLiveData()
    val done: LiveData<String>
        get() = _done

    suspend fun saveUser(context: Context, id: String) {
        withContext(Dispatchers.IO) {
            CampaignSettings.authType(context, CampaignSettings.UserType.USER.ordinal)
            CampaignSettings.user(context, ShopLiveUser().apply { userId = id })
        }
        _done.value = id
    }
}