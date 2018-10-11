package com.aqrlei.open.utils.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2018/9/9
 */
class QRContainer(private val context: WeakReference<Context>,
                  private var configure: ContainerConfigure) {
    companion object {
        const val DEFAULT_CONTAINER_LENGTH = 200F
        const val DEFAULT_BACKGROUND_COLOR = "#FFFFFFFF"
        val DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    }

    fun create(): QRCode.Container {
        return configure.run {
            val w = DensityTransform.dpToPx(context, width).toInt()
            val h = DensityTransform.dpToPx(context, height).toInt()

            val container = Bitmap.createBitmap(w, h, bitmapConfigure)
            val canvas = Canvas(container).apply { drawColor(Color.parseColor(backgroundColor)) }
            QRCode.Container(container, canvas)
        }
    }

    fun refreshContainer(newConfigure: ContainerConfigure, action: (QRContainer) -> Unit) {
        with(newConfigure) {
            if (configure.width != width) {
                configure.width = width
            }
            if (configure.height != height) {
                configure.height = height
            }
            if (configure.backgroundColor != backgroundColor) {
                configure.backgroundColor = backgroundColor
            }
            if (configure.bitmapConfigure != bitmapConfigure) {
                configure.bitmapConfigure = bitmapConfigure
            }
            action(this@QRContainer)
        }

    }

    data class ContainerConfigure(
            var width: Float = DEFAULT_CONTAINER_LENGTH,
            var height: Float = DEFAULT_CONTAINER_LENGTH,
            var backgroundColor: String = DEFAULT_BACKGROUND_COLOR,
            var bitmapConfigure: Bitmap.Config = DEFAULT_BITMAP_CONFIG

    )
}