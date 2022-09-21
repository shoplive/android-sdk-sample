package cloud.shoplive.sample.fragment

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
import cloud.shoplive.sample.extension.isTablet
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLivePIPRatio

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, null)

        Options.init(requireContext())

        val pipRatioPref: Preference? = findPreference(getString(R.string.preference_pip_ratio_key))
        pipRatioPref?.let {
            val ratioArray = resources.getStringArray(R.array.ratio)
            val index = when(Options.getPIPRatio()) {
                ShopLivePIPRatio.RATIO_1X1  -> 0
                ShopLivePIPRatio.RATIO_1X2  -> 1
                ShopLivePIPRatio.RATIO_2X3  -> 2
                ShopLivePIPRatio.RATIO_3X4  -> 3
                ShopLivePIPRatio.RATIO_9X16 -> 4
            }
            pipRatioPref.title = "${getString(R.string.preference_pip_ratio_title)}\n(${ratioArray[index]})"
        }

        val playerNextActionPref: Preference? = findPreference(getString(R.string.preference_next_action_key))
        playerNextActionPref?.let {
            val nextActionArray = resources.getStringArray(R.array.playerNextAction)
            val action = Options.playerNextAction()
            playerNextActionPref.title = "${getString(R.string.preference_next_action_title)}\n(${nextActionArray[action.value]})"
        }

        val shareUrlPref: Preference? = findPreference(getString(R.string.preference_share_url_key))
        shareUrlPref?.let {
            val schemeUrl = Options.shareSchemeUrl()
            shareUrlPref.summary = if (schemeUrl.isNullOrEmpty()) getString(R.string.preference_share_url_summary) else schemeUrl
        }

        val loadingHexPref: Preference? = findPreference(getString(R.string.preference_loading_progress_key))
        loadingHexPref?.let {
            val hex = Options.loadingProgressColor()
            loadingHexPref.summary = if (hex.isNullOrEmpty()) getString(R.string.preference_loading_progress_summary) else hex
        }

        val tabletAspectPref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_tablet_aspect_key))
        tabletAspectPref?.isEnabled = isTablet()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            Options.setAutoResumeVideoOnCallEnded(true)
            val callPref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_call_key))
            callPref?.isChecked = true
        }

        // 방송 자동종료 옵션(기본값 true)
        val autoClosePref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_auto_close_key))
        autoClosePref?.isChecked = Options.isAutoCloseWhenAppDestroyed()
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when(preference?.key) {
            getString(R.string.preference_pip_ratio_key) -> {
                showPipRatioDialog()
            }
            getString(R.string.preference_share_url_key) -> {
                showShareUrlInputDialog()
            }
            getString(R.string.preference_loading_progress_key) -> {
                showLoadingProgressColorInputDialog()
            }
            getString(R.string.preference_next_action_key) -> {
                showPlayerNextActionDialog()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {
            getString(R.string.preference_mute_key) -> {
                sharedPreferences?.let {
                    val isKeepPlayVideo = Options.isKeepPlayVideoOnHeadphoneUnplugged()
                    if (!isKeepPlayVideo) {
                        val mutePref = findPreference<SwitchPreferenceCompat>(getString(R.string.preference_mute_key))
                        mutePref?.isChecked = false
                        Toast.makeText(requireContext(), getString(R.string.toast_headset_mute_option), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
            getString(R.string.preference_call_key) -> {
                sharedPreferences?.let {
                    val value = it.getBoolean(key, true)
                    if (!value) {
                        requestReadPhoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                    }
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
            val pipPref: Preference? = findPreference(getString(R.string.preference_pip_ratio_key))
            pipPref?.let {
                pipPref.title = "${getString(R.string.preference_pip_ratio_title)}(${ratioTextArray[which]})"
                Options.setPIPRatio(ratioArray[which])
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showShareUrlInputDialog() {
        val inflater = requireContext().getSystemService(Application.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.input_dialog, null)
        val etInput = view.findViewById<EditText>(R.id.etInput)
        etInput.setText(Options.shareSchemeUrl())
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
                Options.shareSchemeUrl(schemeUrl)
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
        etInput.setText(Options.loadingProgressColor())
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
                Options.loadingProgressColor(hexColor)
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
            val nextActonPref: Preference? = findPreference(getString(R.string.preference_next_action_key))
            nextActonPref?.let {
                nextActonPref.title = "${getString(R.string.preference_next_action_title)} (${nextActionTextArray[which]})"
                Options.setNextActionOnHandleNavigation(actionTypes[which])
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private val requestReadPhoneStatePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_PHONE_STATE)){
                Options.setAutoResumeVideoOnCallEnded(true)
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