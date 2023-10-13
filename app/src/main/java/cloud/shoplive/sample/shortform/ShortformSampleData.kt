package cloud.shoplive.sample.shortform

import android.content.Context
import androidx.annotation.IntDef
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_FULL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_HORIZONTAL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_MAIN
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_VERTICAL
import cloud.shoplive.sdk.network.response.ShopLiveShortformCollectionResponse
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class ShortformSampleData(
    val title: String,
    val collectionData: ShopLiveShortformCollectionData,
    val response: ShopLiveShortformCollectionResponse
) {
    companion object {
        suspend fun getData(
            context: Context,
            title: String,
            data: ShopLiveShortformCollectionData
        ): ShortformSampleData {
            val response = suspendCoroutine<ShopLiveShortformCollectionResponse> { continuation ->
                ShopLiveShortform.get(context, data, object : ShopLiveShortformCollectionListener {
                    override fun onData(data: ShopLiveShortformCollectionResponse) {
                        continuation.resume(data)
                    }

                    override fun onError(e: Exception) {
                        continuation.resumeWithException(e)
                    }
                })
            }
            return ShortformSampleData(title, data, response)
        }
    }
}

@IntDef(value = [PAGE_SHORTS_MAIN, PAGE_SHORTS_VERTICAL, PAGE_SHORTS_HORIZONTAL, PAGE_SHORTS_FULL])
annotation class ShortFormPage