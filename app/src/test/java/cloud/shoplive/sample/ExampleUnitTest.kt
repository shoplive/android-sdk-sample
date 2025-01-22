package cloud.shoplive.sample

import cloud.shoplive.sample.data.KeyValueStorage
import cloud.shoplive.sample.data.SharedPreferenceStorage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun access_key_저장_후_알맞는_값_return () {

        //arrange
        val mockStorage = mockk<KeyValueStorage>()
        val preference = PreferencesUtilImpl(mockStorage)

        every { mockStorage.putString("access_key", "testKey") } just Runs
        every { mockStorage.getString("access_key") } returns "testKey"

        //Act
        preference.accessKey = "testKey"
        val result = preference.accessKey

        //Assert
        verify { mockStorage.putString("access_key", "testKey") }
        assertEquals("testKey", result)    }
}