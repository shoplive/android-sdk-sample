package cloud.shoplive.sample.views.user

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.UserType
import cloud.shoplive.sample.getOrAwaitValue
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private val preference = mockk<PreferencesUtil>(relaxed = true)
    private val testUser = ShopLiveUser(
        userId = "testId2",
        userName = "testName2",
        age = 0,
        gender = ShopLiveUserGender.Female
    )

    @Before
    fun setUp() {
        viewModel = UserViewModel(preference)
    }

    @Test
    fun getUser() {
        every { preference.user } returns testUser
        viewModel.loadInitialData()
        assertEquals(testUser, viewModel.user.getOrAwaitValue())
    }

    @Test
    fun getJwt() {
       every { preference.jwt } returns "testJWT"
        viewModel.loadInitialData()
        assertEquals("testJWT", viewModel.jwt.getOrAwaitValue())
    }

    @Test
    fun getAuthType() {
        every { preference.authType } returns 1
        viewModel.loadInitialData()
        assertEquals(1, viewModel.authType.getOrAwaitValue())
    }

    @Test
    fun loadUserData() {
        every { preference.user } returns testUser
        viewModel.loadInitialData()
        assertEquals(testUser, viewModel.user.getOrAwaitValue())
    }

    @Test
    fun loadJwt() {
        every { preference.jwt } returns "jwt"
        viewModel.loadInitialData()
        assertEquals("jwt", viewModel.jwt.getOrAwaitValue())
    }

    @Test
    fun loadType() {
        every { preference.authType } returns UserType.JWT.ordinal
        viewModel.loadInitialData()
        assertEquals(UserType.JWT.ordinal, viewModel.authType.getOrAwaitValue())
    }

    @Test
    fun saveUserData() {
        val testData = ShopLiveUser(
            userId = "testId",
            userName = "testName",
            age = 0,
            gender = ShopLiveUserGender.Female
        )
        every { preference.user } returns testData

        viewModel.saveUserData(testData)

        assertEquals(testData, viewModel.user.getOrAwaitValue())
    }

    @Test
    fun saveJwt() {
        val testJwt = "testJwt"
        every { preference.jwt } returns testJwt
        viewModel.saveJwt(testJwt)
        verify { preference.jwt = testJwt }
        assertEquals(testJwt, viewModel.jwt.getOrAwaitValue())
    }

    @Test
    fun saveAuthType() {
        val testAuthType = UserType.GUEST
        viewModel.saveAuthType(
            testAuthType
        )
        verify {
            preference.authType = testAuthType.ordinal
        }
    }
}