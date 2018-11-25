package com.aqrlei.open.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.StyleRes

/**
 * @author aqrlei on 2018/11/15
 */
abstract class CustomProgressDialog(context: Context,
                                    @StyleRes style: Int) : Dialog(context, style) {

    protected lateinit var progressBar: ProgressBar
    abstract fun getLayoutRes(): Int
    protected open fun getProgressDrawableRes(): Drawable? {
        return null
    }
    protected open fun getIndeterminateDrawableRes(): Drawable? {
        return null
    }
    protected open fun getMProgressbar():ProgressBar{
        return ProgressBar(context)
    }
    protected open fun getDialogLayoutParams():ViewGroup.LayoutParams{
       return  ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    abstract fun bindProgressBar(parentView: View, progressBar: ProgressBar): View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v = LayoutInflater.from(context).inflate(getLayoutRes(), null).apply {
            progressBar = getMProgressbar().apply {
                progressDrawable = getProgressDrawableRes() ?: progressDrawable
                indeterminateDrawable = getIndeterminateDrawableRes() ?: indeterminateDrawable
            }
            bindProgressBar(this, progressBar)
        }
        val lp = getDialogLayoutParams()
        bindAction(v)
        setContentView(v, lp)
    }

    protected open fun bindAction(view: View) {}

}