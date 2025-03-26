package cloud.shoplive.sample.views.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.UserType
import cloud.shoplive.sdk.ShopLiveUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(private val preferencesUtil: PreferencesUtil) :
    ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState>
        get() = _loginUiState

    private var _done: MutableLiveData<String> = MutableLiveData()
    val done: LiveData<String>
        get() = _done

    fun onIdChanged(id: String) {
        _loginUiState.update {
            it.copy(
                id = id
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _loginUiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun saveUser() {
        preferencesUtil.authType = UserType.USER.ordinal
        preferencesUtil.user = ShopLiveUser().apply { userId = loginUiState.value.id }
        _done.value = loginUiState.value.id
    }
}

@Immutable
data class LoginUiState(
    val id: String = "",
    val password: String = ""
)