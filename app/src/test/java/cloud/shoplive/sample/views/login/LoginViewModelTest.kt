package cloud.shoplive.sample.views.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sample.getOrAwaitValue
import cloud.shoplive.sdk.ShopLiveUser
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val preference: PreferencesUtil by lazy {
        PreferencesUtilImpl(mockStorage)
    }
    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(preference)
    }
    private val mockStorage = mockk<KeyValueStorage>(relaxed = true)
    
    @Test
    fun saveUser() {
        val id = "test"
        val id_2 = "test2"

        viewModel.saveUser(id)

        verify { preference.user = ShopLiveUser().apply { userId = id } }
        assertEquals(viewModel.done.getOrAwaitValue(), id)

        viewModel.saveUser(id_2)

        verify { preference.user = ShopLiveUser().apply { userId = id_2 } }
        assertEquals(viewModel.done.getOrAwaitValue(), id_2)
    }
}