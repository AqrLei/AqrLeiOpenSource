package com.aqrlei.open.utils.qrcode

import android.graphics.*

/**
 * @author aqrlei on 2018/9/6
 */
class RoundLogoAdapter : LogoAdapter {
    override fun shape(shape: Bitmap?, action: (Bitmap?) -> Unit) {
        action(
                shape?.let {
                    val output = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(output)
                    val rect = Rect(0, 0, it.width, it.height)
                    val rectF = RectF(rect)
                    val paint = Paint()
                    paint.isAntiAlias = true
                    canvas.drawARGB(0, 0, 0, 0)
                    canvas.drawRoundRect(rectF, it.width.toFloat(), it.height.toFloat(), paint)
                    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                    canvas.drawBitmap(it, rect, rect, paint)
                    output
                })
    }

    class Factory : LogoAdapter.Factory() {
        override fun get(): LogoAdapter {
            return RoundLogoAdapter()
        }
    }
}