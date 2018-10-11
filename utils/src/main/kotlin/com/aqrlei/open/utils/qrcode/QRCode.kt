package com.aqrlei.open.utils.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2018/9/4
 */
class QRCode private constructor(
        private val context: WeakReference<Context>,
        private val containerConfigure: QRContainer.ContainerConfigure,
        private val qrConfigure: QRContent.QRConfigure,
        private var logoAdapterFactory: LogoAdapter.Factory? = null,
        private var topTextConfigure: QRText.TextConfigure? = null,
        private var bottomTextConfigure: QRText.TextConfigure? = null) {
    private var resultBitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null

    private var logoCreator: QRContent.LogoCreator? = null
    private var qrText: QRText? = null
    private var qrContent: QRContent? = null
    private var qrContainer: QRContainer? = null

    init {
        create()
    }

    private fun create() {

        qrContainer = QRContainer(context, containerConfigure)

        qrContent = QRContent(context, logoAdapterFactory, qrConfigure)

        if (null != topTextConfigure || null != bottomTextConfigure) {
            qrText = QRText(context, topTextConfigure, bottomTextConfigure)
        }
        qrContainer?.create()?.run {
            resultBitmap = container
            bitmapCanvas = canvas
        }
    }

    fun drawText(topContent: String = "", bottomContent: String = ""): QRCode {
        if (null == qrText) {
            logDebug("TextConfigure was not configured")
        }
        resultBitmap?.let { container ->
            qrText?.let { text ->
                when {
                    topContent.isNotEmpty() && bottomContent.isNotEmpty() -> {
                        textDraw(text.addTextOnBottom(bottomContent, container))
                        textDraw(text.addTextOnTop(topContent, container))
                    }
                    topContent.isNotEmpty() && bottomContent.isEmpty() -> {
                        textDraw(text.addTextOnTop(topContent, container))
                    }
                    topContent.isEmpty() && bottomContent.isNotEmpty() -> {
                        textDraw(text.addTextOnBottom(bottomContent, container))
                    }
                }
            }
        }
        return this
    }

    private fun textDraw(textDraw: TextDraw?) {
        textDraw?.run {
            bitmapCanvas?.drawText(content, leftMargin, topMargin, paint)
        }
    }

    fun drawQRContent(content: String): QRCode {
        if (null == qrContent) {
            println("qrContent was not set")
        }
        val logoBitmap = logoCreator?.logoCreator()
        resultBitmap?.let { container ->
            bitmapDraw(qrContent?.createQrCode(content, logoBitmap, container))
        }
        return this
    }

    private fun bitmapDraw(bitmapDraw: BitmapDraw?) {
        bitmapDraw?.run {
            bitmapCanvas?.drawBitmap(bitmap, leftMargin, topMargin, Paint(Paint.ANTI_ALIAS_FLAG))
        }
    }

    fun addLogoCreator(creator: QRContent.LogoCreator): QRCode {
        logoCreator = creator
        return this
    }

    fun addLogoCreator(action: () -> Bitmap?): QRCode {
        logoCreator = object : QRContent.LogoCreator {
            override fun logoCreator(): Bitmap? {
                return action.invoke()
            }
        }
        return this
    }

    fun get(): Bitmap? {
        return if (resultBitmap == null) {
            null
        } else {
            Bitmap.createBitmap(resultBitmap!!)
        }
    }


    fun refreshContainer(configure: QRContainer.ContainerConfigure) {
        qrContainer?.refreshContainer(configure) {
            it.create().run {
                resultBitmap = container
                bitmapCanvas = canvas
            }
        }
    }

    fun refreshContent(configure: QRContent.QRConfigure) {
        qrContent?.refresh(configure)
    }

    fun refreshTopTextConfig(configure: QRText.TextConfigure?) {
        qrText?.refreshTop(configure)
    }

    fun refreshBottomTextConfig(configure: QRText.TextConfigure?) {
        qrText?.refreshBottom(configure)
    }

    fun wipeAll() {
        containerConfigure.run {
            bitmapCanvas?.drawColor(Color.parseColor(backgroundColor))
        }
    }

    fun destroy() {
        if (null != qrContainer) {
            qrContainer = null
        }
        if (null != qrContent) {
            qrContent = null
        }
        if (null != qrText) {
            qrText = null
        }
        if (null != bitmapCanvas) {
            bitmapCanvas = null
        }
        if (null != resultBitmap) {
            resultBitmap?.recycle()
            resultBitmap = null
        }
    }

    class Builder {
        private var reference: WeakReference<Context>? = null
        private var logoAdapterFactory: LogoAdapter.Factory? = null
        private var containerConfigure: QRContainer.ContainerConfigure? = null
        private var qrConfigure: QRContent.QRConfigure? = null
        private var topTextConfigure: QRText.TextConfigure? = null
        private var bottomTextConfigure: QRText.TextConfigure? = null
        fun addContext(context: Context): Builder {
            reference = WeakReference(context)
            return this
        }

        fun addLogoAdapterFactory(factory: LogoAdapter.Factory): Builder {
            logoAdapterFactory = factory
            return this
        }

        fun addContainerConfigure(configure: QRContainer.ContainerConfigure): Builder {
            containerConfigure = configure
            return this
        }

        fun addQRConfigure(configure: QRContent.QRConfigure): Builder {
            qrConfigure = configure
            return this
        }

        fun addTextConfigure(topConfigure: QRText.TextConfigure?, bottomConfigure: QRText.TextConfigure?): Builder {
            topTextConfigure = topConfigure
            bottomTextConfigure = bottomConfigure
            return this
        }

        fun build(): QRCode? {
            if (null == reference) {
                throw NullPointerException("reference  cannot be null")
            }
            if (null == containerConfigure) {
                throw NullPointerException("containerConfigure cannot be null")
            }
            if (null == qrConfigure) {
                throw NullPointerException("qrConfigure cannot be null")
            }
            return QRCode(reference!!, containerConfigure!!, qrConfigure!!, logoAdapterFactory,
                    topTextConfigure, bottomTextConfigure)
        }
    }

    data class Container(
            var container: Bitmap,
            var canvas: Canvas
    )

    data class TextDraw(
            var content: String,
            var leftMargin: Float,
            var topMargin: Float,
            var paint: Paint
    )

    data class BitmapDraw(
            var bitmap: Bitmap,
            var leftMargin: Float,
            var topMargin: Float
    )
}