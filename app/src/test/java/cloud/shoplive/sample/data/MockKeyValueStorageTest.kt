package cloud.shoplive.sample.data

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MockKeyValueStorageTest {
    private val context: Context = mockk(relaxed = true)
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    val editor = mockk<SharedPreferences.Editor>(relaxed = true)
    val testIntKey = "testInt"
    val testStringKey = "testString"
    val testString = "test"

    init {
        every {
            context.getSharedPreferences(
                "ShopLive-Preference",
                Context.MODE_PRIVATE
            )
        } returns sharedPreferences
        every { sharedPreferences.getInt(testIntKey, 0) } returns 123
        every { sharedPreferences.getString(testStringKey, null) } returns testString
        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(testIntKey, 123) } returns editor
        every { editor.putString(testStringKey, testString) } returns editor
    }

    @Test
    fun keyValueMockTest() {
        val storage = SharedPreferenceStorage(context)

        assertEquals(123, storage.getInt(testIntKey))
        assertEquals(testString, storage.getString(testStringKey))

        storage.putInt(testIntKey, 123)
        storage.putString(testStringKey, testString)

        verify { editor.putInt(testIntKey, 123) }
        verify { editor.putString(testStringKey, "test") }
    }
}