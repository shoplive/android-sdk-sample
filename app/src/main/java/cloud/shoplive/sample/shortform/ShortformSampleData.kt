package cloud.shoplive.sample.shortform

import android.content.Context
import cloud.shoplive.sdk.network.request.ShopLiveShortformTagSearchOperator
import cloud.shoplive.sdk.network.response.ShopLiveShortformCollectionResponse
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class ShortformSampleData(
    val title: String,
    val collectionData: ShopLiveShortformCollectionData,
    val response: ShopLiveShortformCollectionResponse?
) {
    companion object {
        suspend fun convertData(
            context: Context,
            title: String,
            data: ShopLiveShortformCollectionData
        ): ShortformSampleData {
            val response = suspendCoroutine<ShopLiveShortformCollectionResponse?> { continuation ->
                ShopLiveShortform.get(context, data) {
                    continuation.resume(it)
                }
            }
            return ShortformSampleData(title, data, response)
        }
    }
}