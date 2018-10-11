package com.aqrlei.open.utils.qrcode

import android.graphics.Bitmap

/**
 * @author aqrlei on 2018/9/6
 */
interface LogoAdapter {
    fun shape(shape: Bitmap?, action: (Bitmap?) -> Unit)
    abstract class Factory {
        abstract fun get(): LogoAdapter
    }

}