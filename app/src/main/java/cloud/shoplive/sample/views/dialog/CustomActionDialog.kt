package cloud.shoplive.sample.views.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cloud.shoplive.sample.R
import cloud.shoplive.sdk.ShopLive
import cloud.shoplive.sdk.ShopLiveHandlerCallback
import org.json.JSONObject

class CustomActionDialog(
    private val context: Context,
    private val id: String,
    private val type: String,
    private val payload: JSONObject,
    private val callback: ShopLiveHandlerCallback
) : Dialog(context) {

    private val tvId: TextView by lazy {
        findViewById(R.id.tvId)
    }

    private val tvType: TextView by lazy {
        findViewById(R.id.tvType)
    }

    private val tvPayload: TextView by lazy {
        findViewById(R.id.tvPayload)
    }

    private val btSuccess: View by lazy {
        findViewById(R.id.btSuccess)
    }

    private val btFail: View by lazy {
        findViewById(R.id.btFail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_action_dialog)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setCancelable(false)

        tvId.text = context.getString(R.string.sample_custom_action_id, id)
        tvType.text = context.getString(R.string.sample_custom_action_type, type)
        tvPayload.text =
            context.getString(R.string.sample_custom_action_payload, payload.toString())

        btSuccess.setOnClickListener {
            callback.customActionResult(
                true,
                context.getString(R.string.alert_custom_action_success),
                ShopLive.CouponPopupStatus.HIDE,
                ShopLive.CouponPopupResultAlertType.TOAST
            )
            dismiss()
        }

        btFail.setOnClickListener {
            callback.customActionResult(
                false,
                context.getString(R.string.alert_custom_action_fail),
                ShopLive.CouponPopupStatus.SHOW,
                ShopLive.CouponPopupResultAlertType.ALERT
            )
            dismiss()
        }
    }
}