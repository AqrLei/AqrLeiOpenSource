package com.aqrlei.open.views.banner

import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup

/**
 * @author aqrlei on 2018/9/25
 */
interface BannerAdapterHolder {
    fun getPagerAdapter(): PagerAdapter
    fun setDotViews(parent: ViewGroup)
}