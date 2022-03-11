package cloud.shoplive.sample.options

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cloud.shoplive.sample.BuildConfig
import cloud.shoplive.sample.Options
import cloud.shoplive.sample.R
import cloud.shoplive.sample.isTablet
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLivePIPRatio

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, null)

        val pipRatioPref: Preference? = findPreference(getString(R.string.preference_pipRatio_key))
        pipRatioPref?.let {
            var index: Int = 4
            val ratioArray = resources.getStringArray(R.array.ratio)
            val ratio = ShopLive.getPIPRatio()
            ratio?.let {
                index = when(it) {
                    ShopLivePIPRatio.RATIO_1X1  -> 0
                    ShopLivePIPRatio.RATIO_1X2  -> 1
                    ShopLivePIPRatio.RATIO_2X3  -> 2
                    ShopLivePIPRatio.RATIO_3X4  -> 3
                    ShopLivePIPRatio.RATIO_9X16 -> 4
                }
            }
            pipRatioPref.title = "${getString(R.string.preference_pipRatio_title)}\n(${ratioArray[index]})"
        }

        val playerNextActionPref: Preference? = findPreference(getString(R.string.preference_nextAction_key))
        playerNextActionPref?.let {
            val nextActionArray = resources.getStringArray(R.array.playerNextAction)
            val action = Options.playerNextAction(requireContext())
            playerNextActionPref.title = "${getString(R.string.preference_nextAction_title)}\n(${nextActionArray[action]})"
        }

        val shareUrlPref: Preference? = findPreference(getString(R.string.preference_share_url_key))
        shareUrlPref?.let {
            val schemeUrl = Options.shareSchemeUrl(requireContext())
            shareUrlPref.summary = if (schemeUrl.isNullOrEmpty()) getString(R.string.preference_share_url_summary) else schemeUrl
        }

        val loadingHexPref: Preference? = findPreference(getString(R.string.preference_loading_progress_key))
        loadingHexPref?.let {
            val hex = Options.loadingProgressColor(requireContext())
            loadingHexPref.summary = if (hex.isNullOrEmpty()) getString(R.string.preference_loading_progress_summary) else hex
        }

        val tabletAspectPref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_tabletAspect_key))
        tabletAspectPref?.isEnabled = isTablet()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ShopLive.setAutoResumeVideoOnCallEnded(true)
            val callPref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_call_key))
            callPref?.isChecked = true
        }

        // 방송 자동종료 옵션(기본값 true)
        val autoClosePref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_autoClose_key))
        autoClosePref?.isChecked = ShopLive.isAutoCloseWhenAppDestroyed()
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when(preference?.key!!) {
            getString(R.string.preference_pipRatio_key) -> {
                showPipRatioDialog()
            }
            getString(R.string.preference_share_url_key) -> {
                showShareUrlInputDialog()
            }
            getString(R.string.preference_loading_progress_key) -> {
                showLoadingProgressColorInputDialog()
            }
            getString(R.string.preference_nextAction_key) -> {
                showPlayerNextActionDialog()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {
            getString(R.string.preference_headset_key) -> {
                sharedPreferences?.let {
                    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(it.getBoolean(key, false))
                    if (!it.getBoolean(key, false)) {
                        ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(false, false)
                        val mutePref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_mute_key))
                        mutePref?.isChecked = false
                    }
                }
            }
            getString(R.string.preference_mute_key) -> {
                sharedPreferences?.let {
                    val isKeepPlayVideo = ShopLive.isKeepPlayVideoOnHeadphoneUnplugged()
                    if (!isKeepPlayVideo && it.getBoolean(key, false) ) {
                        val mutePref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_mute_key))
                        mutePref?.isChecked = false
                        Toast.makeText(requireContext(), getString(R.string.toast_headset_mute_option), Toast.LENGTH_SHORT).show()
                        return
                    }
                    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(isKeepPlayVideo, it.getBoolean(key, false))
                }
            }
            getString(R.string.preference_call_key) -> {
                sharedPreferences?.let {
                    val value = it.getBoolean(key, true)
                    if (!value) {
                        requestReadPhoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                    }
                    ShopLive.setAutoResumeVideoOnCallEnded(value)
                }
            }
            getString(R.string.preference_custom_share_key) -> {
                sharedPreferences?.let {
                    val value = it.getBoolean(key, true)
                    Options.saveUseCustomShareSheet(requireContext(), value)
                }
            }
            getString(R.string.preference_loading_image_animation_key) -> {
                sharedPreferences?.let {
                    val value = it.getBoolean(key, false)
                    val loadingProgressColorPref = findPreference<Preference>(getString(R.string.preference_loading_progress_key))
                    loadingProgressColorPref?.isEnabled = !value

                    Options.useLoadingImageAnimation(requireContext(), value)
                }
            }
            getString(R.string.preference_chat_input_font_key) -> {
                sharedPreferences?.let {
                    Options.useCustomFontChatInput(requireContext(), it.getBoolean(key, false))
                }
            }
            getString(R.string.preference_chat_send_font_key) -> {
                sharedPreferences?.let {
                    Options.useCustomFontChatSendButton(requireContext(), it.getBoolean(key, false))
                }
            }
            getString(R.string.preference_pipModeOnBackPressed_key) -> {
                sharedPreferences?.let {
                    ShopLive.setEnterPipModeOnBackPressed(it.getBoolean(key, false))
                }
            }
            getString(R.string.preference_autoClose_key) -> {
                sharedPreferences?.let {
                    ShopLive.setAutoCloseWhenAppDestroyed(it.getBoolean(key, true))
                }
            }
            getString(R.string.preference_tabletAspect_key) -> {
                sharedPreferences?.let {
                    ShopLive.setKeepAspectOnTabletPortrait(it.getBoolean(key, false))
                }
            }
        }
    }

    private fun showPipRatioDialog() {
        val ratioArray = arrayOf(
            ShopLivePIPRatio.RATIO_1X1,
            ShopLivePIPRatio.RATIO_1X2,
            ShopLivePIPRatio.RATIO_2X3,
            ShopLivePIPRatio.RATIO_3X4,
            ShopLivePIPRatio.RATIO_9X16)

        val ratioTextArray = resources.getStringArray(R.array.ratio)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.setting_pipRatio_dialog_title))
        builder.setItems(ratioTextArray) { _, which ->
            val pipPref: Preference? = findPreference(getString(R.string.preference_pipRatio_key))
            pipPref?.let {
                pipPref.title = "${getString(R.string.preference_pipRatio_title)}(${ratioTextArray[which]})"
                ShopLive.setPIPRatio(ratioArray[which])
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showShareUrlInputDialog() {
        val inflater = requireContext().getSystemService(Application.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.input_dialog, null)
        val etInput = view.findViewById<EditText>(R.id.etInput)
        etInput.setText(Options.shareSchemeUrl(requireContext()))
        etInput.hint = getString(R.string.hint_share_scheme_url)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.setting_share))
        builder.setView(view)

        builder.setPositiveButton(getString(R.string.dialog_confirm)) { dialog, _ ->
            val schemeUrlPref: Preference? = findPreference(getString(R.string.preference_share_url_key))
            schemeUrlPref?.let {
                val schemeUrl = etInput.text.toString()
                if (schemeUrl.isEmpty()) {
                    schemeUrlPref.summary = getString(R.string.preference_share_url_summary)
                } else {
                    schemeUrlPref.summary = etInput.text.toString()
                }
                Options.shareSchemeUrl(requireContext(), schemeUrl)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showLoadingProgressColorInputDialog() {
        val inflater = requireContext().getSystemService(Application.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.input_dialog, null)
        val etInput = view.findViewById<EditText>(R.id.etInput)
        etInput.setText(Options.loadingProgressColor(requireContext()))
        etInput.hint = getString(R.string.hint_loading_progress_hex)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.setting_loading_progress_color))
        builder.setView(view)

        builder.setPositiveButton(getString(R.string.dialog_confirm)) { dialog, _ ->
            val loadingProgressColorPref: Preference? = findPreference(getString(R.string.preference_loading_progress_key))
            loadingProgressColorPref?.let {
                val hexColor = etInput.text.toString()
                if (hexColor.isEmpty()) {
                    loadingProgressColorPref.summary = getString(R.string.preference_loading_progress_summary)
                } else {
                    loadingProgressColorPref.summary = etInput.text.toString()
                }
                Options.loadingProgressColor(requireContext(), hexColor)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showPlayerNextActionDialog() {
        val actionTypes = arrayOf(
            ShopLive.ActionType.PIP,
            ShopLive.ActionType.KEEP,
            ShopLive.ActionType.CLOSE
        )

        val nextActionTextArray = resources.getStringArray(R.array.playerNextAction)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.setting_next_action_on_handle_navigation))
        builder.setItems(nextActionTextArray) { _, which ->
            val nextActonPref: Preference? = findPreference(getString(R.string.preference_nextAction_key))
            nextActonPref?.let {
                nextActonPref.title = "${getString(R.string.preference_nextAction_title)} (${nextActionTextArray[which]})"
                ShopLive.setNextActionOnHandleNavigation(actionTypes[which])
                Options.playerNextAction(requireContext(), which)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private val requestReadPhoneStatePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_PHONE_STATE)){
                ShopLive.setAutoResumeVideoOnCallEnded(true)
                val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alert, null)
                val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
                tvMessage.text = getString(R.string.alert_call_permission)

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                builder.setPositiveButton("OK") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                    startActivity(intent)
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

}