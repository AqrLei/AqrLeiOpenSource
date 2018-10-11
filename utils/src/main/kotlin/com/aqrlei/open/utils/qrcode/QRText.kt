package com.aqrlei.open.utils.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2018/9/9
 */
class QRText(
        private val context: WeakReference<Context>,
        private var topTextConfigure: TextConfigure? = null,
        private var bottomTextConfigure: TextConfigure? = null) {
    companion object {
        const val DEFAULT_TEXT_SIZE = 12F
        const val DEFAULT_TEXT_COLOR = "#FF000000"
    }


    fun addTextOnTop(content: String, container: Bitmap): QRCode.TextDraw? {
        return topTextConfigure?.run {
            addText(topOrBottomMargin, leftMargin, content, textSize, textColor, container)
        }
    }

    fun addTextOnBottom(content: String, container: Bitmap): QRCode.TextDraw? {
        return bottomTextConfigure?.run {
            var formatTopMargin = topOrBottomMargin
            if (null != formatTopMargin) {
                formatTopMargin = DensityTransform.pxToDp(context, container.height.toFloat()) - formatTopMargin
            }
            addText(formatTopMargin, leftMargin, content, textSize, textColor, container)
        }
    }

    private fun addText(topMarginWithDp: Float?, leftMarginWithDp: Float?, content: String,
                        textSizeWithDp: Float, textColor: String, container: Bitmap): QRCode.TextDraw {
        val customTextSize = DensityTransform.dpToPx(context, textSizeWithDp)
        val customTextColor = Color.parseColor(textColor)
        val textFormat = content.trim()
        val freeWidthLength = container.width - textFormat.length * customTextSize
        val freeHeightLength = container.height - customTextSize
        val (leftMargin, topMargin) = MarginFormat.getMargin(context, freeHeightLength, freeWidthLength, leftMarginWithDp, topMarginWithDp)
        val paint = Paint().apply {
            color = customTextColor
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            this.textSize = customTextSize
        }
        return QRCode.TextDraw(content, leftMargin, topMargin, paint)
    }

    fun refreshTop(newConfigure: TextConfigure?) {
        when {
            null == newConfigure -> topTextConfigure = null
            null == topTextConfigure -> topTextConfigure = newConfigure
            else -> refresh(topTextConfigure, newConfigure)
        }
    }

    fun refreshBottom(newConfigure: TextConfigure?) {
        when {
            null == newConfigure -> bottomTextConfigure = null
            null == bottomTextConfigure -> bottomTextConfigure = newConfigure
            else -> refresh(bottomTextConfigure, newConfigure)
        }
    }

    private fun refresh(textConfigure: TextConfigure?, newConfigure: TextConfigure) {
        with(newConfigure) {
            if (textConfigure?.leftMargin != leftMargin) {
                textConfigure?.leftMargin = leftMargin
            }
            if (textConfigure?.topOrBottomMargin != topOrBottomMargin) {
                textConfigure?.topOrBottomMargin = topOrBottomMargin
            }
            if (textConfigure?.textSize != textSize) {
                textConfigure?.textSize = textSize
            }
            if (textConfigure?.textColor != textColor) {
                textConfigure?.textColor = textColor
            }
        }
    }

    data class TextConfigure(
            var leftMargin: Float? = null,
            var topOrBottomMargin: Float? = null,
            var textSize: Float = DEFAULT_TEXT_SIZE,
            var textColor: String = DEFAULT_TEXT_COLOR)

}