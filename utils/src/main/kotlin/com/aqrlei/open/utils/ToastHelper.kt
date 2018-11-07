package com.aqrlei.open.utils

import android.content.Context
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * @author  aqrLei on 2018/8/8
 */
class ToastHelper private constructor() {
    companion object {
        private var reference: WeakReference<Context>? = null
        private val mHelper: ToastHelper by lazy { ToastHelper() }
        fun init(context: Context) {
            reference = WeakReference(context)
        }

        fun getHelper() = mHelper
    }

    private lateinit var toast: Toast

    init {
        reference?.get()?.let {
            toast = Toast.makeText(it, "", Toast.LENGTH_SHORT)
        } ?: let {
            throw RuntimeException("must invoke init() before new an object")
        }
    }

    fun show(msg: String) {
        with(toast) {
            setText(msg)
            show()
        }
    }
}