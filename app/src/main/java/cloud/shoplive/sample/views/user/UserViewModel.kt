package cloud.shoplive.sample.views.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.UserType
import cloud.shoplive.sdk.ShopLiveUser
import javax.inject.Inject

class UserViewModel @Inject constructor(private val preferencesUtil: PreferencesUtil) :
    ViewModel() {

    private val _user = MutableLiveData<ShopLiveUser?>()
    val user: LiveData<ShopLiveUser?> get() = _user

    private val _jwt: MutableLiveData<String> = MutableLiveData()
    val jwt: LiveData<String>
        get() = _jwt

    private val _authType: MutableLiveData<Int> = MutableLiveData()
    val authType: LiveData<Int>
        get() = _authType

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        _jwt.value = preferencesUtil.jwt
        _authType.value = preferencesUtil.authType
        _user.value = preferencesUtil.user
    }

    fun saveUserData(user: ShopLiveUser) {
        preferencesUtil.user = user
        _user.value = preferencesUtil.user
    }

    fun saveJwt(jwt: String) {
        preferencesUtil.jwt = jwt
        _jwt.value = preferencesUtil.jwt
    }

    fun saveAuthType(type: UserType) {
        preferencesUtil.authType = type.ordinal
        _authType.value = preferencesUtil.authType
    }
}