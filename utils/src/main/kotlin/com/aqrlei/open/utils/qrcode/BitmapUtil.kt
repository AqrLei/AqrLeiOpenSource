package com.aqrlei.open.utils.qrcode

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * @author aqrlei on 2018/9/9
 */

fun scaleBitmap(w: Int, h: Int, bitmap: Bitmap?, scale: Float): Bitmap? {
    return bitmap?.let {
        val matrix = Matrix().apply {
            val scaleRatio = Math.min(scale * w * 1.0f / bitmap.width, scale * h * 1.0f / bitmap.height)
            postScale(scaleRatio, scaleRatio)
        }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}