package cloud.shoplive.sample.shortform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cloud.shoplive.sample.PreferencesUtil
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.shorts.ShopLiveShortform

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
    val radiusLiveData = MutableLiveData<Int?>()
    val distanceLiveData = MutableLiveData<Int?>()
    val submitLiveData = MutableLiveData<Boolean>()

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
        radiusLiveData.value = data.radius
        distanceLiveData.value = data.distance
    }

    fun getSavedCardType(): ShopLiveShortform.CardViewType {
        return when (preference.shortformCardType) {
            0 -> ShopLiveShortform.CardViewType.CARD_TYPE0
            1 -> ShopLiveShortform.CardViewType.CARD_TYPE1
            2 -> ShopLiveShortform.CardViewType.CARD_TYPE2
            else -> ShopLiveShortform.CardViewType.CARD_TYPE0
        }
    }
}