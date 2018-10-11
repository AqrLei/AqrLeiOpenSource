package com.aqrlei.open.utils.qrcode

import android.content.Context
import com.aqrlei.open.utils.qrcode.DensityTransform.dpToPx
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2018/9/9
 */
object MarginFormat {
    fun getMargin(context: WeakReference<Context>, freeHeightLength: Float, freeWidthLength: Float, leftMarginWithDp: Float?,
                  topMarginWithDp: Float?): Margin {
        return if (leftMarginWithDp != null && topMarginWithDp != null) {
            Margin((dpToPx(context, leftMarginWithDp)), dpToPx(context, topMarginWithDp))
        } else if (leftMarginWithDp == null && topMarginWithDp != null) {
            Margin(freeWidthLength / 2F, dpToPx(context, topMarginWithDp))
        } else if (leftMarginWithDp != null && topMarginWithDp == null) {
            Margin((dpToPx(context, leftMarginWithDp)), freeHeightLength / 2F)
        } else {
            Margin(freeWidthLength / 2F, freeHeightLength / 2F)
        }
    }

    data class Margin(var leftMargin: Float, var topMargin: Float)
}