package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sample.extension.showShareDialog
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.common.extension.debugShopLiveLog
import cloud.shoplive.sdk.network.ShopLiveNetwork
import cloud.shoplive.sdk.network.ShopLiveShortformServiceImpl
import cloud.shoplive.sdk.network.request.ShopLiveShortformCollectionRequest
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdData
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsData
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsMoreData
import cloud.shoplive.sdk.shorts.ShopLiveShortformMessageListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformMoreSuspendListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayerEventCommand
import cloud.shoplive.sdk.shorts.ShopLiveShortformPreviewData
import cloud.shoplive.sdk.shorts.ShopLiveShortformProductListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData
import cloud.shoplive.sdk.shorts.ShopLiveShortformUrlListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformVisibleDetailTypeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ShortformViewModel(private val preference: PreferencesUtil) : ViewModel() {
    val shortsCollectionIdLiveData: LiveData<String?>
        get() = _shortsCollectionIdLiveData
    private val _shortsCollectionIdLiveData = MutableLiveData<String?>()
    val playableTypeLiveData: LiveData<ShopLiveShortform.PlayableType>
        get() = _playableTypeLiveData
    private val _playableTypeLiveData = MutableLiveData<ShopLiveShortform.PlayableType>()
    val hashTagLiveData: LiveData<Pair<List<String>?, ShopLiveShortformTagSearchOperator>>
        get() = _hashTagLiveData
    private val _hashTagLiveData =
        MutableLiveData<Pair<List<String>?, ShopLiveShortformTagSearchOperator>>()
    val brandLiveData: LiveData<List<String>?>
        get() = _brandLiveData
    private val _brandLiveData = MutableLiveData<List<String>?>()
    val skusLiveData: LiveData<List<String>?>
        get() = _skusLiveData
    private val _skusLiveData = MutableLiveData<List<String>?>()
    val isVisibleTitleLiveData: LiveData<Boolean>
        get() = _isVisibleTitleLiveData
    private val _isVisibleTitleLiveData = MutableLiveData<Boolean>()
    val isVisibleDescriptionLiveData: LiveData<Boolean>
        get() = _isVisibleDescriptionLiveData
    private val _isVisibleDescriptionLiveData = MutableLiveData<Boolean>()
    val isVisibleBrandLiveData: LiveData<Boolean>
        get() = _isVisibleBrandLiveData
    private val _isVisibleBrandLiveData = MutableLiveData<Boolean>()
    val isVisibleProductCountLiveData: LiveData<Boolean>
        get() = _isVisibleProductCountLiveData
    private val _isVisibleProductCountLiveData = MutableLiveData<Boolean>()
    val isVisibleViewCountLiveData: LiveData<Boolean>
        get() = _isVisibleViewCountLiveData
    private val _isVisibleViewCountLiveData = MutableLiveData<Boolean>()
    val isEnableShuffleLiveData: LiveData<Boolean>
        get() = _isEnableShuffleLiveData
    private val _isEnableShuffleLiveData = MutableLiveData<Boolean>()
    val isEnableSnapLiveData: LiveData<Boolean>
        get() = _isEnableSnapLiveData
    private val _isEnableSnapLiveData = MutableLiveData<Boolean>()
    val isEnablePlayVideosLiveData: LiveData<Boolean>
        get() = _isEnablePlayVideosLiveData
    private val _isEnablePlayVideosLiveData = MutableLiveData<Boolean>()
    val isEnablePlayWifiLiveData: LiveData<Boolean>
        get() = _isEnablePlayWifiLiveData
    private val _isEnablePlayWifiLiveData = MutableLiveData<Boolean>()
    val visibleDetailTypeDataLiveData: LiveData<ShopLiveShortformVisibleDetailTypeData>
        get() = _visibleDetailTypeDataLiveData
    private val _visibleDetailTypeDataLiveData =
        MutableLiveData<ShopLiveShortformVisibleDetailTypeData>()
    val isCenterCrop: LiveData<Boolean>
        get() = _isCenterCrop
    private val _isCenterCrop = MutableLiveData<Boolean>()
    val radiusLiveData: LiveData<Int?>
        get() = _radiusLiveData
    private val _radiusLiveData = MutableLiveData<Int?>()
    val maxCount: Int
        get() = _maxCount
    private var _maxCount: Int = 0
    val isMuted: Boolean
        get() = _isMuted
    private var _isMuted = true
    val isEnabledVolumeKey: Boolean
        get() = _isEnabledVolumeKey
    private var _isEnabledVolumeKey = false
    val isShowEditorEvent: Boolean
        get() = _isShowEditorEvent
    private var _isShowEditorEvent = false
    val isCustomShortform: Boolean
        get() = _isCustomShortform
    private var _isCustomShortform = false

    val submitLiveData = MutableLiveData<Int>()
    val needInitializeTabFlow = MutableStateFlow<Set<Int>>(setOf())

    fun setShortformOption(data: ShortformOptionDialogData) {
        preference.shortFormCardType = data.cardTypePosition
        _playableTypeLiveData.value = when (data.playableTypePosition) {
            0 -> ShopLiveShortform.PlayableType.FIRST
            1 -> ShopLiveShortform.PlayableType.CENTER
            2 -> ShopLiveShortform.PlayableType.ALL
            else -> ShopLiveShortform.PlayableType.FIRST
        }
        _shortsCollectionIdLiveData.value = data.shortsCollectionId
        _hashTagLiveData.value = Pair(
            data.hashTag?.split(",")?.map { it.trim() },
            if (data.hashTagOperator == 0) ShopLiveShortformTagSearchOperator.OR else ShopLiveShortformTagSearchOperator.AND
        )
        _brandLiveData.value = data.brand?.split(",")?.map { it.trim() }
        _skusLiveData.value = data.skus?.split(",")?.map { it.trim() }
        _isVisibleTitleLiveData.value = data.isVisibleTitle
        _isVisibleDescriptionLiveData.value = data.isVisibleDescription
        _isVisibleBrandLiveData.value = data.isVisibleBrand
        _isVisibleProductCountLiveData.value = data.isVisibleProductCount
        _isVisibleViewCountLiveData.value = data.isVisibleViewCount
        _isEnableShuffleLiveData.value = data.isEnableShuffle
        _isEnableSnapLiveData.value = data.isEnableSnap
        _isEnablePlayVideosLiveData.value = data.isEnablePlayVideos
        _isEnablePlayWifiLiveData.value = data.isEnablePlayOnlyWifi
        _visibleDetailTypeDataLiveData.value = ShopLiveShortformVisibleDetailTypeData().apply {
            isBookmarkVisible = data.isBookmarkVisible
            isShareButtonVisible = data.isShareButtonVisible
            isCommentButtonVisible = data.isCommentButtonVisible
            isLikeButtonVisible = data.isLikeButtonVisible
        }
        _isCenterCrop.value = data.isCentCrop
        _radiusLiveData.value = data.radius
        _maxCount = data.maxCount ?: 0
        _isMuted = data.isMuted
        _isEnabledVolumeKey = data.isEnabledVolumeKey
        _isShowEditorEvent = data.isShowEditorEvent
        _isCustomShortform = data.isCustomEditor
    }

    fun getSavedCardType(): ShopLiveShortform.CardViewType {
        return when (preference.shortFormCardType) {
            0 -> ShopLiveShortform.CardViewType.CARD_TYPE0
            1 -> ShopLiveShortform.CardViewType.CARD_TYPE1
            2 -> ShopLiveShortform.CardViewType.CARD_TYPE2
            else -> ShopLiveShortform.CardViewType.CARD_TYPE0
        }
    }

    fun initializeTab(@ShortFormPage tabIndex: Int) {
        needInitializeTabFlow.update { it + setOf(tabIndex) }
    }

    fun setAccessKey(value: String) {
        preference.accessKey = value
    }

    private var reference: String? = null
    private var hasMore: Boolean = false
    private var morePagingReference: String? = null
    private var previousPagingReference: String? = null
    private var morePagingHasMore: Boolean = false
    private var previousPagingHasMore: Boolean = false

  fun playShortformV2TestTask(activity: Activity) {
        viewModelScope.launch {
            val service = ShopLiveShortformServiceImpl()
            val response =
                kotlin.runCatching {
                    service.collection(
                        ShopLiveCommon.getAccessKey(),
                        ShopLiveShortformCollectionRequest(
                            count = ShopLiveNetwork.shortsConfig?.sdk?.detailApiInitializeCount,
                            finite = true,
                        )
                    )
                }.getOrNull()
                    ?: return@launch
            val list = response.shortsList ?: emptyList()
            this@ShortformViewModel.morePagingReference = response.reference
            this@ShortformViewModel.previousPagingReference = response.reference
            this@ShortformViewModel.morePagingHasMore = response.hasMore
            this@ShortformViewModel.previousPagingHasMore = response.hasMore
            ShopLiveShortform.play(activity, ShopLiveShortformIdsData().apply {
                ids = list.mapNotNull {
                    val shortsId = it.shortsId ?: return@mapNotNull null
                    ShopLiveShortformIdData(shortsId).apply {
                        payload = mapOf(
                            Pair("shortsId", shortsId),
                            Pair("title", it.shortsDetail?.title ?: "title"),
                            Pair("description", "shortsId + ${it.shortsId}")
                        )
                    }
                }
                handler = ShortformSampleData.handler
            }, moreSuspendListener = ShopLiveShortformMoreSuspendListener {
                val moreResponse =
                    kotlin.runCatching {
                        service.collection(
                            ShopLiveCommon.getAccessKey(),
                            ShopLiveShortformCollectionRequest(
                                reference = this@ShortformViewModel.morePagingReference,
                                count = ShopLiveNetwork.shortsConfig?.sdk?.detailApiPaginationCount,
                                finite = true,
                            )
                        )
                    }.getOrNull() ?: return@ShopLiveShortformMoreSuspendListener null
                val moreList = moreResponse.shortsList ?: emptyList()
                this@ShortformViewModel.morePagingReference = moreResponse.reference
                this@ShortformViewModel.morePagingHasMore = moreResponse.hasMore
                return@ShopLiveShortformMoreSuspendListener ShopLiveShortformIdsMoreData().apply {
                    ids = moreList.mapNotNull {
                        val shortsId = it.shortsId ?: return@mapNotNull null
                        ShopLiveShortformIdData(shortsId).apply {
                            payload = mapOf(
                                Pair("title", it.shortsDetail?.title ?: "title"),
                                Pair("description", "shortsId + ${it.shortsId}")
                            )
                        }
                    }
                    hasMore = this@ShortformViewModel.morePagingHasMore
                }
            }, previousSuspendListener = ShopLiveShortformMoreSuspendListener {
                val previous =
                    kotlin.runCatching {
                        service.collection(
                            ShopLiveCommon.getAccessKey(),
                            ShopLiveShortformCollectionRequest(
                                reference = this@ShortformViewModel.previousPagingReference,
                                count = ShopLiveNetwork.shortsConfig?.sdk?.detailApiPaginationCount,
                                finite = true,
                            )
                        )
                    }.getOrNull() ?: return@ShopLiveShortformMoreSuspendListener null
                val moreList = previous.shortsList ?: emptyList()
                this@ShortformViewModel.previousPagingReference = previous.reference
                this@ShortformViewModel.previousPagingHasMore = previous.hasMore
                return@ShopLiveShortformMoreSuspendListener ShopLiveShortformIdsMoreData().apply {
                    ids = moreList.mapNotNull {
                        val shortsId = it.shortsId ?: return@mapNotNull null
                        ShopLiveShortformIdData(shortsId).apply {
                            payload = mapOf(
                                Pair("title", it.shortsDetail?.title ?: "title"),
                                Pair("description", "shortsId + ${it.shortsId}")
                            )
                        }
                    }.reversed()
                    hasMore = this@ShortformViewModel.previousPagingHasMore
                }
            })
        }
    }
}