package com.aqrlei.open.views.adapter

import android.content.Context
import androidx.annotation.LayoutRes

/**
 * @author aqrlei on 2018/11/8
 */
abstract class SpecialRecyclerAdapter<T>(context: Context,
                                         @LayoutRes resId: Int,
                                         data: List<T>) : CommonRecyclerAdapter<T>(context, resId, data) {
    private val specialItemList = ArrayList<T>()

    fun addSpecialItem(item: T) {
        specialItemList.add(item)
    }

    fun clearSpecialItems() {
        specialItemList.clear()
    }

    fun removeSpecialItem(item: T) {
        specialItemList.remove(item)
    }

    fun specialItemIsEmpty(): Boolean = specialItemList.isEmpty()
}