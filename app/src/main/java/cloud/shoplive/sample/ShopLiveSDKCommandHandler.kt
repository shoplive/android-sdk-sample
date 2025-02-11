package cloud.shoplive.sample

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import cloud.shoplive.sample.core.SellerStoreData
import cloud.shoplive.sample.core.SellerSubscriptionData
import cloud.shoplive.sample.views.login.LoginActivity
import cloud.shoplive.sdk.ShopLive
import com.google.gson.Gson
import org.json.JSONObject

class ShopLiveSDKCommandHandler(
    private val activity: Activity,
    private val loginLauncher: ActivityResultLauncher<Intent>
) {

    fun commandHandler(command: String, data: JSONObject) {
        when (command) {
            LOGIN_REQUIRED -> showLoginRequiredDialog()
            CLICK_PRODUCT_DETAIL -> {}
            CLICK_PRODUCT_CART -> showDialog(title = command, message = data.toString())
            ON_SUCCESS_CAMPAIGN_JOIN -> {}
            EVENT_DEEPLINK -> showDialog(title = command, message = data.toString())
            CLICK_PRODUCT_BANNER_LINK,
            CLICK_PRODUCT_BANNER_COUPON -> showDialog(
                title = command,
                message = data.toString(),
                positiveButtonLabel = activity.getString(R.string.bt_ok)
            )

            CLICK_BACK_BUTTON -> ShopLive.close()
            ON_CLICK_BRAND_FAVORITE_BUTTON -> likeBrand(data)
            ON_CHANGED_BRAND_FAVORITE -> showToast("$ON_CHANGED_BRAND_FAVORITE : ${data.getString("identifier")}")
            ON_RECEIVED_SELLER_CONFIG -> setSellerConfig()
            ON_CLICK_VIEW_SELLER_STORE -> {
                Gson().fromJson(data.toString(), SellerStoreData::class.java)
                    ?.let { sellerStoreData ->
                        showToast("$ON_CLICK_VIEW_SELLER_STORE : ${sellerStoreData.seller?.storeUrl ?: return}")
                    }
            }

            ON_CLICK_SELLER_SUBSCRIPTION -> {
                Gson().fromJson(data.toString(), SellerSubscriptionData::class.java)
                    ?.let { sellerSubscriptionData ->
                        subscribeSeller(sellerSubscriptionData)
                    }
            }
        }
    }

    private fun likeBrand(getLikedJSONData: JSONObject) {
        val isFavorite = getLikedJSONData.getBoolean("favorite")
        val identifier = getLikedJSONData.getString("identifier")
        val brandFavorite = mapOf(
            Pair("identifier", identifier),
            Pair("favorite", !isFavorite)
        )
        ShopLive.sendCommandMessage(
            SET_BRAND_FAVORITE,
            brandFavorite
        )
        showToast("$ON_CLICK_BRAND_FAVORITE_BUTTON : ${!isFavorite}")
    }

    private fun setSellerConfig() {
        val sellerSavedData = mapOf(
            Pair("saved", true)
        )
        ShopLive.sendCommandMessage(
            SET_SELLER_SAVED_STATE,
            sellerSavedData
        )
    }

    private fun subscribeSeller(sellerSubscriptionData: SellerSubscriptionData) {
        val sellerSavedData = mapOf(
            Pair("saved", !sellerSubscriptionData.saved)
        )
        ShopLive.sendCommandMessage(
            SET_SELLER_SAVED_STATE,
            sellerSavedData
        )
        showToast("$SET_SELLER_SAVED_STATE : ${!sellerSubscriptionData.saved}")
        Toast.makeText(activity, MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private fun showLoginRequiredDialog() {
        activity.let {
            AlertDialog.Builder(it).apply {
                setMessage(context.getString(R.string.alert_need_login))
                setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
                    ShopLive.startPictureInPicture()
                    loginLauncher.launch(LoginActivity.buildIntent(context))
                    dialog.dismiss()
                }
                setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            }.run {
                this.create().show()
            }
        }
    }

    private fun showDialog(
        title: String,
        message: String,
        positiveButtonLabel: String? = activity.getString(R.string.confirm)
    ) {
        val builder = activity.let { AlertDialog.Builder(it) }
        builder.let { dialogBuilder ->
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton(positiveButtonLabel) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: Dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    private fun showToast(message: String?) {
        val layerToastData = mapOf(
            Pair(
                "message",
                "$message"
            ),
            Pair("duration", 1000),
            Pair("position", "CENTER")
        )
        ShopLive.sendCommandMessage(
            SHOW_LAYER_TOAST,
            layerToastData,
        )
    }

    companion object {
        private const val LOGIN_REQUIRED = "LOGIN_REQUIRED"
        private const val CLICK_PRODUCT_DETAIL = "CLICK_PRODUCT_DETAIL"
        private const val CLICK_PRODUCT_CART = "CLICK_PRODUCT_CART"
        private const val ON_SUCCESS_CAMPAIGN_JOIN = "ON_SUCCESS_CAMPAIGN_JOIN"
        private const val EVENT_DEEPLINK = "EVENT_DEEPLINK"
        private const val CLICK_PRODUCT_BANNER_LINK = "CLICK_PRODUCT_BANNER_LINK"
        private const val CLICK_PRODUCT_BANNER_COUPON = "CLICK_PRODUCT_BANNER_COUPON"
        private const val CLICK_BACK_BUTTON = "CLICK_BACK_BUTTON"
        private const val ON_CLICK_BRAND_FAVORITE_BUTTON = "ON_CLICK_BRAND_FAVORITE_BUTTON"
        private const val SET_BRAND_FAVORITE = "SET_BRAND_FAVORITE"
        private const val SHOW_LAYER_TOAST = "SHOW_LAYER_TOAST"
        private const val ON_CHANGED_BRAND_FAVORITE = "ON_CHANGED_BRAND_FAVORITE"
        private const val ON_RECEIVED_SELLER_CONFIG = "ON_RECEIVED_SELLER_CONFIG"
        private const val ON_CLICK_VIEW_SELLER_STORE = "ON_CLICK_VIEW_SELLER_STORE"
        private const val ON_CLICK_SELLER_SUBSCRIPTION = "ON_CLICK_SELLER_SUBSCRIPTION"
        private const val SET_SELLER_SAVED_STATE = "SET_SELLER_SAVED_STATE"
        private const val MESSAGE = "MESSAGE"
    }
}