package cloud.shoplive.sample.shortform


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.DialogShortformOptionBinding
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorResolution
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorVideoQuality

data class ShortformOptionDialogData(
    val accessKey: String,
    val userId: String?,
    val userName: String?,
    val age: Int?,
    val gender: String?,
    val userScore: Int?,
    val cardTypePosition: Int,
    val playableTypePosition: Int,
    val shortsCollectionId: String?,
    val hashTag: String?,
    val hashTagOperator: Int,
    val brand: String?,
    val skus: String?,
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
    val isCentCrop: Boolean,
    val radius: Int?,
    val maxCount: Int?,
    val isMuted: Boolean,
    val isEnabledVolumeKey: Boolean,
    val cropWidth: Int,
    val cropHeight: Int,
    val cropFixed: Boolean,
    val minTrimDuration: Long,
    val maxTrimDuration: Long,
    val isUsedPlaybackSpeedButton: Boolean,
    val isUsedVolumeButton: Boolean,
    val isUsedCropButton: Boolean,
    val isUsedFilterButton: Boolean,
    val outputVideoQuality: ShopLiveVideoEditorVideoQuality,
    val outputVideoResolution: ShopLiveVideoEditorResolution,
    val isShowEditorEvent: Boolean,
    val isCustomEditor: Boolean
)

class ShortformOptionDialog(
    context: Context,
    private val listener: Listener
) : Dialog(context) {
    fun interface Listener {
        fun invoke(data: ShortformOptionDialogData)
    }

    companion object {
        internal var userId: String? = null
        internal var name: String? = null
        internal var age: Int? = null
        internal var gender: String? = null
        internal var userScore: Int? = null

        internal var cardTypePosition = 0
        internal var playableTypePosition = 0

        internal var shortsCollectionId: String? = null
        internal var hashTag: String? = null
        internal var hashTagOperator = 0
        internal var brands: String? = null
        internal var skus: String? = null

        internal var isVisibleTitle = true
        internal var isVisibleDescription = true
        internal var isVisibleBrand = true
        internal var isVisibleProductCount = true
        internal var isVisibleViewCount = true
        internal var isEnableShuffle = false
        internal var isEnableSnap = false
        internal var isEnablePlayVideos = true
        internal var isEnablePlayOnlyWifi = false
        internal var isBookmarkVisible = true
        internal var isShareButtonVisible = true
        internal var isCommentButtonVisible = true
        internal var isLikeButtonVisible = true
        internal var isCenterCrop = true
        internal var radius: Int? = null
        internal var maxCount: Int = 0
        internal var isMuted = true
        internal var isEnabledVolumeKey = false

        internal var cropWidth: Int = 9
        internal var cropHeight: Int = 16
        internal var cropFixed: Boolean = false
        internal var minTrimDuration: Long = 1 * 1000
        internal var maxTrimDuration: Long = 60 * 1000
        internal var isUsedPlaybackSpeedButton: Boolean = true
        internal var isUsedVolumeButton: Boolean = true
        internal var isUsedCropButton: Boolean = true
        internal var isUsedFilterButton: Boolean = true
        internal var outputVideoQuality = ShopLiveVideoEditorVideoQuality.NORMAL
        internal var outputVideoResolution = ShopLiveVideoEditorResolution.RESOLUTION_720
        internal var isShowEditorEvent: Boolean = false
        internal var isCustomEditor: Boolean = false
    }

    private val binding: DialogShortformOptionBinding by lazy {
        DialogShortformOptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        binding.accessKeyEdit.setText(ShopLiveCommon.getAccessKey())
        binding.userLoggedIn.text = if (ShopLiveCommon.isLoggedIn()) {
            context.getString(R.string.shortform_option_user_logged_in)
        } else {
            context.getString(R.string.shortform_option_user_guest)
        }
        binding.userIdEdit.setText(userId)
        binding.userNameEdit.setText(name)
        binding.userAgeEdit.setText(age?.toString())
        binding.userGenderEdit.setText(gender)
        binding.userScoreEdit.setText(userScore?.toString())
        binding.shortsCollectionIdEdit.setText(shortsCollectionId?.toString() ?: "")
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
        binding.brandEdit.setText(brands)
        binding.skusEdit.setText(skus)

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
        binding.centerCropButtonCheckBox.isChecked = isCenterCrop
        binding.radiusEdit.setText(radius?.toString())
        binding.previewMaxCountEdit.setText(maxCount?.toString())
        binding.previewMuted.isChecked = isMuted
        binding.previewEnabledVolumeKey.isChecked = isEnabledVolumeKey

        binding.cropWidthEdit.setText(cropWidth.toString())
        binding.cropHeightEdit.setText(cropHeight.toString())
        binding.cropFixed.isChecked = cropFixed
        binding.minDurationEdit.setText((minTrimDuration / 1000).toString())
        binding.maxDurationEdit.setText((maxTrimDuration / 1000).toString())
        binding.isPlaybackSpeedButton.isChecked = isUsedPlaybackSpeedButton
        binding.isVolumeButton.isChecked = isUsedVolumeButton
        binding.isCropButton.isChecked = isUsedCropButton
        binding.isFilterButton.isChecked = isUsedFilterButton
        when (outputVideoQuality) {
            ShopLiveVideoEditorVideoQuality.NORMAL ->
                binding.outputVideoQualityNormal.isChecked = true

            ShopLiveVideoEditorVideoQuality.HIGH ->
                binding.outputVideoQualityHigh.isChecked = true

            ShopLiveVideoEditorVideoQuality.MAX ->
                binding.outputVideoQualityMax.isChecked = true
        }

        when (outputVideoResolution) {
            ShopLiveVideoEditorResolution.RESOLUTION_720 ->
                binding.outputVideoResolution720p.isChecked = true

            ShopLiveVideoEditorResolution.RESOLUTION_960 ->
                binding.outputVideoResolution960p.isChecked = true

            ShopLiveVideoEditorResolution.RESOLUTION_1080 ->
                binding.outputVideoResolution1080p.isChecked = true

            else -> binding.outputVideoResolution720p.isChecked = true
        }
        binding.isEditorEventShowButton.isChecked = isShowEditorEvent
        binding.isCustomEditorButton.isChecked = isCustomEditor

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

            shortsCollectionId =
                binding.shortsCollectionIdEdit.text?.toString()
            hashTag = binding.hashTagEdit.text?.toString()
            hashTagOperator = if (binding.hashTagOptionOr.isChecked) {
                0
            } else if (binding.hashTagOptionAnd.isChecked) {
                1
            } else {
                0
            }
            brands = binding.brandEdit.text?.toString()
            skus = binding.skusEdit.text?.toString()

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
            isCenterCrop = binding.centerCropButtonCheckBox.isChecked

            radius = binding.radiusEdit.text?.toString()?.toIntOrNull()
            maxCount = binding.previewMaxCountEdit.text?.toString()?.toIntOrNull() ?: 0
            isEnabledVolumeKey = binding.previewEnabledVolumeKey.isChecked
            isMuted = binding.previewMuted.isChecked


            cropWidth = binding.cropWidthEdit.text?.toString()?.toIntOrNull() ?: 0
            cropHeight = binding.cropHeightEdit.text?.toString()?.toIntOrNull() ?: 0
            cropFixed = binding.cropFixed.isChecked
            minTrimDuration = (binding.minDurationEdit.text?.toString()?.toLongOrNull() ?: 0) * 1000
            maxTrimDuration = (binding.maxDurationEdit.text?.toString()?.toLongOrNull() ?: 0) * 1000
            isUsedPlaybackSpeedButton = binding.isPlaybackSpeedButton.isChecked
            isUsedVolumeButton = binding.isVolumeButton.isChecked
            isUsedCropButton = binding.isCropButton.isChecked
            isUsedFilterButton = binding.isFilterButton.isChecked
            outputVideoQuality = if (binding.outputVideoQualityNormal.isChecked) {
                ShopLiveVideoEditorVideoQuality.NORMAL
            } else if (binding.outputVideoQualityHigh.isChecked) {
                ShopLiveVideoEditorVideoQuality.HIGH
            } else if (binding.outputVideoQualityMax.isChecked) {
                ShopLiveVideoEditorVideoQuality.MAX
            } else {
                ShopLiveVideoEditorVideoQuality.NORMAL
            }
            outputVideoResolution = if (binding.outputVideoResolution720p.isChecked) {
                ShopLiveVideoEditorResolution.RESOLUTION_720
            } else if (binding.outputVideoResolution960p.isChecked) {
                ShopLiveVideoEditorResolution.RESOLUTION_960
            } else if (binding.outputVideoResolution1080p.isChecked) {
                ShopLiveVideoEditorResolution.RESOLUTION_1080
            } else {
                ShopLiveVideoEditorResolution.RESOLUTION_720
            }
            isShowEditorEvent = binding.isEditorEventShowButton.isChecked
            isCustomEditor = binding.isCustomEditorButton.isChecked

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
                    shortsCollectionId,
                    hashTag,
                    hashTagOperator,
                    brands,
                    skus,
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
                    isCenterCrop,
                    radius,
                    maxCount,
                    isMuted,
                    isEnabledVolumeKey,
                    cropWidth,
                    cropHeight,
                    cropFixed,
                    minTrimDuration,
                    maxTrimDuration,
                    isUsedPlaybackSpeedButton,
                    isUsedVolumeButton,
                    isUsedCropButton,
                    isUsedFilterButton,
                    outputVideoQuality,
                    outputVideoResolution,
                    isShowEditorEvent,
                    isCustomEditor
                )
            )
        }
    }
}