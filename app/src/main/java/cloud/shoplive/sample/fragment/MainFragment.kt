package cloud.shoplive.sample.fragment

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import cloud.shoplive.sample.*
import cloud.shoplive.sample.R
import cloud.shoplive.sample.databinding.FragmentMainBinding
import cloud.shoplive.sdk.*
import org.json.JSONObject

class MainFragment : Fragment() {

    companion object {
        val TAG: String = MainFragment::class.java.name
    }

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Options.init(requireContext())

        viewModel.liveInfo.observe(requireActivity()) {
            CampaignSettings.setAccessKey(requireContext(), it.accessKey)
            CampaignSettings.setCampaignKey(requireContext(), it.campaignKey)
            binding.tvCampaign.text = loadCampaignData()
            setOptions()
            play()
        }

        binding.btCampaign.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.campaign_fragment)
        }

        binding.btUser.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.user_fragment)
        }

        binding.btOption.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.settings_fragment)
        }

        binding.btPlay.setOnClickListener {
            if (!ShopLive.isSuccessCampaignJoin()) {
                // first play
                setOptions()
                play()
            } else {
                // set user when pip mode
                setUserOrJwt()
                CampaignSettings.campaignKey(requireContext())?.let {
                    ShopLive.play(it, true)
                }
            }
        }

        binding.btWindowPreview.setOnClickListener {
            setOptions()
            startPreview()
        }

        binding.btInAppPreview.setOnClickListener {
            if (binding.preview.visibility == View.VISIBLE) {
                binding.preview.release()
            } else {
                val accessKey =
                    CampaignSettings.accessKey(requireContext()) ?: return@setOnClickListener
                val campaignKey =
                    CampaignSettings.campaignKey(requireContext()) ?: return@setOnClickListener

                binding.preview.start(accessKey, campaignKey)
                binding.preview.visibility = View.VISIBLE
            }
        }

        binding.btPreviewSwipe.setOnClickListener {
            if (binding.previewSwipe.visibility == View.VISIBLE) {
                binding.previewSwipe.release()
            } else {
                val accessKey =
                    CampaignSettings.accessKey(requireContext()) ?: return@setOnClickListener
                val campaignKey =
                    CampaignSettings.campaignKey(requireContext()) ?: return@setOnClickListener

                binding.previewSwipe.start(accessKey, campaignKey)
                binding.previewSwipe.visibility = View.VISIBLE
            }
        }

        binding.preview.setLifecycleObserver(this.viewLifecycleOwner)
        binding.preview.setOnClickListener {
            setOptions()
            val campaignKey =
                CampaignSettings.campaignKey(requireContext()) ?: return@setOnClickListener
            // Preview transition animation
            ShopLive.setPreviewTransitionAnimation(requireActivity(), binding.preview)
            ShopLive.play(requireActivity(), campaignKey)
            binding.preview.release()
        }
        binding.preview.setOnCloseListener {
            binding.preview.visibility = View.GONE
        }

        binding.previewSwipe.setLifecycleObserver(this.viewLifecycleOwner)
        binding.previewSwipe.setOnPreviewClickListener {
            setOptions()
            val campaignKey =
                CampaignSettings.campaignKey(requireContext()) ?: return@setOnPreviewClickListener
            // Preview transition animation
            ShopLive.setPreviewTransitionAnimation(requireActivity(), binding.previewSwipe.preview)
            ShopLive.play(requireActivity(), campaignKey)
            binding.previewSwipe.release()
        }
        binding.previewSwipe.setOnCloseListener {
            binding.previewSwipe.visibility = View.GONE
        }

        registerShopLiveHandler()

        binding.tvSdkVersion.text = getString(R.string.label_sdk_version, ShopLive.getVersion())
    }

    override fun onResume() {
        super.onResume()

        binding.tvCampaign.text = loadCampaignData()
        binding.tvUser.text = loadUserData()
        binding.tvOption.text = Options.toString(requireContext())
    }

    private fun loadCampaignData(): String {
        return "• Access Key : ${CampaignSettings.accessKey(requireContext()) ?: getString(R.string.label_none)}\n" +
                "• Campaign Key : ${CampaignSettings.campaignKey(requireContext()) ?: getString(R.string.label_none)}"
    }

    private fun loadUserData(): String {
        var userText = ""
        when(CampaignSettings.authType(requireContext())) {
            0 -> {
                // user
                userText = getString(R.string.label_use_user2) + "\n"
                val user = CampaignSettings.user(requireContext())
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
            1 -> {
                // token
                userText = getString(R.string.label_use_token2) + "\n"

                val token = CampaignSettings.jwt(requireContext())
                userText += token ?: getString(R.string.label_no_token)
            }
            2 -> {
                // guest
                userText = getString(R.string.label_use_guest2)
            }
        }
        return userText
    }

    private fun setUserOrJwt() {
        when(CampaignSettings.authType(requireContext())) {
            0 -> { CampaignSettings.user(requireContext())?.let { ShopLive.setUser(it) } }
            1 -> { CampaignSettings.jwt(requireContext())?.let { ShopLive.setAuthToken(it) } }
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
            ResourcesCompat.getFont(requireContext(), R.font.nanumgothic)
        }

        // 나눔 고딕 볼드
        val nanumGothicBold = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.nanumgothicbold)
        } else {
            ResourcesCompat.getFont(requireContext(), R.font.nanumgothicbold)
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
        CampaignSettings.accessKey(requireContext())?.let {
            ShopLive.setAccessKey(it)
        }

        CampaignSettings.campaignKey(requireContext())?.let {
            ShopLive.play(it)
        }
    }

    private fun startPreview() {
        CampaignSettings.accessKey(requireContext())?.let {
            ShopLive.setAccessKey(it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(requireContext())) {
                CampaignSettings.campaignKey(requireContext())?.let {
                    ShopLive.showPreview(it)
                }
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(getString(R.string.alert_preview_permission_info))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${requireContext().packageName}")
                    )
                    overlaysSettingResult.launch(intent)
                }
                builder.setNegativeButton(getString(R.string.no), null)

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    private val overlaysSettingResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(requireContext())) {
                CampaignSettings.campaignKey(requireContext())?.let {
                    ShopLive.showPreview(it)
                }
            }
        }
    }

    private fun registerShopLiveHandler() {
        ShopLive.setHandler(object : ShopLiveHandler {
            override fun handleNavigation(
                context: Context,
                url: String
            ) {
                when(Options.getNextActionOnHandleNavigation()) {
                    ShopLive.ActionType.PIP,
                    ShopLive.ActionType.CLOSE -> {
                        val intent = Intent(requireActivity(), WebViewActivity::class.java)
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
                val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
                                            callback: ShopLiveHandlerCallback) {
                val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
                    Toast.makeText(requireContext(),
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
                            goToSignIn()
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
        })
    }

    private fun goToSignIn() {
        Handler(Looper.getMainLooper()).postDelayed({
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.signin_fragment)
            setFragmentResultListener("requestKey") { requestKey, bundle ->
                val userId = bundle.getString("userId")
                Log.d(TAG, "requestKey=$requestKey, userId=$userId")
                setOptions()
                play()
            }
        }, 100)
    }
}