package cloud.shoplive.sample

import android.content.Context
import android.content.SharedPreferences
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLivePIPRatio

object Options {

    private const val PREFERENCE_NAME = "sample_ui_preferences"
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = preferences?.edit()
    }

    fun playerNextAction(): ShopLive.ActionType {
        return when(preferences?.getInt("playerNextAction", 0)) {
            1 -> ShopLive.ActionType.KEEP
            2 -> ShopLive.ActionType.CLOSE
            else -> ShopLive.ActionType.PIP
        }
    }

    private fun pipRatioString(ratio: ShopLivePIPRatio): String {
        val result = when(ratio) {
            ShopLivePIPRatio.RATIO_1X1 -> "1:1"
            ShopLivePIPRatio.RATIO_1X2 -> "1:2"
            ShopLivePIPRatio.RATIO_2X3 -> "2:3"
            ShopLivePIPRatio.RATIO_3X4 -> "3:4"
            ShopLivePIPRatio.RATIO_9X16 -> "9:16"
        }
        return result
    }

    private fun playerNextActionString(): String {
        return when(preferences?.getInt("playerNextAction", 0)) {
            1 -> "KEEP"
            2 -> "CLOSE"
            else -> "PIP"
        }
    }

    fun saveUseCustomShareSheet(value: Boolean) {
        editor?.putBoolean("useCustomShareSheet", value)
        editor?.apply()
    }

    private fun useCustomShareSheet(): Boolean {
        return preferences?.getBoolean("useCustomShareSheet", false) == true
    }

    fun shareSchemeUrl(schemeUrl: String) {
        editor?.putString("schemeUrl", schemeUrl)
        editor?.apply()
    }

    fun shareSchemeUrl(): String? {
        return preferences?.getString("schemeUrl", "")
    }

    fun loadingProgressColor(hexColor: String) {
        editor?.putString("loadingHex", hexColor)
        editor?.apply()
    }

    fun loadingProgressColor(): String? {
        return preferences?.getString("loadingHex", "")
    }

    fun useLoadingImageAnimation(use: Boolean) {
        editor?.putBoolean("loadingImageAnimation", use)
        editor?.apply()
    }

    fun useLoadingImageAnimation(): Boolean {
        return preferences?.getBoolean("loadingImageAnimation", false) == true
    }

    fun useCustomFontChatInput(use: Boolean) {
        editor?.putBoolean("useCustomFontChatInput", use)
        editor?.apply()
    }

    fun useCustomFontChatInput(): Boolean {
        return preferences?.getBoolean("useCustomFontChatInput", false) == true
    }

    fun useCustomFontChatSendButton(use: Boolean) {
        editor?.putBoolean("useCustomFontChatSendButton", use)
        editor?.apply()
    }

    fun useCustomFontChatSendButton(): Boolean {
        return preferences?.getBoolean("useCustomFontChatSendButton", false) == true
    }

    fun setAutoCloseWhenAppDestroyed(isAutoClosed: Boolean) {
        editor?.putBoolean("isAutoClosed", isAutoClosed)
        editor?.apply()
    }

    fun isAutoCloseWhenAppDestroyed(): Boolean {
        return preferences?.getBoolean("isAutoClosed", true) == true
    }

    fun setMuteWhenPlayStart(isMute: Boolean) {
        editor?.putBoolean("isMuteWhenPlayStart", isMute)
        editor?.apply()
    }

    fun isMuteWhenPlayStart(): Boolean {
        return preferences?.getBoolean("isMuteWhenPlayStart", false) == true
    }

    fun setKeepAspectOnTabletPortrait(isKeep: Boolean) {
        editor?.putBoolean("isKeepAspectOnTabletPortrait", isKeep)
        editor?.apply()
    }

    fun isKeepAspectOnTabletPortrait(): Boolean {
        return preferences?.getBoolean("isKeepAspectOnTabletPortrait", false) == true
    }

    fun setEnterPipModeOnBackPressed(isEnterPipMode: Boolean) {
        editor?.putBoolean("isEnterPipModeOnBackPressed", isEnterPipMode)
        editor?.apply()
    }

    fun isEnterPipModeOnBackPressed(): Boolean {
        return preferences?.getBoolean("isEnterPipModeOnBackPressed", false) == true
    }

    fun setAutoResumeVideoOnCallEnded(isAutoResume: Boolean) {
        editor?.putBoolean("isAutoResumeVideoOnCallEnded", isAutoResume)
        editor?.apply()
    }

    fun isAutoResumeVideoOnCallEnded(): Boolean {
        return preferences?.getBoolean("isAutoResumeVideoOnCallEnded", true) == true
    }

    fun setKeepPlayVideoOnHeadphoneUnplugged(isKeep: Boolean) {
        editor?.putBoolean("isKeepPlayVideoOnHeadphoneUnplugged", isKeep)
        editor?.apply()
    }

    fun setKeepPlayVideoOnHeadphoneUnplugged(isKeep: Boolean, isMute: Boolean) {
        editor?.putBoolean("isKeepPlayVideoOnHeadphoneUnplugged", isKeep)
        editor?.putBoolean("isMuteVideoOnHeadphoneUnplugged", isMute)
        editor?.apply()
    }

    fun isKeepPlayVideoOnHeadphoneUnplugged(): Boolean {
        return preferences?.getBoolean("isKeepPlayVideoOnHeadphoneUnplugged", false) == true
    }

    fun isMuteVideoOnHeadphoneUnplugged(): Boolean {
        return preferences?.getBoolean("isMuteVideoOnHeadphoneUnplugged", false) == true
    }

    fun setPlayerScreenCaptureEnabled(isEnabled: Boolean) {
        editor?.putBoolean("isPlayerScreenCaptureEnabled", isEnabled)
        editor?.apply()
    }

    fun isPlayerScreenCaptureEnabled(): Boolean {
        return preferences?.getBoolean("isPlayerScreenCaptureEnabled", true) ?: true
    }

    fun setStatusBarTransparent(value: Boolean) {
        editor?.putBoolean("statusBarTransparent", value)
        editor?.apply()
    }

    fun isStatusBarTransparent(): Boolean {
        return preferences?.getBoolean("statusBarTransparent", false) ?: false
    }

    fun setNextActionOnHandleNavigation(type: ShopLive.ActionType) {
        editor?.putInt("nextActionOnHandleNavigation", type.value)
        editor?.apply()
    }

    fun getNextActionOnHandleNavigation(): ShopLive.ActionType {
        val type = preferences?.getInt("nextActionOnHandleNavigation", 0)
        type?.let {
            return ShopLive.ActionType.getType(it)
        }
        return ShopLive.ActionType.PIP
    }

    fun setPIPRatio(ratio: ShopLivePIPRatio) {
        editor?.putInt("pipRatio", ratio.value)
        editor?.apply()
    }

    fun getPIPRatio(): ShopLivePIPRatio {
        return when(preferences?.getInt("pipRatio", 4)) {
            0 -> ShopLivePIPRatio.RATIO_1X1
            1 -> ShopLivePIPRatio.RATIO_1X2
            2 -> ShopLivePIPRatio.RATIO_2X3
            3 -> ShopLivePIPRatio.RATIO_3X4
            else -> ShopLivePIPRatio.RATIO_9X16
        }
    }

    fun toString(context: Context): String {
        return "${context.getString(R.string.setting_category_player)}\n" +
                "• ${context.getString(R.string.preference_pipRatio_title)} : ${pipRatioString(ShopLive.getPIPRatio())}\n" +
                "• ${context.getString(R.string.setting_next_action_on_handle_navigation2)} : ${playerNextActionString()}\n" +
                "• ${context.getString(R.string.setting_player_status_bar_transparent)} : ${if (isStatusBarTransparent()) "Yes" else "No"}\n\n" +
                "${context.getString(R.string.setting_category_sound)}\n" +
                "• ${context.getString(R.string.preference_mute_start_title)} : ${if (isMuteWhenPlayStart()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_auto_play)}\n" +
                "• ${context.getString(R.string.preference_headset_title)} : ${if (isKeepPlayVideoOnHeadphoneUnplugged()) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_mute_title)} : ${if (isMuteVideoOnHeadphoneUnplugged()) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_call_title2)} : ${if (isAutoResumeVideoOnCallEnded()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_share)}\n" +
                "• ${context.getString(R.string.preference_share_url_title)} : ${shareSchemeUrl()}\n" +
                "• ${context.getString(R.string.custom)} : ${if (useCustomShareSheet()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_loading_progress)}\n" +
                "• ${context.getString(R.string.preference_loading_progress_title)} : ${loadingProgressColor()}\n" +
                "• ${context.getString(R.string.preference_loading_image_animation_title)} : ${if (useLoadingImageAnimation()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_chat_font)}\n" +
                "• ${context.getString(R.string.preference_chat_input_font_title)} : ${if (useCustomFontChatInput()) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_chat_send_font_title)} : ${if (useCustomFontChatSendButton()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_exit)}\n" +
                "• ${context.getString(R.string.preference_pipModeOnBackPressed_title)} : ${if (isEnterPipModeOnBackPressed()) "Enabled" else "Disabled"}\n"+
                "• ${context.getString(R.string.preference_autoClose_title)} : ${if (isAutoCloseWhenAppDestroyed()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_secure)}\n" +
                "• ${context.getString(R.string.preference_player_screen_capture_enabled_title)} : ${if (isPlayerScreenCaptureEnabled()) "Enabled" else "Disabled"}"
    }

}