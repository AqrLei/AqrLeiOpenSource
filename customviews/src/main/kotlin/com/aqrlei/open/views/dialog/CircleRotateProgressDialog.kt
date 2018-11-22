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
class CircleRotateProgressDialog(context: Context) :
        CustomProgressDialog(context, R.style.IPhoneDialogStyle) {

    override fun getLayoutRes(): Int = R.layout.layout_circle_rotate_progress

    override fun getIndeterminateDrawableRes(): Drawable? {
        return context.resources?.getDrawable(R.drawable.progress_circle_rotate)
    }

    override fun bindProgressBar(parentView: View, progressBar: ProgressBar): View {
        if (parentView is ViewGroup) {
            parentView.addView(progressBar, 0,
                    ViewGroup.LayoutParams(DensityUtil.dip2px(82F), DensityUtil.dip2px(82F)))
        }
        return parentView
    }
}