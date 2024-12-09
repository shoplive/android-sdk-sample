package cloud.shoplive.sample.shortform

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.IntDef
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_FULL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_HORIZONTAL
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_MAIN
import cloud.shoplive.sample.shortform.NativeShortformActivity.Companion.PAGE_SHORTS_VERTICAL
import cloud.shoplive.sdk.common.ShopLiveCommonError
import cloud.shoplive.sdk.common.extension.debugShopLiveLog
import cloud.shoplive.sdk.common.extension.showShareDialog
import cloud.shoplive.sdk.network.response.ShopLiveShortformCollectionResponse
import cloud.shoplive.sdk.network.response.ShopLiveShortformData
import cloud.shoplive.sdk.shorts.ShopLiveShortform
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionData
import cloud.shoplive.sdk.shorts.ShopLiveShortformCollectionListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformHandler
import cloud.shoplive.sdk.shorts.ShopLiveShortformMessageListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformPlayerEventCommand
import cloud.shoplive.sdk.shorts.ShopLiveShortformPreviewData
import cloud.shoplive.sdk.shorts.ShopLiveShortformProductListener
import cloud.shoplive.sdk.shorts.ShopLiveShortformShareData
import cloud.shoplive.sdk.shorts.ShopLiveShortformUrlListener
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

        val handler = object : ShopLiveShortformHandler() {
            override fun onError(context: Context, error: ShopLiveCommonError) {
                Toast.makeText(
                    context,
                    error.message ?: error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun getOnClickProductListener(): ShopLiveShortformProductListener {
                return ShopLiveShortformProductListener { context, data, product ->
                    // Something landing customer
                    ShopLiveShortform.hidePreview()
                    ShopLiveShortform.showPreview(
                        context as? Activity ?: return@ShopLiveShortformProductListener,
                        ShopLiveShortformPreviewData().apply {
                            shortsId = data?.shortsId
                            productId = product.productId
                            isMuted = ShortformOptionDialog.isMuted
                            isEnabledVolumeKey = ShortformOptionDialog.isEnabledVolumeKey
                            if (ShortformOptionDialog.maxCount > 0) {
                                maxCount = ShortformOptionDialog.maxCount
                            }
                        })
                }
            }

            override fun getOnClickBannerListener(): ShopLiveShortformUrlListener {
                return ShopLiveShortformUrlListener { context, _, url ->
                    // Something landing customer
                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onShare(context: Context, data: ShopLiveShortformShareData) {
                super.onShare(context, data)
                context.showShareDialog(data.title ?: return)
            }

            override fun onShortsAttached(data: ShopLiveShortformData) {
                super.onShortsAttached(data)
                data.toString().debugShopLiveLog()
            }

            override fun onShortsDetached(data: ShopLiveShortformData) {
                super.onShortsDetached(data)
                data.toString().debugShopLiveLog()
            }

            override fun onEvent(
                context: Context,
                messenger: ShopLiveShortformMessageListener?,
                command: String,
                payload: Map<String, Any?>
            ) {
                super.onEvent(context, messenger, command, payload)
                command.debugShopLiveLog()
                if (command == ShopLiveShortformPlayerEventCommand.DETAIL_SHORTFORM_MORE_ENDED.name) {
                    Toast.makeText(context, command, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@IntDef(value = [PAGE_SHORTS_MAIN, PAGE_SHORTS_VERTICAL, PAGE_SHORTS_HORIZONTAL, PAGE_SHORTS_FULL])
annotation class ShortFormPage