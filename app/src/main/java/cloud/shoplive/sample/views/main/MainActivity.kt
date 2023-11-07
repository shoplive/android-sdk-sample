package cloud.shoplive.sample.views.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
import cloud.shoplive.sample.views.dialog.CustomActionDialog
import cloud.shoplive.sample.views.dialog.CustomShareDialog
import cloud.shoplive.sample.views.login.LoginActivity
import cloud.shoplive.sample.views.settings.SettingsActivity
import cloud.shoplive.sample.views.user.UserActivity
import cloud.shoplive.sdk.OnAudioFocusListener
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveHandler
import cloud.shoplive.sdk.ShopLiveHandlerCallback
import cloud.shoplive.sdk.ShopLivePlayerData
import cloud.shoplive.sdk.ShopLivePreviewData
import cloud.shoplive.sdk.ShopLiveUserGender
import cloud.shoplive.sdk.common.ShopLiveCommon
import cloud.shoplive.sdk.common.ShopLiveCommonUser
import cloud.shoplive.sdk.common.ShopLiveCommonUserGender
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        private const val ACCESS_KEY = "accessKey"
        private const val CAMPAIGN_KEY = "campaignKey"

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
                ShopLive.play(
                    this,
                    ShopLivePlayerData(
                        CampaignSettings.campaignKey(this) ?: return@setOnClickListener
                    ).apply {
                        keepWindowStateOnPlayExecuted = true
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
            ShopLive.play(this, ShopLivePlayerData(campaignKey))
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
            ShopLive.play(this, ShopLivePlayerData(campaignKey))
            binding.previewSwipe.release()
        }
        binding.previewSwipe.setOnCloseListener {
            binding.previewSwipe.visibility = View.GONE
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
                ShopLive.setUser(user)
                ShopLiveCommon.setUserJWT(
                    accessKey,
                    ShopLiveCommonUser(user.userId ?: return).apply {
                        name = user.userName
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
                ShopLive.setAuthToken(jwt)
                ShopLiveCommon.setUserJWT(jwt)
            }

            CampaignSettings.UserType.GUEST.ordinal -> {
                ShopLive.setUser(null)
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
            ShopLive.setLoadingProgressColor(Options.loadingProgressColor())
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

        ShopLive.setKeepAspectOnTabletPortrait(Options.isKeepAspectOnTabletPortrait())

        ShopLive.setAutoCloseWhenAppDestroyed(Options.isAutoCloseWhenAppDestroyed())

        ShopLive.setNextActionOnHandleNavigation(Options.getNextActionOnHandleNavigation())

        ShopLive.setPlayerScreenCaptureEnabled(Options.isPlayerScreenCaptureEnabled())

        ShopLive.setStatusBarTransparent(Options.isStatusBarTransparent())

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
        ShopLive.setAccessKey(CampaignSettings.accessKey(this) ?: return)
        ShopLive.play(this, ShopLivePlayerData(CampaignSettings.campaignKey(this) ?: return))
    }

    private fun startPreview() {
        ShopLive.showPreviewPopup(
            this,
            ShopLivePreviewData(
                CampaignSettings.campaignKey(this) ?: return,
            ).apply {
                useCloseButton = true
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

        override fun onError(context: Context, code: String, message: String) {
            Log.d(TAG, "code:$code, message:$message")
        }

        /*
        override fun handlePreview(context: Context, campaignKey: String) {
            super.handlePreview(context, campaignKey)
            //Toast.makeText(context, "ck=$campaignKey", Toast.LENGTH_SHORT).show()
        }*/

        override fun handleShare(context: Context, shareUrl: String) {
            CustomShareDialog(context, shareUrl).show()
        }

        override fun handleCustomAction(
            context: Context, id: String, type: String, payload: String,
            callback: ShopLiveHandlerCallback
        ) {
            CustomActionDialog(context, id, type, payload, callback)
                .show()
        }

        /**
         * @param isPipMode - pipMode:true, fullMode:false
         * @param state - 'CREATED' or 'CLOSING' or 'DESTROYED'
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
            Log.d(TAG, "onReceivedCommand = command=$command, data=${data.toString()}")

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