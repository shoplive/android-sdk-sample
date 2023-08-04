package cloud.shoplive.sample.extension

import android.content.Context
import android.util.TypedValue

fun Int.toDp(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)

fun Int.toPx(context: Context): Float = this / context.resources.displayMetrics.density
