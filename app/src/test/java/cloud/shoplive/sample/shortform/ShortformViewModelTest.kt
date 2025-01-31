package cloud.shoplive.sample.shortform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.PreferencesUtilImpl
import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sample.getOrAwaitValue
import cloud.shoplive.sdk.ShopLiveUserGender
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorResolution
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorVideoQuality
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformVisibleDetailTypeData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShortformViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val mockStorage = mockk<KeyValueStorage>(relaxed = true)

    private var preference = mockk<PreferencesUtil>(relaxed = true)
    private lateinit var viewModel: ShortformViewModel

    @Before
    fun setUp() {
        preference = PreferencesUtilImpl(mockStorage)
        viewModel = ShortformViewModel(preference)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializeTab() = runTest {
        viewModel.initializeTab(1)

        advanceUntilIdle()

        assertEquals(viewModel.needInitializeTabFlow.first(), setOf(1))
    }

    @Test
    fun setShortformOption() {
        val testDialogData = ShortformOptionDialogData(
            accessKey = "acc",
            cardTypePosition = 0,
            playableTypePosition = 0,
            hashTagOperator = 0,
            isVisibleTitle = false,
            isVisibleDescription = false,
            isVisibleBrand = false,
            isVisibleProductCount = false,
            isVisibleViewCount = false,
            isEnableShuffle = false,
            isEnableSnap = false,
            isEnablePlayVideos = false,
            isEnablePlayOnlyWifi = false,
            isBookmarkVisible = false,
            isShareButtonVisible = false,
            isCommentButtonVisible = false,
            isLikeButtonVisible = false,
            isCentCrop = false,
            isMuted = false,
            isEnabledVolumeKey = false,
            cropWidth = 100,
            cropHeight = 200,
            cropFixed = false,
            minTrimDuration = 0L,
            maxTrimDuration = 0L,
            isUsedPlaybackSpeedButton = false,
            isUsedVolumeButton = false,
            isUsedCropButton = false,
            isUsedFilterButton = false,
            outputVideoQuality = ShopLiveVideoEditorVideoQuality.NORMAL,
            outputVideoResolution = ShopLiveVideoEditorResolution.RESOLUTION_1080,
            isShowEditorEvent = false,
            isCustomEditor = false,
            userId = "userId",
            userName = "name",
            age = 10,
            gender = ShopLiveUserGender.Female.name,
            userScore = 100,
            shortsCollectionId = "collectionId",
            hashTag = "hashTag",
            brand = "test",
            skus = "skus",
            radius = 20,
            maxCount = 10
        )

        viewModel.setShortformOption(
            testDialogData
        )

        verify {
            preference.shortFormCardType = testDialogData.cardTypePosition
        }

        assertEquals(viewModel.playableTypeLiveData.getOrAwaitValue(), ShopLiveShortform.PlayableType.FIRST)
        assertEquals(viewModel.shortsCollectionIdLiveData.getOrAwaitValue(), testDialogData.shortsCollectionId)
        assertEquals(viewModel.hashTagLiveData.getOrAwaitValue(), Pair(listOf(testDialogData.hashTag), ShopLiveShortformTagSearchOperator.OR))
        assertEquals(viewModel.brandLiveData.getOrAwaitValue(), listOf(testDialogData.brand))
        assertEquals(viewModel.skusLiveData.getOrAwaitValue(), listOf(testDialogData.skus))
        assertEquals(viewModel.isVisibleTitleLiveData.getOrAwaitValue(), testDialogData.isVisibleTitle)
        assertEquals(viewModel.isVisibleDescriptionLiveData.getOrAwaitValue(), testDialogData.isVisibleDescription)
        assertEquals(viewModel.isVisibleBrandLiveData.getOrAwaitValue(), testDialogData.isVisibleBrand)
        assertEquals(viewModel.isVisibleProductCountLiveData.getOrAwaitValue(), testDialogData.isVisibleProductCount)
        assertEquals(viewModel.isVisibleViewCountLiveData.getOrAwaitValue(), testDialogData.isVisibleViewCount)
        assertEquals(viewModel.isEnableShuffleLiveData.getOrAwaitValue(), testDialogData.isEnableShuffle)
        assertEquals(viewModel.isEnableSnapLiveData.getOrAwaitValue(), testDialogData.isEnableSnap)
        assertEquals(viewModel.isEnablePlayVideosLiveData.getOrAwaitValue(), testDialogData.isEnablePlayVideos)
        assertEquals(viewModel.isEnablePlayWifiLiveData.getOrAwaitValue(), testDialogData.isEnablePlayOnlyWifi)
        assertEquals(viewModel.visibleDetailTypeDataLiveData.getOrAwaitValue(), ShopLiveShortformVisibleDetailTypeData().apply {
            isBookmarkVisible = testDialogData.isBookmarkVisible
            isShareButtonVisible = testDialogData.isShareButtonVisible
            isCommentButtonVisible = testDialogData.isCommentButtonVisible
            isLikeButtonVisible = testDialogData.isLikeButtonVisible
        })
        assertEquals(viewModel.isCenterCrop.getOrAwaitValue(), testDialogData.isCentCrop)
        assertEquals(viewModel.radiusLiveData.getOrAwaitValue(), testDialogData.radius)
        assertEquals(viewModel.maxCount, testDialogData.maxCount)
        assertEquals(viewModel.isMuted, testDialogData.isMuted)
        assertEquals(viewModel.isEnabledVolumeKey, testDialogData.isEnabledVolumeKey)
        assertEquals(viewModel.isShowEditorEvent, testDialogData.isShowEditorEvent)
        assertEquals(viewModel.isCustomShortform, testDialogData.isCustomEditor)
    }

    @Test
    fun setAccessKey() {
        viewModel.setAccessKey("test")
        verify { preference.accessKey = "test" }
    }

    @Test
    fun getSavedCardType() {
        every { preference.shortFormCardType } returns 0
        assertEquals(viewModel.getSavedCardType(), ShopLiveShortform.CardViewType.CARD_TYPE0)
    }
}