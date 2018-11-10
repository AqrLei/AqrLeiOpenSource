package com.aqrlei.open.views.adapter

import android.content.Context
import androidx.annotation.LayoutRes

/**
 * @author aqrlei on 2018/11/10
 */
abstract class BottomDialogAdapter<T>(context: Context,
                                      @LayoutRes resId: Int,
                                      data: List<T>) : SpecialRecyclerAdapter<T>(context, resId, data) {
    var positiveAction: (() -> Nothing)? = null
    var negativeAction: (() -> Nothing)? = null
    var neutralAction: (() -> Nothing)? = null
}