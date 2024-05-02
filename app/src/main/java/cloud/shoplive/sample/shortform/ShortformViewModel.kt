package cloud.shoplive.sample.shortform

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.network.ShopLiveShortformServiceImpl
import cloud.shoplive.sdk.network.request.ShopLiveShortformCollectionRequest
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsData
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsMoreData
import cloud.shoplive.sdk.shorts.ShopLiveShortformMoreSuspendListener
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
    val radiusLiveData: LiveData<Int?>
        get() = _radiusLiveData
    private val _radiusLiveData = MutableLiveData<Int?>()
    val maxCount: Int
        get() = _maxCount
    private var _maxCount: Int = 0

    val submitLiveData = MutableLiveData<Int>()
    val needInitializeTabFlow = MutableStateFlow<Set<Int>>(setOf())
    fun setShortformOption(data: ShortformOptionDialogData) {
        preference.shortformCardType = data.cardTypePosition
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
        _radiusLiveData.value = data.radius
        _maxCount = data.maxCount ?: 0
    }

    fun getSavedCardType(): ShopLiveShortform.CardViewType {
        return when (preference.shortformCardType) {
            0 -> ShopLiveShortform.CardViewType.CARD_TYPE0
            1 -> ShopLiveShortform.CardViewType.CARD_TYPE1
            2 -> ShopLiveShortform.CardViewType.CARD_TYPE2
            else -> ShopLiveShortform.CardViewType.CARD_TYPE0
        }
    }

    fun initializeTab(@ShortFormPage tabIndex: Int) {
        needInitializeTabFlow.update { it + setOf(tabIndex) }
    }

    private var reference: String? = null
    private var hasMore: Boolean = false
    fun playShortformV2TestTask(context: Context) {
        viewModelScope.launch {
            val service = ShopLiveShortformServiceImpl()
            val response =
                kotlin.runCatching { service.collection(ShopLiveCommon.getAccessKey()) }.getOrNull()
                    ?: return@launch
            val list = response.shortsList?.mapNotNull { it.shortsId } ?: emptyList()
            this@ShortformViewModel.reference = response.reference
            this@ShortformViewModel.hasMore = response.hasMore
            ShopLiveShortform.play(context, ShopLiveShortformIdsData().apply {
                ids = list
                currentId = list.getOrNull((Math.random() * list.size).toInt())
            }, ShopLiveShortformMoreSuspendListener {
                val moreResponse =
                    kotlin.runCatching {
                        service.collection(
                            ShopLiveCommon.getAccessKey(),
                            ShopLiveShortformCollectionRequest(reference = this@ShortformViewModel.reference)
                        )
                    }.getOrNull() ?: return@ShopLiveShortformMoreSuspendListener null
                val moreList = moreResponse.shortsList?.mapNotNull { it.shortsId } ?: emptyList()
                this@ShortformViewModel.reference = moreResponse.reference
                this@ShortformViewModel.hasMore = moreResponse.hasMore
                return@ShopLiveShortformMoreSuspendListener ShopLiveShortformIdsMoreData().apply {
                    ids = moreList
                    hasMore = this@ShortformViewModel.hasMore
                }
            })
        }
    }
}