package com.aqrlei.open.views.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.aqrlei.open.views.R
import com.aqrlei.open.views.util.DensityUtil

/**
 * @author aqrlei on 2018/11/22
 */
class HorizontalProgressDialog(context: Context) :
        CustomProgressDialog(context, R.style.IPhoneDialogStyle) {
    override fun getLayoutRes(): Int = R.layout.layout_horizontal_progress

    override fun bindProgressBar(parentView: View, progressBar: ProgressBar): View {
        if (parentView is ViewGroup) {
            parentView.addView(progressBar,
                    ViewGroup.LayoutParams((DensityUtil.screenWidth()*0.8).toInt(), DensityUtil.dip2px(5F)))
        }
        return parentView
    }

    override fun getMProgressbar(): ProgressBar {
        return LayoutInflater.from(context).inflate(R.layout.horizontal_progressbar,null) as ProgressBar
    }

    override fun getProgressDrawableRes(): Drawable? {
        return context.resources.getDrawable(R.drawable.progress_horizontal)
    }

    override fun getDialogLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(12F))
    }

    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }

    fun setSecondaryProgress(progress: Int) {
        progressBar.secondaryProgress = progress
    }
}