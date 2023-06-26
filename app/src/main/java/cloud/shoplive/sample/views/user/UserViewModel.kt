package cloud.shoplive.sample.views.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sdk.ShopLiveUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {

    private val _user: MutableLiveData<ShopLiveUser> = MutableLiveData()
    val user: LiveData<ShopLiveUser>
        get() = _user

    private val _jwt: MutableLiveData<String> = MutableLiveData()
    val jwt: LiveData<String>
        get() = _jwt

    fun loadUserData(context: Context) {
        _user.value = CampaignSettings.user(context)
    }

    fun loadJwt(context: Context) {
        _jwt.value = CampaignSettings.jwt(context)
    }

    suspend fun saveUserData(context: Context, user: ShopLiveUser) {
        withContext(Dispatchers.IO) {
            CampaignSettings.user(context, user)
        }
    }

    suspend fun saveJwt(context: Context, jwt: String) {
        withContext(Dispatchers.IO) {
            CampaignSettings.jwt(context, jwt)
        }
    }

    suspend fun saveAuthType(context: Context, type: CampaignSettings.UserType) {
        withContext(Dispatchers.IO) {
            CampaignSettings.authType(context, type.ordinal)
        }
    }
}