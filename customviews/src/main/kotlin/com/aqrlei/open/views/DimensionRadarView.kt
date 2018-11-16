package com.aqrlei.open.views

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author aqrlei on 2018/11/16
 */
/**
 * @description: 雷达维度图
 * scoreLevel 评分级别
 * maxScore 最高评分
 * dimensionTextSize
 * dimensionTextList 评分维度 >=3
 * dimensionTextColorList
 * scoreLevelList 维度评分 this.size == dimensionTextList.size
 * dimensionRadarBackgroundColor
 * dimensionRadarScoreColorList
 * diagonalLineColor
 * diagonalLineWidth
 * sideLineColor
 * sideLineWidth
 * */
//TODO
class DimensionRadarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null) : View(context, attrs) {

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}