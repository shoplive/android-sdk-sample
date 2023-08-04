package cloud.shoplive.sample

import android.content.Context

interface PreferencesUtil {
    var accessKey: String?
    var shortformCardType: Int
}

class PreferencesUtilImpl(private val context: Context) : PreferencesUtil {
    companion object {
        private const val ACCESS_KEY = "access_key"
        private const val SHORT_FORM_CARD_TYPE = "short_form_card_type"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            "ShopLive-Preference",
            Context.MODE_PRIVATE
        )
    }

    override var accessKey: String?
        get() = sharedPreferences?.getString(ACCESS_KEY, null)
        set(value) {
            sharedPreferences?.edit()?.let {
                it.putString(ACCESS_KEY, value)
                it.commit()
            }
        }

    override var shortformCardType: Int
        get() = sharedPreferences?.getInt(SHORT_FORM_CARD_TYPE, 0) ?: 0
        set(value) {
            sharedPreferences?.edit()?.let {
                it.putInt(SHORT_FORM_CARD_TYPE, value)
                it.commit()
            }
        }
}