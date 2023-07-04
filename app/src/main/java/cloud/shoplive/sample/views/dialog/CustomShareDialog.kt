package cloud.shoplive.sample.views.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import cloud.shoplive.sample.R

class CustomShareDialog(private val context: Context, private val shareUrl: String) :
    Dialog(context) {

    private val tvShareUrl: TextView by lazy {
        findViewById(R.id.tvShareUrl)
    }

    private val btCopy: View by lazy {
        findViewById(R.id.btCopy)
    }

    private val btKakao: View by lazy {
        findViewById(R.id.btKakao)
    }

    private val btLine: View by lazy {
        findViewById(R.id.btLine)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_share_dialog)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        tvShareUrl.text = shareUrl

        btCopy.setOnClickListener {
            Toast.makeText(
                context,
                "${context.getString(R.string.sample_copy_link)}!",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        btKakao.setOnClickListener {
            Toast.makeText(
                context,
                "${context.getString(R.string.sample_share_kakao)}!",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        btLine.setOnClickListener {
            Toast.makeText(
                context,
                "${context.getString(R.string.sample_share_line)}!",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }
}