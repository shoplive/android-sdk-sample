package cloud.shoplive.sample.views.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.UserType
import cloud.shoplive.sample.getOrAwaitValue
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveUser
import cloud.shoplive.sdk.ShopLiveUserGender
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val preference: PreferencesUtil = mockk<PreferencesUtil>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(preference)
    }

    @Test
    fun getCampaignInfo() {
        every { preference.accessKey } returns "testA"
        every { preference.campaignKey } returns "testC"

        viewModel.loadCampaignData()

        assertEquals(viewModel.campaignInfo.getOrAwaitValue(), CampaignInfo("testA", "testC"))
    }

    @Test
    fun getShopliveUser() {
        val testUser = ShopLiveUser(
            userId = "testId",
            userName = "testName",
            age = 11,
            gender = ShopLiveUserGender.Neutral
        )
        every {
            preference.user
        } returns testUser

        viewModel.loadUserData()

        assertEquals(testUser, viewModel.shopliveUser.getOrAwaitValue())

    }

    @Test
    fun getSdkVersion() {
        viewModel.getSdkVersion()
        assertEquals(ShopLive.getSDKVersion(), viewModel.sdkVersion.getOrAwaitValue())
    }

    @Test
    fun getDeeplinkInfo() {
        viewModel.playFromDeeplink("accessKey", "campaignKey")

        assertEquals(
            CampaignInfo("accessKey", "campaignKey"),
            viewModel.deeplinkInfo.getOrAwaitValue()
        )
    }

    @Test
    fun getUserData() {
        val testUser = ShopLiveUser(
            userId = "id",
            userName = "name"
        )
        every { preference.user } returns testUser

        assertEquals(testUser, viewModel.getUserData())
    }

    @Test
    fun getAuthType() {
        every { preference.authType } returns UserType.USER.ordinal

        assertEquals(UserType.USER.ordinal, viewModel.getAuthType())
    }

    @Test
    fun getJWT() {
        every { preference.jwt } returns "jwt"

        assertEquals("jwt", viewModel.getJWT())
    }

    @Test
    fun getAccessKey() {
        every { preference.accessKey } returns "accessKey"

        assertEquals("accessKey", viewModel.getAccessKey())
    }

    @Test
    fun getCampaignKey() {
        every { preference.campaignKey } returns "campaignKey"

        assertEquals("campaignKey", viewModel.getCampaignKey())
    }

    @Test
    fun setAccessKey() {
        viewModel.setAccessKey("test")
        verify { preference.accessKey = "test" }
    }

    @Test
    fun setCampaignKey() {
        viewModel.setCampaignKey("test")
        verify { preference.campaignKey = "test" }
    }

    @Test
    fun playFromDeeplink() {
        viewModel.playFromDeeplink("access", "campaign")
        assertEquals(CampaignInfo("access", "campaign"), viewModel.deeplinkInfo.getOrAwaitValue())
    }

    @Test
    fun testGetSdkVersion() {
        viewModel.getSdkVersion()
        assertEquals(viewModel.sdkVersion.getOrAwaitValue(), ShopLive.getSDKVersion())
    }

    @Test
    fun loadCampaignData() {
        every { preference.accessKey } returns "accTest"
        every { preference.campaignKey } returns "camTest"

        viewModel.loadCampaignData()

        assertEquals(viewModel.campaignInfo.getOrAwaitValue(), CampaignInfo("accTest", "camTest"))
    }

    @Test
    fun loadUserData() {
        val testUser = ShopLiveUser(
            userId = "userId",
            userName = "userName",
            age = 12,
        )
        every {
            preference.user
        } returns testUser

        viewModel.loadUserData()

        assertEquals(testUser, viewModel.shopliveUser.getOrAwaitValue())
    }
}

