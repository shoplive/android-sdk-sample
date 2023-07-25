package cloud.shoplive.sample

import android.content.Context
import cloud.shoplive.sdk.ShopLiveUser
import com.google.gson.Gson

object CampaignSettings {

    private const val PREFERENCE_NAME = "sample_ui_preferences"

    fun setAccessKey(context: Context, key: String?) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString("accessKey", if (key.isNullOrEmpty()) null else key)
        editor.apply()
    }

    fun accessKey(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString("accessKey", null)
    }

    fun setCampaignKey(context: Context, key: String?) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("campaignKey", if (key.isNullOrEmpty()) null else key)
        editor.apply()
    }

    fun campaignKey(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString("campaignKey", null)
    }

    fun user(context: Context, user: ShopLiveUser?) {
        val gson = Gson()
        val userValue = gson.toJson(user)
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        editor?.putString("user", userValue)
        editor?.apply()
    }

    fun user(context: Context): ShopLiveUser? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val userJson = preferences?.getString("user", "")
        return Gson().fromJson(userJson, ShopLiveUser::class.java)
    }

    fun jwt(context: Context, token: String) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("jwt", if (token.isEmpty()) null else token)
        editor.apply()
    }

    fun jwt(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString("jwt", null)
    }

    enum class UserType {
        USER,
        JWT,
        GUEST
    }
    /**
     * @param type - 0:user, 1:token, 2:guest
     * */
    fun authType(context: Context, type: Int) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("authType", type)
        editor.apply()
    }

    fun authType(context: Context): Int {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt("authType", UserType.GUEST.ordinal)
    }


}