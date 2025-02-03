package cloud.shoplive.sample.data

interface KeyValueStorage {
    fun putString(key: String, value: String?)
    fun getString(key: String, defaultValue: String? = null): String?
    fun putInt(key: String, value: Int? = null)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun remove(key: String)
}