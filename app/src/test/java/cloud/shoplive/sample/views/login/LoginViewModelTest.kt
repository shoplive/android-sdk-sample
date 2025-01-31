package cloud.shoplive.sample.views.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sdk.ShopLiveUser
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var preference: PreferencesUtil
    private lateinit var viewModel: LoginViewModel
    private val doneInfoObserver: Observer<String> = mockk(relaxed = true)
    private val mockStorage = mockk<KeyValueStorage>(relaxed = true)

    @Before
    fun setUp() {
        preference = PreferencesUtilImpl(mockStorage)
        viewModel = LoginViewModel(preference)

        viewModel.done.observeForever(doneInfoObserver)
    }

    @Test
    fun saveUser() {
        val id = "test"
        val id_2 = "test2"

        viewModel.saveUser(id)

        verify { preference.user = ShopLiveUser().apply { userId = id } }
        verify { doneInfoObserver.onChanged("test") }

        viewModel.saveUser(id_2)

        verify { preference.user = ShopLiveUser().apply { userId = id } }
        verify { doneInfoObserver.onChanged("test2") }
    }
}