package com.aqrlei.open.views.dialog

import android.content.Context
import android.graphics.drawable.Drawable
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
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(10F)))
        }
        return parentView
    }

    override fun getProgressDrawableRes(): Drawable? {
        return context.resources.getDrawable(R.drawable.progress_horizontal)
    }

    override fun getIndeterminateDrawableRes(): Drawable? {
        return context.resources.getDrawable(android.R.drawable.progress_indeterminate_horizontal)
    }

    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }

    fun setSecondaryProgress(progress: Int) {
        progressBar.secondaryProgress = progress
    }
}