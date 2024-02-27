package cloud.shoplive.sample.shortform

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.network.ShopLiveShortformServiceImpl
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsData
import cloud.shoplive.sdk.shorts.ShopLiveShortformIdsMoreData
import cloud.shoplive.sdk.shorts.ShopLiveShortformMoreSuspendListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformVisibleDetailTypeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShortformViewModel(private val preference: PreferencesUtil) : ViewModel() {
    val playableTypeLiveData = MutableLiveData<ShopLiveShortform.PlayableType>()
    val hashTagLiveData = MutableLiveData<Pair<List<String>?, ShopLiveShortformTagSearchOperator>>()
    val brandLiveData = MutableLiveData<List<String>?>()
    val isVisibleTitleLiveData = MutableLiveData<Boolean>()
    val isVisibleDescriptionLiveData = MutableLiveData<Boolean>()
    val isVisibleBrandLiveData = MutableLiveData<Boolean>()
    val isVisibleProductCountLiveData = MutableLiveData<Boolean>()
    val isVisibleViewCountLiveData = MutableLiveData<Boolean>()
    val isEnableShuffleLiveData = MutableLiveData<Boolean>()
    val isEnableSnapLiveData = MutableLiveData<Boolean>()
    val isEnablePlayVideosLiveData = MutableLiveData<Boolean>()
    val isEnablePlayWifiLiveData = MutableLiveData<Boolean>()
    val visibleDetailTypeDataLiveData = MutableLiveData<ShopLiveShortformVisibleDetailTypeData>()
    val radiusLiveData = MutableLiveData<Int?>()
    val submitLiveData = MutableLiveData<Int>()
    val needInitializeTabFlow = MutableStateFlow<Set<Int>>(setOf())

    fun setShortformOption(data: ShortformOptionDialogData) {
        preference.shortformCardType = data.cardTypePosition
        playableTypeLiveData.value = when (data.playableTypePosition) {
            0 -> ShopLiveShortform.PlayableType.FIRST
            1 -> ShopLiveShortform.PlayableType.CENTER
            2 -> ShopLiveShortform.PlayableType.ALL
            else -> ShopLiveShortform.PlayableType.FIRST
        }
        hashTagLiveData.value = Pair(
            data.hashTag?.split(",")?.map { it.trim() },
            if (data.hashTagOperator == 0) ShopLiveShortformTagSearchOperator.OR else ShopLiveShortformTagSearchOperator.AND
        )
        brandLiveData.value = data.brand?.split(",")?.map { it.trim() }
        isVisibleTitleLiveData.value = data.isVisibleTitle
        isVisibleDescriptionLiveData.value = data.isVisibleDescription
        isVisibleBrandLiveData.value = data.isVisibleBrand
        isVisibleProductCountLiveData.value = data.isVisibleProductCount
        isVisibleViewCountLiveData.value = data.isVisibleViewCount
        isEnableShuffleLiveData.value = data.isEnableShuffle
        isEnableSnapLiveData.value = data.isEnableSnap
        isEnablePlayVideosLiveData.value = data.isEnablePlayVideos
        isEnablePlayWifiLiveData.value = data.isEnablePlayOnlyWifi
        visibleDetailTypeDataLiveData.value = ShopLiveShortformVisibleDetailTypeData().apply {
            isBookmarkVisible = data.isBookmarkVisible
            isShareButtonVisible = data.isShareButtonVisible
            isCommentButtonVisible = data.isCommentButtonVisible
            isLikeButtonVisible = data.isLikeButtonVisible
        }
        radiusLiveData.value = data.radius
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
                currentId = list.getOrNull(2)
            }, ShopLiveShortformMoreSuspendListener {
                val moreResponse =
                    kotlin.runCatching {
                        service.collection(
                            ShopLiveCommon.getAccessKey(),
                            reference = this@ShortformViewModel.reference
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