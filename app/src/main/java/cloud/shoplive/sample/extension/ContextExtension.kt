package cloud.shoplive.sample.extension

import android.app.Activity
import android.content.Context
import android.content.Intent

fun Context.showShareDialog(shareUrl: String) {
    if (this is Activity && isFinishing) return

    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
    sendIntent.type = "text/plain"

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}
