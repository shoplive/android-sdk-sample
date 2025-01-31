package cloud.shoplive.sample.views.campaign

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sample.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CampaignViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val mockStorage = mockk<KeyValueStorage>(relaxed = true)

    private lateinit var preference: PreferencesUtil
    private lateinit var viewModel: CampaignViewModel

    @Before
    fun setUp() {
        preference = PreferencesUtilImpl(mockStorage)
        viewModel = CampaignViewModel(preference)
    }

    @Test
    fun loadCampaign() {
        val testCampaign = CampaignInfo("observerTestAccessKey", "observerTestCampaignKey")

        every { preference.campaignKey } returns "observerTestCampaignKey"
        every { preference.accessKey } returns "observerTestAccessKey"

        viewModel.loadCampaign()

        assertEquals(viewModel.campaignInfo.getOrAwaitValue(), testCampaign)
    }

    @Test
    fun saveCampaign() {
        val testCampaign = CampaignInfo("testAccessKey2", "campaignKey2")

        viewModel.saveCampaign(testCampaign)

        verify {
            preference.accessKey = "testAccessKey2"
            preference.campaignKey = "campaignKey2"
        }
    }
}