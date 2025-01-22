package cloud.shoplive.sample

import cloud.shoplive.sample.data.KeyValueStorage

interface PreferencesUtil {
    var accessKey: String?
    var shortFormCardType: Int
}

class PreferencesUtilImpl(private val keyValueStorage: KeyValueStorage) : PreferencesUtil {
    companion object {
        private const val ACCESS_KEY = "access_key"
        private const val SHORT_FORM_CARD_TYPE = "short_form_card_type"
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
}