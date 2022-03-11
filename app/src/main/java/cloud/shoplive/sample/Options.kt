package cloud.shoplive.sample

import android.content.Context
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLivePIPRatio

object Options {

    private const val PREFERENCE_NAME = "sample_ui_preferences"

    fun saveUseCustomShareSheet(context: Context, value: Boolean) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("useCustomShareSheet", value)
        editor.apply()
    }

    fun useCustomShareSheet(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean("useCustomShareSheet", false)
    }

    fun shareSchemeUrl(context: Context, schemeUrl: String) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("schemeUrl", schemeUrl)
        editor.apply()
    }

    fun shareSchemeUrl(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString("schemeUrl", "")
    }

    fun loadingProgressColor(context: Context, hexColor: String) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("loadingHex", hexColor)
        editor.apply()
    }

    fun loadingProgressColor(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString("loadingHex", "#FFFFFF")
    }

    fun useLoadingImageAnimation(context: Context, use: Boolean) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("loadingImageAnimation", use)
        editor.apply()
    }

    fun useLoadingImageAnimation(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean("loadingImageAnimation", false)
    }

    fun useCustomFontChatInput(context: Context, use: Boolean) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("useCustomFontChatInput", use)
        editor.apply()
    }

    fun useCustomFontChatInput(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean("useCustomFontChatInput", false)
    }

    fun useCustomFontChatSendButton(context: Context, use: Boolean) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("useCustomFontChatSendButton", use)
        editor.apply()
    }

    fun useCustomFontChatSendButton(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean("useCustomFontChatSendButton", false)
    }

    fun playerNextAction(context: Context, type: Int) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("playerNextAction", type)
        editor.apply()
    }

    fun playerNextAction(context: Context): Int {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt("playerNextAction", 0)
    }

    private fun nextAction(context: Context, type: Int): String {
        val nextActionArray = context.resources.getStringArray(R.array.playerNextAction)
        return nextActionArray[type]
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

    fun toString(context: Context): String {
        return "${context.getString(R.string.setting_category_player)}\n" +
                "• ${context.getString(R.string.preference_pipRatio_title)} : ${pipRatioString(ShopLive.getPIPRatio())}\n" +
                "• ${context.getString(R.string.setting_next_action_on_handle_navigation2)} : ${nextAction(context, playerNextAction(context))}\n\n" +
                "${context.getString(R.string.setting_category_auto_play)}\n" +
                "• ${context.getString(R.string.preference_headset_title)} : ${if (ShopLive.isKeepPlayVideoOnHeadphoneUnplugged()) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_mute_title)} : ${if (ShopLive.isMuteVideoOnHeadphoneUnplugged()) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_call_title2)} : ${if (ShopLive.isAutoResumeVideoOnCallEnded()) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_share)}\n" +
                "• ${context.getString(R.string.preference_share_url_title)} : ${shareSchemeUrl(context)}\n" +
                "• ${context.getString(R.string.custom)} : ${if (useCustomShareSheet(context)) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_loading_progress)}\n" +
                "• ${context.getString(R.string.preference_loading_progress_title)} : ${loadingProgressColor(context)}\n" +
                "• ${context.getString(R.string.preference_loading_image_animation_title)} : ${if (useLoadingImageAnimation(context)) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_chat_font)}\n" +
                "• ${context.getString(R.string.preference_chat_input_font_title)} : ${if (useCustomFontChatInput(context)) "Enabled" else "Disabled"}\n" +
                "• ${context.getString(R.string.preference_chat_send_font_title)} : ${if (useCustomFontChatSendButton(context)) "Enabled" else "Disabled"}\n\n" +
                "${context.getString(R.string.setting_category_exit)}\n" +
                "• ${context.getString(R.string.preference_pipModeOnBackPressed_title)} : ${if (ShopLive.isEnterPipModeOnBackPressed()) "Enabled" else "Disabled"}\n"+
                "• ${context.getString(R.string.preference_autoClose_title)} : ${if (ShopLive.isAutoCloseWhenAppDestroyed()) "Enabled" else "Disabled"}"
    }

}