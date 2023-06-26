package cloud.shoplive.sample.views.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import cloud.shoplive.sample.views.login.LoginActivity
import cloud.shoplive.sample.views.settings.SettingsActivity
import cloud.shoplive.sample.views.user.UserActivity
import cloud.shoplive.sdk.OnAudioFocusListener
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveHandler
import cloud.shoplive.sdk.ShopLiveHandlerCallback
import cloud.shoplive.sdk.ShopLiveUserGender
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLivePreviewPositionConfig
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        private const val ACCESS_KEY = "accessKey"
        private const val CAMPAIGN_KEY = "campaignKey"

        fun buildIntent(context: Context) : Intent {
            return Intent(context, MainActivity::class.java)
        }

        fun buildIntentFromDeeplink(
            context: Context,
            accessKey: String?,
            campaignKey: String?
        ): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(ACCESS_KEY, accessKey)
            intent.putExtra(CAMPAIGN_KEY, campaignKey)
            return intent
        }
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Options.init(this)

//        viewModel.liveInfo.observe(this) {
//            CampaignSettings.setAccessKey(this, it.accessKey)
//            CampaignSettings.setCampaignKey(this, it.campaignKey)
//            binding.tvCampaign.text = loadCampaignData()
//            setOptions()
//            play()
//        }

        viewModel.campaignInfo.observe(this) {
            binding.tvCampaign.text =
                "• Access Key : ${it.accessKey ?: getString(R.string.label_none)}\n" + "• Campaign Key : ${
                    it.campaignKey ?: getString(R.string.label_none)
                }"
        }

        viewModel.shopliveUser.observe(this) {user ->
            var userText = ""
            when(CampaignSettings.authType(this)) {
                CampaignSettings.UserType.USER.ordinal -> {
                    // user
                    userText = getString(R.string.label_use_user2) + "\n"
                    userText += if (user == null) {
                        getString(R.string.label_no_user)
                    } else {
                        val gender = when(user.gender) {
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
                    // token
                    userText = getString(R.string.label_use_token2) + "\n"

                    val token = CampaignSettings.jwt(this)
                    userText += token ?: getString(R.string.label_no_token)
                }
                CampaignSettings.UserType.GUEST.ordinal -> {
                    // guest
                    userText = getString(R.string.label_use_guest2)
                }
            }
            binding.tvUser.text = userText
        }

        viewModel.sdkVersion.observe(this) {version ->
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
                CampaignSettings.campaignKey(this)?.let {
                    ShopLive.play(it, true)
                }
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

        binding.btPreviewSwipe.setOnClickListener {
            if (binding.previewSwipe.visibility == View.VISIBLE) {
                binding.previewSwipe.release()
            } else {
                val accessKey =
                    CampaignSettings.accessKey(this) ?: return@setOnClickListener
                val campaignKey =
                    CampaignSettings.campaignKey(this) ?: return@setOnClickListener

                binding.previewSwipe.start(accessKey, campaignKey)
                binding.previewSwipe.visibility = View.VISIBLE
            }
        }

        binding.preview.setLifecycleObserver(this)
        binding.preview.setOnClickListener {
            setOptions()
            val campaignKey =
                CampaignSettings.campaignKey(this) ?: return@setOnClickListener
            // Preview transition animation
            ShopLive.setPreviewTransitionAnimation(this, binding.preview)
            ShopLive.play(this, campaignKey)
            binding.preview.release()
        }
        binding.preview.setOnCloseListener {
            binding.preview.visibility = View.GONE
        }

        binding.previewSwipe.setLifecycleObserver(this)
        binding.previewSwipe.setOnPreviewClickListener {
            setOptions()
            val campaignKey =
                CampaignSettings.campaignKey(this) ?: return@setOnPreviewClickListener
            // Preview transition animation
            ShopLive.setPreviewTransitionAnimation(this, binding.previewSwipe.preview)
            ShopLive.play(this, campaignKey)
            binding.previewSwipe.release()
        }
        binding.previewSwipe.setOnCloseListener {
            binding.previewSwipe.visibility = View.GONE
        }

        binding.btHybridShortform.setOnClickListener {
            startActivity(HybridShortformActivity.buildIntent(this, "https://shopliveshorts.cafe24.com/index.html"))
        }

        binding.btNativeShortform.setOnClickListener {
            val accessKey: String = CampaignSettings.accessKey(this) ?: return@setOnClickListener
            ShopLiveCommon.setAccessKey(accessKey)
            startActivity(NativeShortformActivity.buildIntent(this))
        }

        ShopLive.setHandler(shopliveHandler)

        viewModel.getSdkVersion()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadCampaignData(this)
        viewModel.loadUserData(this)
        binding.tvOption.text = Options.toString(this)
    }

    private fun setUserOrJwt() {
        when(CampaignSettings.authType(this)) {
            0 -> { CampaignSettings.user(this)?.let { ShopLive.setUser(it) } }
            1 -> { CampaignSettings.jwt(this)?.let { ShopLive.setAuthToken(it) } }
            2 -> { ShopLive.setUser(null)}
        }
    }

    private fun setLoadingProgress() {
        if (Options.useLoadingImageAnimation()) {
            ShopLive.setLoadingAnimation(R.drawable.progress_animation1)
        } else {
            val hexColor = Options.loadingProgressColor()
            ShopLive.setLoadingProgressColor(hexColor)
        }
    }

    private fun setCustomFontForChatting() {
        // 나눔 고딕
        val nanumGothic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.nanumgothic)
        } else {
            ResourcesCompat.getFont(this, R.font.nanumgothic)
        }

        // 나눔 고딕 볼드
        val nanumGothicBold = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.nanumgothicbold)
        } else {
            ResourcesCompat.getFont(this, R.font.nanumgothicbold)
        }

        val chatInputTf = if (Options.useCustomFontChatInput()) nanumGothic else null
        val chatSendTf = if (Options.useCustomFontChatSendButton()) nanumGothicBold else null
        ShopLive.setChatViewTypeface(chatInputTf, chatSendTf)
    }

    private fun setOptions() {
        setUserOrJwt()

        setLoadingProgress()

        setCustomFontForChatting()

        ShopLive.setPIPRatio(Options.getPIPRatio())

        ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(
            Options.isKeepPlayVideoOnHeadphoneUnplugged(),
            Options.isMuteVideoOnHeadphoneUnplugged()
        )

        ShopLive.setAutoResumeVideoOnCallEnded(Options.isAutoResumeVideoOnCallEnded())

        ShopLive.setEnterPipModeOnBackPressed(Options.isEnterPipModeOnBackPressed())

        ShopLive.setMuteWhenPlayStart(Options.isMuteWhenPlayStart())

        ShopLive.setShareScheme(Options.shareSchemeUrl())

        ShopLive.setKeepAspectOnTabletPortrait(Options.isKeepAspectOnTabletPortrait())

        ShopLive.setAutoCloseWhenAppDestroyed(Options.isAutoCloseWhenAppDestroyed())

        ShopLive.setNextActionOnHandleNavigation(Options.getNextActionOnHandleNavigation())

        ShopLive.setPlayerScreenCaptureEnabled(Options.isPlayerScreenCaptureEnabled())

        ShopLive.setStatusBarTransparent(Options.isStatusBarTransparent())

        ShopLive.setUiMessage(ShopLive.UiMessageType.NOT_SUPPORT_PIP, R.string.alert_not_support_pip)

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
        CampaignSettings.accessKey(this)?.let {
            ShopLive.setAccessKey(it)
        }

        CampaignSettings.campaignKey(this)?.let {
            ShopLive.play(it)
        }
    }

    private fun startPreview() {
        val accessKey = CampaignSettings.accessKey(this)
        val campaignKey = CampaignSettings.campaignKey(this)

        ShopLive.showPreviewPopup(this, accessKey ?: return, campaignKey ?: return, true, true, Options.isUseCloseButton(), ShopLivePreviewPositionConfig.BOTTOM_RIGHT)
    }

    private val shopliveHandler = object : ShopLiveHandler {
        override fun handleNavigation(
            context: Context,
            url: String
        ) {
            when(Options.getNextActionOnHandleNavigation()) {
                ShopLive.ActionType.PIP,
                ShopLive.ActionType.CLOSE -> {
                    val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("url", url)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                ShopLive.ActionType.KEEP -> {
                    val fragment = WebViewDialogFragment()
                    fragment.arguments = Bundle().apply {
                        putString("url", url)
                    }
                    ShopLive.showDialogFragment(fragment)
                }
            }
        }

        override fun handleDownloadCoupon(
            context: Context,
            couponId: String,
            callback: ShopLiveHandlerCallback
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.sample_coupon_download))
            builder.setMessage(getString(R.string.sample_coupon_download_id, couponId))
            builder.setPositiveButton(getString(R.string.success)) { _, _ ->
                callback.couponResult(
                    true,
                    getString(R.string.alert_coupon_download_success),
                    ShopLive.CouponPopupStatus.HIDE,
                    ShopLive.CouponPopupResultAlertType.TOAST
                )
            }
            builder.setNegativeButton(getString(R.string.fail)) { _, _ ->
                callback.couponResult(
                    false,
                    getString(R.string.alert_coupon_download_fail),
                    ShopLive.CouponPopupStatus.SHOW,
                    ShopLive.CouponPopupResultAlertType.ALERT
                )
            }
            builder.setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        }

        override fun onChangeCampaignStatus(context: Context, campaignStatus: String) {
            Log.d(TAG, "campaignStatus >> $campaignStatus")
        }

        override fun onCampaignInfo(campaignInfo: JSONObject) {
            Log.d(TAG, campaignInfo.toString())
        }

        override fun onError(context: Context, code: String, message: String) {
            Log.d(TAG, "code:$code, message:$message")
        }

        /*
        override fun handlePreview(context: Context, campaignKey: String) {
            super.handlePreview(context, campaignKey)
            //Toast.makeText(context, "ck=$campaignKey", Toast.LENGTH_SHORT).show()
        }*/

        override fun handleShare(context: Context, shareUrl: String) {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.custom_share_dialog, null)
            val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
            tvMessage.text = shareUrl

            val btCopy: Button = view.findViewById(R.id.btCopy)
            val btKakao: Button = view.findViewById(R.id.btKakao)
            val btLine: Button = view.findViewById(R.id.btLine)

            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.sample_share))
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            btCopy.setOnClickListener {
                Toast.makeText(context, "${getString(R.string.sample_copy_link)}!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            btKakao.setOnClickListener {
                Toast.makeText(context, "${getString(R.string.sample_share_kakao)}!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            btLine.setOnClickListener {
                Toast.makeText(context, "${getString(R.string.sample_share_line)}!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        override fun handleCustomAction(context: Context, id: String, type: String, payload: String,
                                        callback: ShopLiveHandlerCallback
        ) {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.custom_action_dialog, null)
            val tvId = view.findViewById<TextView>(R.id.tvId)
            val tvType = view.findViewById<TextView>(R.id.tvType)
            val tvPayload = view.findViewById<TextView>(R.id.tvPayload)

            tvId.text = getString(R.string.sample_custom_action_id, id)
            tvType.text = getString(R.string.sample_custom_action_type, type)
            tvPayload.text = getString(R.string.sample_custom_action_payload, payload)

            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.sample_custom_action))
            builder.setView(view)
            builder.setPositiveButton(getString(R.string.success)) { _, _ ->
                callback.customActionResult(
                    true,
                    getString(R.string.alert_custom_action_success),
                    ShopLive.CouponPopupStatus.HIDE,
                    ShopLive.CouponPopupResultAlertType.TOAST
                )
            }
            builder.setNegativeButton(getString(R.string.fail)) { _, _ ->
                callback.customActionResult(
                    false,
                    getString(R.string.alert_custom_action_fail),
                    ShopLive.CouponPopupStatus.SHOW,
                    ShopLive.CouponPopupResultAlertType.ALERT
                )
            }
            builder.setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        }

        /**
         * @param isPipMode - pipMode:true, fullMode:false
         * @param state - 'CREATED' or 'CLOSING' or 'DESTROYED'
         * */
        override fun onChangedPlayerStatus(isPipMode: Boolean, playerLifecycle: ShopLive.PlayerLifecycle) {
            super.onChangedPlayerStatus(isPipMode, playerLifecycle)
            Log.d(TAG, "isPipMode=$isPipMode, playerLifecycle=${playerLifecycle.getText()}")

            when(playerLifecycle) {
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
            Log.d(TAG, "onSetUserName = ${jsonObject.toString()}")
            try {
                Toast.makeText(this@MainActivity,
                    "userId=${jsonObject.get("userId")}, userName=${jsonObject.get("userName")}", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onReceivedCommand(context: Context, command: String, data: JSONObject) {
            Log.d(TAG, "onReceivedCommand = command=$command, data=${data.toString()}")

            when(command) {
                "LOGIN_REQUIRED" -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(getString(R.string.alert_need_login))
                    builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        ShopLive.startPictureInPicture()
                        loginResult.launch(LoginActivity.buildIntent(this@MainActivity))
                        dialog.dismiss()
                    }
                    builder.setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
                    val dialog: Dialog = builder.create()
                    dialog.show()
                }
                "CLICK_PRODUCT_CART" -> {

                }
                "CLICK_PRODUCT_DETAIL" -> {

                }
                "ON_SUCCESS_CAMPAIGN_JOIN" -> {

                }
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

}