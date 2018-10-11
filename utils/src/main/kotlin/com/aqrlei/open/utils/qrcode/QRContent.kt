package com.aqrlei.open.utils.qrcode

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author aqrlei on 2018/9/9
 */
class QRContent(private val context: WeakReference<Context>,
                private var factory: LogoAdapter.Factory? = null,
                private val configure: QRConfigure) {
    companion object {
        const val DEFAULT_LENGTH = 180F
        const val DEFAULT_LOGO_RATIO = 0.25F
    }

    fun createQrCode(urlContent: String,
                     logo: Bitmap? = null,
                     container: Bitmap): QRCode.BitmapDraw? {

        if (urlContent.isEmpty()) {
            return null
        }
        return configure.run {
            val w = DensityTransform.dpToPx(context, width).toInt()
            val h = DensityTransform.dpToPx(context, height).toInt()

            var offsetX = w / 2
            var offsetY = h / 2

            var logoX = 0
            var logoY = 0
            var logoBitmap: Bitmap? = logo
            logoBitmap = shapeLogo(logoBitmap)
            logoBitmap = scaleBitmap(w, h, logoBitmap, logoRatio)
            logoBitmap?.let {
                logoX = logoBitmap.width
                logoY = logoBitmap.height
                offsetX = (w - logoX) / 2
                offsetY = (h - logoY) / 2
            }
            val hints = Hashtable<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.MARGIN] = 0
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            val matrix = MultiFormatWriter().encode(urlContent, BarcodeFormat.QR_CODE, w, h, hints)

            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                for (x in 0 until w) {
                    if (x >= offsetX && x < offsetX + logoX && y >= offsetY && y < offsetY + logoY) {
                        var pixel = logoBitmap?.getPixel(x - offsetX, y - offsetY) ?: 0
                        if (pixel == 0) {
                            pixel = if (matrix.get(x, y)) {
                                0xff000000.toInt()
                            } else {
                                0xffffffff.toInt()
                            }
                        }
                        pixels[y * w + x] = pixel
                    } else {
                        if (matrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000.toInt()
                        } else {
                            pixels[y * w + x] = 0xffffffff.toInt()
                        }
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
            val freeHeight = (container.height - bitmap.height).toFloat()
            val freeWidth = (container.width - bitmap.width).toFloat()
            val (leftMargin, topMargin) = MarginFormat.getMargin(context, freeHeight, freeWidth, leftMargin, topMargin)
            QRCode.BitmapDraw(bitmap, leftMargin, topMargin)
        }
    }

    fun refresh(newConfigure: QRConfigure) {
        with(newConfigure) {
            if (configure.width != width) {
                configure.width = width
            }
            if (configure.height != height) {
                configure.height = height
            }
            if (configure.logoRatio != logoRatio) {
                configure.logoRatio = logoRatio
            }
            if (configure.leftMargin != leftMargin) {
                configure.leftMargin = leftMargin
            }
            if (configure.topMargin != topMargin) {
                configure.topMargin = topMargin
            }
        }
    }

    private fun shapeLogo(bitmap: Bitmap?): Bitmap? {
        var tempBitmap = bitmap
        factory?.get()?.shape(bitmap) {
            tempBitmap = it
        }
        return tempBitmap
    }

    data class QRConfigure(
            var width: Float = DEFAULT_LENGTH,
            var height: Float = DEFAULT_LENGTH,
            var logoRatio: Float = DEFAULT_LOGO_RATIO,
            var leftMargin: Float? = null,
            var topMargin: Float? = null)

    interface LogoCreator {
        fun logoCreator(): Bitmap?
    }
}