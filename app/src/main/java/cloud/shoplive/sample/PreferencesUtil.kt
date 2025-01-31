package cloud.shoplive.sample

import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sdk.ShopLiveUser
import com.google.gson.Gson

interface PreferencesUtil {
    var accessKey: String?
    var campaignKey: String?
    var shortFormCardType: Int
    var authType: Int
    var jwt: String?
    var user: ShopLiveUser?
}

//todo ; repository로 명칭 변경 + datalayer 분리
class PreferencesUtilImpl(private val keyValueStorage: KeyValueStorage) : PreferencesUtil {
    companion object {
        private const val ACCESS_KEY = "access_key"
        private const val CAMPAIGN_KEY = "campaign_key"
        private const val AUTH_TYPE = "authType"
        private const val JWT = "jwt"
        private const val USER_KEY = "user"
        private const val SHORT_FORM_CARD_TYPE = "short_form_card_type"
        const val PREFERENCE_NAME = "sample_ui_preferences"
    }

    override var accessKey: String?
        get() = keyValueStorage.getString(ACCESS_KEY, null)
        set(value) {
            keyValueStorage.putString(ACCESS_KEY, value)
        }

    override var shortFormCardType: Int
        get() = keyValueStorage.getInt(SHORT_FORM_CARD_TYPE, 0) ?: 0
        set(value) {
            keyValueStorage.putInt(SHORT_FORM_CARD_TYPE, value)
        }

    override var campaignKey: String?
        get() = keyValueStorage.getString(CAMPAIGN_KEY, null)
        set(value) {
            keyValueStorage.getString(CAMPAIGN_KEY, value)
        }

    override var authType: Int
        get() = keyValueStorage.getInt(AUTH_TYPE, UserType.GUEST.ordinal)
        set(value) {
            keyValueStorage.putInt(AUTH_TYPE, value)
        }

    override var jwt: String?
        get() = keyValueStorage.getString(JWT, null)
        set(value) {
            keyValueStorage.putString(JWT, value)
        }

    override var user: ShopLiveUser?
        get() {
            val user = keyValueStorage.getString(USER_KEY, null) ?: return null
            return if (user.isNotEmpty()) Gson().fromJson(
                user,
                ShopLiveUser::class.java
            ) else null
        }
        set(value) {
            value?.let {
                val gson = Gson()
                val userValue = gson.toJson(value)
                keyValueStorage.putString(USER_KEY, userValue)
            } ?: run {
                keyValueStorage.putString(USER_KEY, null)
            }
        }
}

enum class UserType {
    USER,
    JWT,
    GUEST
}