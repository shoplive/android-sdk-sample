package cloud.shoplive.sample.data

import android.content.Context

class SharedPreferenceStorage(private val context: Context) : KeyValueStorage {
    companion object {
         private const val PREFERENCE_NAME = "ShopLive-Preference"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPreferences.getString(key, defaultValue)

    override fun putInt(key: String, value: Int?) {
        sharedPreferences.edit().putInt(key, value ?: 0).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}