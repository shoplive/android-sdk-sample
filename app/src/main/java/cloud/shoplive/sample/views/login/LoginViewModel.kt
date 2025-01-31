package cloud.shoplive.sample.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.UserType
import cloud.shoplive.sdk.ShopLiveUser
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val preferencesUtil: PreferencesUtil) :
    ViewModel() {

    private var _done: MutableLiveData<String> = MutableLiveData()
    val done: LiveData<String>
        get() = _done

    fun saveUser(id: String) {
        preferencesUtil.authType = UserType.USER.ordinal
        preferencesUtil.user = ShopLiveUser().apply { userId = id }
        _done.value = id
    }
}