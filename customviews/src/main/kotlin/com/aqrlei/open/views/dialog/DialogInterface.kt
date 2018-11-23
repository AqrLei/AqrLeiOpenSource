package com.aqrlei.open.views.dialog

import android.graphics.Color
import android.view.View

/**
 * @author aqrlei on 2018/11/12
 */
interface DialogInterface<T> {
    companion object {
        const val CANCELABLE_KEY = "cancelableKey"
        const val TITLE_KEY = "titleKey"
        const val MESSAGE_KEY = "messageKey"
        const val POSITIVE_KEY = "positiveKey"
        const val POSITIVE_SHOW_KEY = "positiveShowKey"
        const val NEGATIVE_KEY = "negativeKey"
        const val NEGATIVE_SHOW_KEY = "negativeShowKey"
        const val NEUTRAL_KEY = "neutralKey"
    }

    fun configureTitle(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 12F): T

    fun configureNegativeButton(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 12F, action: ((View) -> Unit)? = null): T

    fun configurePositiveButton(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 12F, action: ((View) -> Unit)? = null): T

    fun setMCancelable(cancelable: Boolean): T
}