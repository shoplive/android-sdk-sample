package cloud.shoplive.sample.data

import android.content.Context

class SharedPreferenceStorage(private val context: Context): KeyValueStorage {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            "ShopLive-Preference",
            Context.MODE_PRIVATE
        )
    }

    override fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key,value).apply()
    }

    override fun getString(key: String, defaultValue: String?): String? = sharedPreferences.getString(key, defaultValue)

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key,value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int = sharedPreferences.getInt(key, defaultValue)
}