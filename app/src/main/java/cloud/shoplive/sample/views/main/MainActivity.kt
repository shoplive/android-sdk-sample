package cloud.shoplive.sample.views.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import cloud.shoplive.sample.CampaignSettings
import cloud.shoplive.sample.Options
import cloud.shoplive.sample.R
import cloud.shoplive.sample.WebViewActivity
import cloud.shoplive.sample.WebViewDialogFragment
import cloud.shoplive.sample.databinding.ActivityMainBinding
import cloud.shoplive.sample.shortform.HybridShortformActivity
import cloud.shoplive.sample.shortform.NativeShortformActivity
import cloud.shoplive.sample.views.campaign.CampaignActivity
import cloud.shoplive.sample.views.dialog.CustomActionDialog
import cloud.shoplive.sample.views.dialog.CustomListDialog
import cloud.shoplive.sample.views.dialog.CustomShareDialog
import cloud.shoplive.sample.views.login.LoginActivity
import cloud.shoplive.sample.views.settings.SettingsActivity
import cloud.shoplive.sample.views.user.UserActivity
import cloud.shoplive.sdk.OnAudioFocusListener
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveHandler
import cloud.shoplive.sdk.ShopLiveHandlerCallback
import cloud.shoplive.sdk.ShopLiveLog
import cloud.shoplive.sdk.ShopLivePlayerData
import cloud.shoplive.sdk.ShopLivePlayerShareData
import cloud.shoplive.sdk.ShopLivePreviewData
import cloud.shoplive.sdk.ShopLiveUserGender
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.common.ShopLiveCommonUser
import cloud.shoplive.sdk.common.ShopLiveCommonUserGender
import cloud.shoplive.sdk.editor.ShopLiveCoverPicker
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerData
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerHandler
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerUrlData
import cloud.shoplive.sdk.editor.ShopLiveCoverPickerVisibleActionButton
import cloud.shoplive.sdk.editor.ShopLiveEditorLocalData
import cloud.shoplive.sdk.editor.ShopLiveEditorResultData
import cloud.shoplive.sdk.editor.ShopLiveImageEditor
import cloud.shoplive.sdk.editor.ShopLiveImageEditorData
import cloud.shoplive.sdk.editor.ShopLiveImageEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveShortformEditor
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorAspectRatio
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorVisibleActionButton
import cloud.shoplive.sdk.editor.ShopLiveShortformEditorVisibleContentData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditor
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorData
import cloud.shoplive.sdk.editor.ShopLiveVideoEditorHandler
import cloud.shoplive.sdk.editor.ShopLiveVideoUploaderData
import cloud.shoplive.sdk.network.ShopLiveConversionData
import cloud.shoplive.sdk.network.ShopLiveConversionProductData
import cloud.shoplive.sdk.network.ShopLiveEvent
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        private const val ACCESS_KEY = "accessKey"
        private const val CAMPAIGN_KEY = "campaignKey"

        private const val KEY_PICK_VISUAL_VIDEO_REQUEST = "key_pick_visual_video_request"
        private const val KEY_PICK_VISUAL_IMAGE_REQUEST = "key_pick_visual_image_request"

        fun buildIntentFromDeeplink(
            context: Context,
            accessKey: String?,
            campaignKey: String?
        ): Intent {
            return Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ACCESS_KEY, accessKey)
                putExtra(CAMPAIGN_KEY, campaignKey)
            }
        }
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    private val editorDialog: CustomListDialog<String> by lazy {
        val uploadEditorString = getString(R.string.shortform_editor_upload)
        val videoEditorString = getString(R.string.shortform_video_editor_only)
        val imageEditorString = getString(R.string.shortform_image_editor_only)
        val coverPickerString = getString(R.string.shortform_cover_picker_only)

        val list =
            listOf(uploadEditorString, videoEditorString, imageEditorString, coverPickerString)
        CustomListDialog(this, list, callback = {
            when (it) {
                uploadEditorString -> showShortformEditor()
                videoEditorString -> showVideoEditor()
                imageEditorString -> showImageEditor()
                coverPickerString -> showCoverPicker()
            }
            editorDialog.dismiss()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_main)

        Options.init(this)

        viewModel.deeplinkInfo.observe(this) {
            CampaignSettings.setAccessKey(this, it.accessKey ?: return@observe)
            CampaignSettings.setCampaignKey(this, it.campaignKey ?: return@observe)
            binding.tvCampaign.text = getString(R.string.label_ak_ck, it.accessKey, it.campaignKey)
            setOptions()
            play()
        }

        viewModel.campaignInfo.observe(this) {
            binding.tvCampaign.text = getString(
                R.string.label_ak_ck,
                it.accessKey ?: getString(R.string.label_none),
                it.campaignKey ?: getString(R.string.label_none)
            )
        }

        viewModel.shopliveUser.observe(this) { user ->
            val userText = when (CampaignSettings.authType(this)) {
                CampaignSettings.UserType.USER.ordinal -> {
                    getString(R.string.label_use_user2) + "\n" + if (user == null) {
                        getString(R.string.label_no_user)
                    } else {
                        val gender = when (user.gender) {
                            ShopLiveUserGender.Male -> getString(R.string.label_gender_male)
                            ShopLiveUserGender.Female -> getString(R.string.label_gender_female)
                            else -> getString(R.string.label_gender_none)
                        }

                        "• User ID : ${user.userId}\n" +
                                "• User Name : ${user.userName}\n" +
                                "• Age : ${user.age}\n" +
                                "• User Score : ${user.userScore}\n" +
                                "• Gender : $gender"
                    }
                }

                CampaignSettings.UserType.JWT.ordinal -> {
                    getString(R.string.label_use_token2) + "\n" + (CampaignSettings.jwt(this)
                        ?: getString(R.string.label_no_token))
                }

                else -> {
                    getString(R.string.label_use_guest2)
                }
            }
            binding.tvUser.text = userText
        }

        viewModel.sdkVersion.observe(this) { version ->
            binding.tvSdkVersion.text = getString(R.string.label_sdk_version, version)
        }

        binding.btCampaign.setOnClickListener {
            startActivity(CampaignActivity.buildIntent(this))
        }

        binding.btUser.setOnClickListener {
            startActivity(UserActivity.buildIntent(this))
        }

        binding.btOption.setOnClickListener {
            startActivity(SettingsActivity.buildIntent(this))
        }

        binding.btPlay.setOnClickListener {
            if (!ShopLive.isSuccessCampaignJoin()) {
                // first play
                setOptions()
                play()
            } else {
                // set user when pip mode
                setUserOrJwt()
                val campaignKey = CampaignSettings.campaignKey(this) ?: run {
                    startActivity(CampaignActivity.buildIntent(this))
                    return@setOnClickListener
                }
                ShopLive.play(this, ShopLivePlayerData(campaignKey).apply {
                    referrer = "referrer"
                })
            }
        }

        binding.btPopupPreview.setOnClickListener {
            setOptions()
            startPreview()
        }

        binding.btInAppPreview.setOnClickListener {
            if (binding.preview.visibility == View.VISIBLE) {
                binding.preview.release()
            } else {
                val accessKey =
                    CampaignSettings.accessKey(this) ?: return@setOnClickListener
                val campaignKey =
                    CampaignSettings.campaignKey(this) ?: return@setOnClickListener

                binding.preview.start(accessKey, campaignKey)
                binding.preview.visibility = View.VISIBLE
            }
        }

        binding.preview.setLifecycleObserver(this)
        binding.preview.useCloseButton(Options.isUseCloseButton())
        binding.preview.setOnClickListener {
            setOptions()
            val campaignKey =
                CampaignSettings.campaignKey(this) ?: return@setOnClickListener
            // Preview transition animation
            ShopLive.setPreviewTransitionAnimation(this, binding.preview)
            ShopLive.play(this, ShopLivePlayerData(campaignKey).apply {
                referrer = "referrer"
            })
            binding.preview.release()
        }
        binding.preview.setOnCloseListener {
            binding.preview.visibility = View.GONE
        }

        binding.btHybridShortform.setOnClickListener {
            startActivity(
                HybridShortformActivity.buildIntent(
                    this,
                    "https://shopliveshorts.cafe24.com/index.html"
                )
            )
        }

        binding.btNativeShortform.setOnClickListener {
            val accessKey: String = CampaignSettings.accessKey(this) ?: kotlin.run {
                startActivity(CampaignActivity.buildIntent(this))
                return@setOnClickListener
            }
            ShopLiveCommon.setAccessKey(accessKey)
            startActivity(NativeShortformActivity.intent(this))
        }

        binding.btShortformEditor.setOnClickListener {
            editorDialog.dismiss()
            editorDialog.show()
        }

        ShopLive.setHandler(shopliveHandler)

        viewModel.getSdkVersion()

        viewModel.playFromDeeplink(
            intent.getStringExtra("accessKey") ?: return,
            intent.getStringExtra("campaignKey") ?: return
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadCampaignData(this@MainActivity)
        viewModel.loadUserData(this@MainActivity)
        binding.tvOption.text = Options.toString(this@MainActivity)
    }

    private fun setUserOrJwt() {
        when (CampaignSettings.authType(this)) {
            CampaignSettings.UserType.USER.ordinal -> {
                val accessKey: String = CampaignSettings.accessKey(this) ?: kotlin.run {
                    startActivity(CampaignActivity.buildIntent(this))
                    return
                }
                val user = CampaignSettings.user(this) ?: return
                ShopLiveCommon.setUser(
                    accessKey,
                    ShopLiveCommonUser(user.userId ?: return).apply {
                        userName = user.userName
                        age = user.age
                        gender = when (user.gender) {
                            ShopLiveUserGender.Female -> ShopLiveCommonUserGender.FEMALE
                            ShopLiveUserGender.Male -> ShopLiveCommonUserGender.MALE
                            ShopLiveUserGender.Neutral -> ShopLiveCommonUserGender.NEUTRAL
                            else -> null
                        }
                        userScore = user.userScore
                    })
            }

            CampaignSettings.UserType.JWT.ordinal -> {
                val jwt = CampaignSettings.jwt(this) ?: return
                ShopLiveCommon.setAuthToken(jwt)
            }

            CampaignSettings.UserType.GUEST.ordinal -> {
                ShopLiveCommon.clearAuth()
            }
        }
    }

    private fun setOptions() {
        setUserOrJwt()

        // loading progress option
        if (Options.useLoadingImageAnimation()) {
            ShopLive.setLoadingAnimation(R.drawable.progress_animation1)
        } else {
            try {
                ShopLive.setLoadingProgressColor(Color.parseColor(Options.loadingProgressColor()))
            } catch (e: Exception) {
                // Do nothing
            }
        }

        if (Options.useCustomFontButton()) {
            // custom font option
            // only shoplive player
            ShopLive.setChatViewTypeface(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    resources.getFont(R.font.nanumgothic)
                } else {
                    ResourcesCompat.getFont(this, R.font.nanumgothic)
                }
            )
            // shoplive player + short-form
            ShopLiveCommon.setTextTypeface(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    resources.getFont(R.font.nanumgothic)
                } else {
                    ResourcesCompat.getFont(this, R.font.nanumgothic)
                }
            )
        } else {
            ShopLive.setChatViewTypeface(null)
            ShopLiveCommon.setTextTypeface(null)
        }

        ShopLive.setPIPRatio(Options.getPIPRatio())

        ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(
            Options.isKeepPlayVideoOnHeadphoneUnplugged(),
            Options.isMuteVideoOnHeadphoneUnplugged()
        )

        ShopLive.setAutoResumeVideoOnCallEnded(Options.isAutoResumeVideoOnCallEnded())

        ShopLive.setEnterPipModeOnBackPressed(Options.isEnterPipModeOnBackPressed())

        ShopLive.setMuteWhenPlayStart(Options.isMuteWhenPlayStart())

        ShopLive.setShareScheme(Options.shareSchemeUrl())

        ShopLive.setAutoCloseWhenAppDestroyed(Options.isAutoCloseWhenAppDestroyed())

        ShopLive.setNextActionOnHandleNavigation(Options.getNextActionOnHandleNavigation())

        ShopLive.setPlayerScreenCaptureEnabled(Options.isPlayerScreenCaptureEnabled())

        ShopLive.setVisibleStatusBar(Options.isVisibleStatusBar())

        ShopLive.setSoundFocusHandling(if (Options.isMuteWhenLossAudioFocus()) {
            object : OnAudioFocusListener {
                override fun onGain() {
                    ShopLive.unmute()
                }

                override fun onLoss() {
                    ShopLive.mute()
                }
            }
        } else {
            null
        })
    }

    private fun play() {
        ShopLiveCommon.setAccessKey(CampaignSettings.accessKey(this) ?: return)
        ShopLive.play(this, ShopLivePlayerData(CampaignSettings.campaignKey(this) ?: return).apply {
            referrer = "referrer"
        })
    }

    private fun startPreview() {
        val accessKey: String = CampaignSettings.accessKey(this) ?: return
        ShopLiveCommon.setAccessKey(accessKey)
        ShopLive.showPreviewPopup(
            this,
            ShopLivePreviewData(
                CampaignSettings.campaignKey(this) ?: return,
            ).apply {
                useCloseButton = Options.isUseCloseButton()
            }
        )
    }

    private val shopliveHandler = object : ShopLiveHandler() {
        override fun handleNavigation(
            context: Context,
            url: String
        ) {
            when (Options.getNextActionOnHandleNavigation()) {
                ShopLive.ActionType.PIP,
                ShopLive.ActionType.CLOSE -> {
                    Intent(this@MainActivity, WebViewActivity::class.java).apply {
                        putExtra("url", url)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }.run {
                        startActivity(this)
                    }
                }

                ShopLive.ActionType.KEEP -> {
                    WebViewDialogFragment().apply {
                        arguments = Bundle().apply {
                            putString("url", url)
                        }
                    }.run {
                        ShopLive.showDialogFragment(this)
                    }
                }
            }
        }

        override fun handleDownloadCoupon(
            context: Context,
            couponId: String,
            callback: ShopLiveHandlerCallback
        ) {
            AlertDialog.Builder(context).apply {
                setTitle(getString(R.string.sample_coupon_download))
                setMessage(getString(R.string.sample_coupon_download_id, couponId))
                setPositiveButton(getString(R.string.success)) { _, _ ->
                    callback.couponResult(
                        true,
                        getString(R.string.alert_coupon_download_success),
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST
                    )
                }
                setNegativeButton(getString(R.string.fail)) { _, _ ->
                    callback.couponResult(
                        false,
                        getString(R.string.alert_coupon_download_fail),
                        ShopLive.CouponPopupStatus.SHOW,
                        ShopLive.CouponPopupResultAlertType.ALERT
                    )
                }
                setCancelable(false)
            }.run {
                this.create().show()
            }
        }

        override fun onChangeCampaignStatus(context: Context, campaignStatus: String) {
            Log.d(TAG, "campaignStatus=$campaignStatus")
        }

        override fun onCampaignInfo(campaignInfo: JSONObject) {
            Log.d(TAG, "campaignInfo=$campaignInfo")
        }

        override fun onError(context: Context, error: ShopLiveCommonError) {
            Log.d(TAG, "code:${error.code}, message:${error.message}")
        }

        /*
        override fun handlePreview(context: Context, campaignKey: String) {
            super.handlePreview(context, campaignKey)
            //Toast.makeText(context, "ck=$campaignKey", Toast.LENGTH_SHORT).show()
        }*/

        override fun handleShare(context: Context, data: ShopLivePlayerShareData) {
            val shareUrl = data.url.let { url ->
                if (url.isNullOrEmpty()) {
                    "https://www.shoplive.cloud/"
                } else {
                    url
                }
            }
            CustomShareDialog(context, shareUrl).show()
        }

        override fun handleCustomAction(
            context: Context, id: String, type: String, payload: JSONObject,
            callback: ShopLiveHandlerCallback
        ) {
            CustomActionDialog(context, id, type, payload, callback)
                .show()
        }

        /**
         * @param isPipMode - pipMode:true, fullMode:false
         * @param playerLifecycle - 'CREATED' or 'CLOSING' or 'DESTROYED'
         * */
        override fun onChangedPlayerStatus(
            isPipMode: Boolean,
            playerLifecycle: ShopLive.PlayerLifecycle
        ) {
            super.onChangedPlayerStatus(isPipMode, playerLifecycle)
            Log.d(TAG, "isPipMode=$isPipMode, playerLifecycle=${playerLifecycle.name}")

            when (playerLifecycle) {
                ShopLive.PlayerLifecycle.CREATED -> {
                    // created
                }

                ShopLive.PlayerLifecycle.CLOSING -> {
                    // closing
                }

                ShopLive.PlayerLifecycle.DESTROYED -> {
                    // destroyed
                }
            }
        }

        override fun onSetUserName(jsonObject: JSONObject) {
            super.onSetUserName(jsonObject)
            Log.d(TAG, "onSetUserName = $jsonObject")
            try {
                Toast.makeText(
                    this@MainActivity,
                    "userId=${jsonObject.get("userId")}, userName=${jsonObject.get("userName")}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onReceivedCommand(context: Context, command: String, data: JSONObject) {
            Log.d(TAG, "onReceivedCommand = command=$command, data=$data")

            when (command) {
                "LOGIN_REQUIRED" -> {
                    AlertDialog.Builder(context).apply {
                        setMessage(getString(R.string.alert_need_login))
                        setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                            ShopLive.startPictureInPicture()
                            loginResult.launch(LoginActivity.buildIntent(this@MainActivity))
                            dialog.dismiss()
                        }
                        setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
                    }.run {
                        this.create().show()
                    }
                }

                "CLICK_PRODUCT_DETAIL" -> {
                    /*
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(command)
                    builder.setMessage(data.toString())
                    builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog: Dialog = builder.create()
                    dialog.show()*/
                    //Toast.makeText(this@MainActivity, command, Toast.LENGTH_SHORT).show()
                }

                "CLICK_PRODUCT_CART" -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(command)
                    builder.setMessage(data.toString())
                    builder.setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog: Dialog = builder.create()
                    dialog.show()
                }

                "ON_SUCCESS_CAMPAIGN_JOIN" -> {
                    /*
                    val isGuest = data.getString("isGuest")
                    Toast.makeText(this@MainActivity, "isGuest=$isGuest", Toast.LENGTH_SHORT).show()
                    */
                }

                "EVENT_DEEPLINK" -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(command)
                    builder.setMessage(data.toString())
                    builder.setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog: Dialog = builder.create()
                    dialog.show()
                }

                "CLICK_PRODUCT_BANNER_LINK",
                "CLICK_PRODUCT_BANNER_COUPON" -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(command)
                    builder.setMessage(data.toString())
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog: Dialog = builder.create()
                    dialog.show()
                }

                "CLICK_BACK_BUTTON" -> {
                    ShopLive.close()
                }

                "ON_CLICK_BRAND_FAVORITE_BUTTON" -> {
                    val identifier = data.getString("identifier")
                    val isFavorite = data.getBoolean("favorite")
                    val brandFavorite = mapOf(
                        Pair("identifier", identifier),
                        Pair("favorite", !isFavorite)
                    )
                    ShopLive.sendCommandMessage(
                        "SET_BRAND_FAVORITE",
                        brandFavorite
                    )
                    val layerToastData = mapOf(
                        Pair(
                            "message",
                            "ON_CLICK_BRAND_FAVORITE_BUTTON : ${!isFavorite}"
                        ),
                        Pair("duration", 1000),
                        Pair("position", "CENTER")
                    )
                    ShopLive.sendCommandMessage(
                        "SHOW_LAYER_TOAST",
                        layerToastData,
                    )
                }

                "ON_CHANGED_BRAND_FAVORITE" -> {
                    val identifier = data.getString("identifier")
                    val layerToastData = mapOf(
                        Pair(
                            "message",
                            "ON_CHANGED_BRAND_FAVORITE : $identifier"
                        ),
                        Pair("duration", 1000),
                        Pair("position", "CENTER")
                    )
                    ShopLive.sendCommandMessage(
                        "SHOW_LAYER_TOAST",
                        layerToastData,
                    )
                }

                "ON_RECEIVED_SELLER_CONFIG" -> {
                    val sellerSavedData = mapOf(
                        Pair("saved", true)
                    )
                    ShopLive.sendCommandMessage(
                        "SET_SELLER_SAVED_STATE",
                        sellerSavedData
                    )
                }

                "ON_CLICK_VIEW_SELLER_STORE" -> {
                    val sellerStoreData =
                        Gson().fromJson(data.toString(), SellerStoreData::class.java)

                    val layerToastData = mapOf(
                        Pair(
                            "message",
                            "ON_CLICK_VIEW_SELLER_STORE : ${sellerStoreData.seller?.storeUrl ?: return}"
                        ),
                        Pair("duration", 1000),
                        Pair("position", "CENTER")
                    )
                    ShopLive.sendCommandMessage(
                        "SHOW_LAYER_TOAST",
                        layerToastData,
                    )
                }

                "ON_CLICK_SELLER_SUBSCRIPTION" -> {
                    val sellerSubscriptionData =
                        Gson().fromJson(data.toString(), SellerSubscriptionData::class.java)
                    val sellerSavedData = mapOf(
                        Pair("saved", !sellerSubscriptionData.saved)
                    )
                    ShopLive.sendCommandMessage(
                        "SET_SELLER_SAVED_STATE",
                        sellerSavedData
                    )
                    val layerToastData = mapOf(
                        Pair(
                            "message",
                            "SET_SELLER_SAVED_STATE : ${!sellerSubscriptionData.saved}"
                        ),
                        Pair("duration", 1000),
                        Pair("position", "CENTER")
                    )
                    ShopLive.sendCommandMessage(
                        "SHOW_LAYER_TOAST",
                        layerToastData,
                    )
                    Toast.makeText(context, "MESSAGE", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private var isMuted = false
        override fun onEvent(context: Context, data: ShopLiveLog.Data) {
            super.onEvent(context, data)
            when (data.name) {
                "video_muted" -> {
                    isMuted = true
                }

                "video_unmuted" -> {
                    isMuted = false
                }
            }

            if (data.name == "product_list_item" && data.feature == "click") {
                ShopLiveEvent.sendConversionEvent(
                    ShopLiveConversionData(
                        "purchase",
                        products = listOf(
                            ShopLiveConversionProductData(
                                productId = data.parameter?.get("goodsId") as? String,
                                sku = data.parameter?.get("sku") as? String,
                                url = data.parameter?.get("url") as? String,
                                purchaseQuantity = 1,
                                purchaseUnitPrice = data.parameter?.get("discountedPrice") as? Double,
                            )
                        ),
                        orderId = "orderId",
                        referrer = "referrer",
                        custom = mapOf(
                            "campaignKey" to data.campaignKey
                        )
                    )
                )
            }
        }
    }

    private val loginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.data?.let {
                        val userId = it.getStringExtra(LoginActivity.USER_ID)
                        Log.d(TAG, "login userId=$userId")
                        setOptions()
                        play()
                    }
                }
            }
        }


    private fun showShortformEditor() {
        ShopLiveShortformEditor(this)
            .apply {
                setVideoEditorData(ShopLiveVideoEditorData().apply {
                    aspectRatio = ShopLiveShortformEditorAspectRatio(9, 16)
                    visibleActionButton =
                        ShopLiveShortformEditorVisibleActionButton().apply {
                            isUsedCropButton = false
                            isUsedPlaybackSpeedButton = false
                            isUsedFilterButton = false
                            isUsedVolumeButton = true
                        }
                    minVideoDuration = 3 * 1000
                    maxVideoDuration = 90 * 1000
                })
                setVideoUploaderData(ShopLiveVideoUploaderData().apply {
                    visibleContentData =
                        ShopLiveShortformEditorVisibleContentData().apply {
                            isDescriptionVisible = true
                            isTagsVisible = true
                        }
                })
                setHandler(object : ShopLiveShortformEditorHandler() {
                    override fun onSuccess(
                        activity: ComponentActivity,
                        resultData: ShopLiveEditorResultData
                    ) {
                        super.onSuccess(activity, resultData)
                        Toast.makeText(
                            this@MainActivity,
                            "onComplete",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(context: Context, error: ShopLiveCommonError) {
                        super.onError(context, error)
                        Toast.makeText(
                            this@MainActivity,
                            "onError : $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCancel() {
                        super.onCancel()
                        Toast.makeText(
                            this@MainActivity,
                            "onClosed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }.start()
    }

    private val videoEditorLock = AtomicBoolean(false)
    private fun showVideoEditor() {
        if (videoEditorLock.getAndSet(true)) {
            return
        }
        this.activityResultRegistry
            .register(
                KEY_PICK_VISUAL_VIDEO_REQUEST,
                ActivityResultContracts.PickVisualMedia()
            ) {
                if (it != null) {
                    ShopLiveVideoEditor(this)
                        .setData(ShopLiveVideoEditorData())
                        .setHandler(object : ShopLiveVideoEditorHandler() {
                            override fun onSuccess(
                                videoEditorActivity: ComponentActivity,
                                result: ShopLiveEditorResultData
                            ) {
                                super.onSuccess(videoEditorActivity, result)
                                Toast.makeText(
                                    this@MainActivity,
                                    "onComplete : ${result.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ShopLiveVideoEditor.close()
                            }

                            override fun onError(context: Context, error: ShopLiveCommonError) {
                                super.onError(context, error)
                                Toast.makeText(
                                    this@MainActivity,
                                    "onError : $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onCancel() {
                                super.onCancel()
                                Toast.makeText(
                                    this@MainActivity,
                                    "onClosed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }).start(ShopLiveEditorLocalData(it))
                }
                videoEditorLock.set(false)
            }.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }

    private val imageEditorLock = AtomicBoolean(false)
    private fun showImageEditor() {
        if (imageEditorLock.getAndSet(true)) {
            return
        }
        this.activityResultRegistry
            .register(
                KEY_PICK_VISUAL_IMAGE_REQUEST,
                ActivityResultContracts.PickVisualMedia()
            ) {
                if (it != null) {
                    ShopLiveImageEditor(this)
                        .setData(ShopLiveImageEditorData())
                        .setHandler(object : ShopLiveImageEditorHandler() {
                            override fun onSuccess(result: ShopLiveEditorResultData) {
                                super.onSuccess(result)
                                Toast.makeText(
                                    this@MainActivity,
                                    "onComplete : ${result.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onError(error: ShopLiveCommonError) {
                                super.onError(error)
                                Toast.makeText(
                                    this@MainActivity,
                                    "onError : $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onCancel() {
                                super.onCancel()
                                Toast.makeText(
                                    this@MainActivity,
                                    "onClosed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }).start(ShopLiveEditorLocalData(it))
                }
                imageEditorLock.set(false)
            }.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showCoverPicker() {
        ShopLiveCoverPicker(this)
            .setData(ShopLiveCoverPickerData().apply {
                this.visibleActionButton = ShopLiveCoverPickerVisibleActionButton(true)
            })
            .setHandler(object : ShopLiveCoverPickerHandler() {
                override fun onSuccess(
                    coverPickerActivity: ComponentActivity,
                    result: ShopLiveEditorResultData
                ) {
                    super.onSuccess(coverPickerActivity, result)
                    Toast.makeText(
                        coverPickerActivity,
                        result.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    ShopLiveCoverPicker.close()
                }

                override fun onError(context: Context, error: ShopLiveCommonError) {
                    super.onError(context, error)
                    Toast.makeText(
                        this@MainActivity,
                        error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancel() {
                    super.onCancel()
                    Toast.makeText(this@MainActivity, "onClosed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .start(ShopLiveCoverPickerUrlData("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
    }
}

private data class SellerStoreData(
    val campaignKey: String?,
    val campaignStatus: String?,
    val campaignTitle: String?,
    val seller: Seller?
)

private data class SellerSubscriptionData(
    val campaignKey: String?,
    val campaignStatus: String?,
    val campaignTitle: String?,
    val isLogin: Boolean,
    val saved: Boolean,
    val seller: Seller?
)

private data class Seller(
    val description: String?,
    val name: String?,
    val profileUrl: String?,
    val sellerId: Int,
    val sellerIdentifier: String,
    val storeUrl: String?
)
