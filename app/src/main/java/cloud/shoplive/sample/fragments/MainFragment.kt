package cloud.shoplive.sample.fragments

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebStorage
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cloud.shoplive.sample.*
import cloud.shoplive.sample.R
import cloud.shoplive.sdk.*
import org.json.JSONObject

class MainFragment : Fragment() {

    private var _view: View? = null
    lateinit var btCampaign: AppCompatButton
    lateinit var btUser: AppCompatButton
    lateinit var btOption: AppCompatButton
    lateinit var tvCampaign: AppCompatTextView
    lateinit var tvUser: AppCompatTextView
    lateinit var tvOption: AppCompatTextView
    lateinit var btPlay: Button
    lateinit var btPreview: Button

    companion object {
        val TAG: String = MainFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_view == null) {
            _view = inflater.inflate(R.layout.fragment_main, container, false)
        }
        return _view    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btCampaign = view.findViewById(R.id.btCampaign)
        btUser = view.findViewById(R.id.btUser)
        btOption = view.findViewById(R.id.btOption)
        tvCampaign = view.findViewById(R.id.tvCampaign)
        tvUser = view.findViewById(R.id.tvUser)
        tvOption = view.findViewById(R.id.tvOption)
        btPlay = view.findViewById(R.id.btPlay)
        btPreview = view.findViewById(R.id.btPreview)

        btCampaign.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.campaign_fragment)
        }

        btUser.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.user_fragment)
        }

        btOption.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.settings_fragment)
        }

        btPlay.setOnClickListener {
            makeCampaignData()
            play()
        }

        btPreview.setOnClickListener {
            makeCampaignData()
            preview()
        }

        registerShopLiveHandler()
    }

    override fun onResume() {
        super.onResume()

        tvCampaign.text = loadCampaignData()
        tvUser.text = loadUserData()
        tvOption.text = Options.toString(requireContext())
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

    private fun makeCampaignData() {
        // user set
        when(CampaignSettings.authType(requireContext())) {
            0 -> { CampaignSettings.user(requireContext())?.let { ShopLive.setUser(it) } }
            1 -> { CampaignSettings.jwt(requireContext())?.let { ShopLive.setAuthToken(it) } }
            2 -> { }
        }

        ShopLive.setShareScheme(Options.shareSchemeUrl(requireContext()))
        ShopLive.setLoadingProgressColor(Options.loadingProgressColor(requireContext()))

        if (Options.useLoadingImageAnimation(requireContext())) {
            ShopLive.setLoadingAnimation(R.drawable.progress_animation1)
        } else {
            ShopLive.setLoadingAnimation(0)
        }

        var nanumGothic: Typeface?
        var nanumGothicBold: Typeface?

        if (Options.useCustomFontChatInput(requireContext())) {
            nanumGothic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                resources.getFont(R.font.nanumgothic)
            } else {
                ResourcesCompat.getFont(requireContext(), R.font.nanumgothic)
            }
        } else {
            nanumGothic = null
        }

        if (Options.useCustomFontChatSendButton(requireContext())) {
            nanumGothicBold = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                resources.getFont(R.font.nanumgothicbold)
            } else {
                ResourcesCompat.getFont(requireContext(), R.font.nanumgothicbold)
            }
        } else {
            nanumGothicBold = null
        }

        ShopLive.setChatViewTypeface(nanumGothic, nanumGothicBold)
    }

    private fun play() {
        WebStorage.getInstance().deleteAllData()

        CampaignSettings.accessKey(requireContext())?.let {
            ShopLive.setAccessKey(it)
        }

        CampaignSettings.campaignKey(requireContext())?.let {
            ShopLive.play(it)
        }
    }

    private fun preview() {
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

                val type = ShopLive.ActionType.getType(Options.playerNextAction(requireContext()))
                type?.let {
                    when(it) {
                        ShopLive.ActionType.PIP,
                        ShopLive.ActionType.CLOSE -> {
                            val intent = Intent(requireContext(), WebViewActivity::class.java)
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
             * @param state - 'CREATE' or 'DESTROY'
             * */
            override fun onChangedPlayerStatus(isPipMode: Boolean, state: String) {
                super.onChangedPlayerStatus(isPipMode, state)
                Log.d(TAG, "isPipMode=$isPipMode, state=$state")
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
}