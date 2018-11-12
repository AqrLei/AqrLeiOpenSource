package com.aqrlei.open.views.dialog

import android.graphics.Color
import android.view.View

/**
 * @author aqrlei on 2018/11/12
 */
interface DialogInterface<T> {
    fun configureTitle(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 0F): T

    fun configureNegativeButton(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 0F, action: ((View) -> Unit)? = null): T

    fun configurePositiveButton(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 0F, action: ((View) -> Unit)? = null): T

    fun setOutCancelable(cancelable: Boolean): T
    fun setBackCancelable(cancelable: Boolean): T
}