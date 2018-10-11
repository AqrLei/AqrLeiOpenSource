package com.aqrlei.open.utils.qrcode

import android.content.Context
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2018/9/9
 */
object DensityTransform {
    fun dpToPx(context: WeakReference<Context>, value: Float): Float {
        return (context.get()?.resources?.displayMetrics?.density ?: 1F) * value + 0.5F
    }
    fun pxToDp(context: WeakReference<Context>, value: Float): Float {
        return (value / (context.get()?.resources?.displayMetrics?.density ?: 1F)) + 0.5F
    }
}
