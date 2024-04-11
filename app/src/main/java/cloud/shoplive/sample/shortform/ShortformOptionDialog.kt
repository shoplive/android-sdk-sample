package cloud.shoplive.sample.shortform

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import cloud.shoplive.sample.databinding.DialogShortformOptionBinding
import cloud.shoplive.sdk.common.ShopLiveCommon

data class ShortformOptionDialogData(
    val accessKey: String,
    val userId: String?,
    val userName: String?,
    val age: Int?,
    val gender: String?,
    val userScore: Int?,
    val cardTypePosition: Int,
    val playableTypePosition: Int,
    val hashTag: String?,
    val hashTagOperator: Int,
    val brand: String?,
    val isVisibleTitle: Boolean,
    val isVisibleDescription: Boolean,
    val isVisibleBrand: Boolean,
    val isVisibleProductCount: Boolean,
    val isVisibleViewCount: Boolean,
    val isEnableShuffle: Boolean,
    val isEnableSnap: Boolean,
    val isEnablePlayVideos: Boolean,
    val isEnablePlayOnlyWifi: Boolean,
    val isBookmarkVisible: Boolean,
    val isShareButtonVisible: Boolean,
    val isCommentButtonVisible: Boolean,
    val isLikeButtonVisible: Boolean,
    val radius: Int?,
)

class ShortformOptionDialog(
    context: Context,
    private val listener: Listener
) : Dialog(context) {
    fun interface Listener {
        fun invoke(data: ShortformOptionDialogData)
    }

    companion object {
        private var userId: String? = null
        private var name: String? = null
        private var age: Int? = null
        private var gender: String? = null
        private var userScore: Int? = null

        private var cardTypePosition = 0
        private var playableTypePosition = 0

        private var hashTag: String? = null
        private var hashTagOperator = 0
        private var brand: String? = null

        private var isVisibleTitle = true
        private var isVisibleDescription = true
        private var isVisibleBrand = true
        private var isVisibleProductCount = true
        private var isVisibleViewCount = true
        private var isEnableShuffle = false
        private var isEnableSnap = false
        private var isEnablePlayVideos = true
        private var isEnablePlayOnlyWifi = false
        private var isBookmarkVisible = true
        private var isShareButtonVisible = true
        private var isCommentButtonVisible = true
        private var isLikeButtonVisible = true
        private var radius: Int? = null
    }

    private val binding: DialogShortformOptionBinding by lazy {
        DialogShortformOptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        binding.accessKeyEdit.setText(ShopLiveCommon.getAccessKey())
        binding.userIdEdit.setText(userId)
        binding.userNameEdit.setText(name)
        binding.userAgeEdit.setText(age?.toString())
        binding.userGenderEdit.setText(gender)
        binding.userScoreEdit.setText(userScore?.toString())
        when (cardTypePosition) {
            0 -> binding.cardType0.isChecked = true
            1 -> binding.cardType1.isChecked = true
            2 -> binding.cardType2.isChecked = true
        }
        when (playableTypePosition) {
            0 -> binding.playableTypeFirst.isChecked = true
            1 -> binding.playableTypeCenter.isChecked = true
            2 -> binding.playableTypeAll.isChecked = true
        }
        binding.hashTagEdit.setText(hashTag)
        when (hashTagOperator) {
            0 -> binding.hashTagOptionOr.isChecked = true
            1 -> binding.hashTagOptionAnd.isChecked = true
        }
        binding.brandEdit.setText(brand)

        binding.visibleTitleCheckBox.isChecked = isVisibleTitle
        binding.visibleDescriptionCheckBox.isChecked = isVisibleDescription
        binding.visibleBrandCountCheckBox.isChecked = isVisibleBrand
        binding.visibleProductCountCheckBox.isChecked = isVisibleProductCount
        binding.visibleViewCountCheckBox.isChecked = isVisibleViewCount
        binding.enableShuffleCheckBox.isChecked = isEnableShuffle
        binding.enableSnapCheckBox.isChecked = isEnableSnap
        binding.enablePlayVideosCheckBox.isChecked = isEnablePlayVideos
        binding.enablePlayOnlyWifiCheckBox.isChecked = isEnablePlayOnlyWifi
        binding.visibleBookmarkButtonCheckBox.isChecked = isBookmarkVisible
        binding.visibleShareButtonCheckBox.isChecked = isShareButtonVisible
        binding.visibleCommentButtonCheckBox.isChecked = isCommentButtonVisible
        binding.visibleLikeButtonCheckBox.isChecked = isLikeButtonVisible
        binding.radiusEdit.setText(radius?.toString())

        binding.confirmButton.setOnClickListener {
            val accessKey = binding.accessKeyEdit.text?.toString()
            if (accessKey.isNullOrEmpty()) {
                binding.accessKeyEdit.requestFocus()
                return@setOnClickListener
            }
            userId = binding.userIdEdit.text?.toString()?.let { text ->
                return@let text.ifBlank { null }
            }
            name = binding.userNameEdit.text?.toString()?.let { text ->
                return@let text.ifBlank { null }
            }
            age = binding.userAgeEdit.text?.toString()?.let { text ->
                return@let text.ifBlank { null }
            }?.toIntOrNull()
            gender = binding.userGenderEdit.text?.toString()?.let { text ->
                return@let text.ifBlank { null }
            }
            userScore = binding.userScoreEdit.text?.toString()?.let { text ->
                return@let text.ifBlank { null }
            }?.toIntOrNull()

            cardTypePosition = if (binding.cardType0.isChecked) {
                0
            } else if (binding.cardType1.isChecked) {
                1
            } else if (binding.cardType2.isChecked) {
                2
            } else {
                0
            }

            playableTypePosition = if (binding.playableTypeFirst.isChecked) {
                0
            } else if (binding.playableTypeCenter.isChecked) {
                1
            } else if (binding.playableTypeAll.isChecked) {
                2
            } else {
                0
            }

            hashTag = binding.hashTagEdit.text?.toString()
            hashTagOperator = if (binding.hashTagOptionOr.isChecked) {
                0
            } else if (binding.hashTagOptionAnd.isChecked) {
                1
            } else {
                0
            }
            brand = binding.brandEdit.text?.toString()

            isVisibleTitle = binding.visibleTitleCheckBox.isChecked
            isVisibleDescription = binding.visibleDescriptionCheckBox.isChecked
            isVisibleBrand = binding.visibleBrandCountCheckBox.isChecked
            isVisibleProductCount = binding.visibleProductCountCheckBox.isChecked
            isVisibleViewCount = binding.visibleViewCountCheckBox.isChecked
            isEnableShuffle = binding.enableShuffleCheckBox.isChecked
            isEnableSnap = binding.enableSnapCheckBox.isChecked
            isEnablePlayVideos = binding.enablePlayVideosCheckBox.isChecked
            isEnablePlayOnlyWifi = binding.enablePlayOnlyWifiCheckBox.isChecked
            isBookmarkVisible = binding.visibleBookmarkButtonCheckBox.isChecked
            isShareButtonVisible = binding.visibleShareButtonCheckBox.isChecked
            isCommentButtonVisible = binding.visibleCommentButtonCheckBox.isChecked
            isLikeButtonVisible = binding.visibleLikeButtonCheckBox.isChecked

            radius = binding.radiusEdit.text?.toString()?.toIntOrNull()

            listener.invoke(
                ShortformOptionDialogData(
                    accessKey,
                    userId,
                    name,
                    age,
                    gender,
                    userScore,
                    cardTypePosition,
                    playableTypePosition,
                    hashTag,
                    hashTagOperator,
                    brand,
                    isVisibleTitle,
                    isVisibleDescription,
                    isVisibleBrand,
                    isVisibleProductCount,
                    isVisibleViewCount,
                    isEnableShuffle,
                    isEnableSnap,
                    isEnablePlayVideos,
                    isEnablePlayOnlyWifi,
                    isBookmarkVisible,
                    isShareButtonVisible,
                    isCommentButtonVisible,
                    isLikeButtonVisible,
                    radius,
                )
            )
        }
    }
}