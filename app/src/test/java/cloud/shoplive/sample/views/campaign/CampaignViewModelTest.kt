package cloud.shoplive.sample.views.campaign

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cloud.shoplive.sample.CampaignInfo
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.data.KeyValueStorage
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
    private val campaignInfoObserver: Observer<CampaignInfo> = mockk(relaxed = true)

    private val _campaignInfo: MutableLiveData<CampaignInfo> = MutableLiveData()
    private val campaignInfo: LiveData<CampaignInfo>
        get() = _campaignInfo

    @Before
    fun setUp() {
        preference = PreferencesUtilImpl(mockStorage)
        viewModel = CampaignViewModel(preference)

        viewModel.campaignInfo.observeForever(campaignInfoObserver)
    }

    @Test
    fun getCampaignInfo() {
        val testCampaign = CampaignInfo("observerTestAccessKey", "observerTestCampaignKey")

        campaignInfo.observeForTesting(campaignInfoObserver) {
            _campaignInfo.value = testCampaign

            verify { campaignInfoObserver.onChanged(testCampaign) }
        }
    }

    @Test
    fun loadCampaign() {
        every { preference.accessKey } returns "testAccessKey"
        every { preference.campaignKey } returns "campaignKey"

        viewModel.loadCampaign()

        verify { campaignInfoObserver.onChanged(CampaignInfo("testAccessKey", "campaignKey")) }
        assertEquals("testAccessKey", viewModel.campaignInfo.value?.accessKey)
        assertEquals("campaignKey", viewModel.campaignInfo.value?.campaignKey)
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

fun <T> LiveData<T>.observeForTesting(observer: Observer<T>, block: () -> Unit) {
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}