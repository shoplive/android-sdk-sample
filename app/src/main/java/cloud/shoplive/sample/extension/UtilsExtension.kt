package cloud.shoplive.sample.extension

import android.content.res.Configuration
import androidx.fragment.app.Fragment

internal fun Fragment.isTablet(): Boolean {
    val screenSizeType = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    if (screenSizeType == Configuration.SCREENLAYOUT_SIZE_XLARGE
        || screenSizeType == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true
    }
    return false
}