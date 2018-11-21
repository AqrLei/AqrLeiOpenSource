package com.aqrlei.open.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.aqrlei.open.views.util.DensityUtil
import kotlin.math.PI

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
 * dimensionScoreLevelList 维度评分 this.size == dimensionTextList.size
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
    companion object {
        const val DEFAULT_BACKGROUND_COLOR = Color.WHITE
        const val DEFAULT_LINE_COLOR = Color.GRAY
        const val DEFAULT_TEXT_COLOR = Color.BLACK
        val DEFAULT_SCORE_COLOR = Color.parseColor("#8800ff00")

        const val DIMENSION_SCORE_DRAW_FULL = 0
        const val DIMENSION_SCORE_DRAW_STROKE = 1
    }

    private var radius: Float = 0F
    private var centerX: Float = 0F
    private var centerY: Float = 0F
    private var scoreLevel: Int = 2
    private var drawScoreLevelStyle = DIMENSION_SCORE_DRAW_FULL

    private var rotateDegree: Float = 0F
    private var dimensionTextSize: Int = DensityUtil.dip2px(14F)

    private val dimensionTextList = ArrayList<String>()
    private lateinit var dimensionTextColorList: Array<Int>

    private var maxScore: Int = 100
    private lateinit var dimensionScoreLevelList: Array<Float>

    private val dimensionScoreLevelPointList by lazy {
        ArrayList<PointF>()
    }

    private var dimensionRadarBackgroundColor: Int = DEFAULT_BACKGROUND_COLOR

    private var dimensionRadarScoreColorList = ArrayList<Int>()

    private var diagonalLineColor: Int = DEFAULT_LINE_COLOR
    private var diagonalLineWidth: Int = DensityUtil.dip2px(1F)
    private var sideLineColor: Int = DEFAULT_LINE_COLOR
    private var sideLineWidth: Int = DensityUtil.dip2px(1F)

    private var divideDegree: Float = 120F

    private var divideCount: Int = 0

    private val linePaint = Paint()
    private val scorePaint = Paint()
    private val textPaint = Paint()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DimensionRadarView)?.run {
            scoreLevel = getInteger(R.styleable.DimensionRadarView_scoreLevel, scoreLevel)
            maxScore = getInteger(R.styleable.DimensionRadarView_maxScore, maxScore)
            dimensionTextSize = getDimensionPixelSize(
                    R.styleable.DimensionRadarView_dimensionTextSize,
                    dimensionTextSize)
            rotateDegree = getFloat(R.styleable.DimensionRadarView_dimensionRotateDegree, rotateDegree)
            drawScoreLevelStyle = getInteger(R.styleable.DimensionRadarView_scoreLevelDrawStyle, drawScoreLevelStyle)
            getTextArray(R.styleable.DimensionRadarView_dimensionTextList)?.forEach {
                dimensionTextList.add(it.toString())
            }
            divideCount = dimensionTextList.size

            dimensionTextColorList = Array(divideCount) { DEFAULT_TEXT_COLOR }
            dimensionScoreLevelList = Array(divideCount) { 0F }
            getTextArray(R.styleable.DimensionRadarView_dimensionTextColorList)?.forEachIndexed { index, it ->
                if (index < divideCount) {
                    dimensionTextColorList[index] =
                            try {
                                Color.parseColor(it.toString())
                            } catch (e: IllegalArgumentException) {
                                DEFAULT_TEXT_COLOR
                            }
                }
            }
            getTextArray(R.styleable.DimensionRadarView_dimensionScoreLevelList)?.forEachIndexed { index, it ->
                if (index < divideCount) {
                    dimensionScoreLevelList[index] = (it?.toString()?.toFloatOrNull() ?: 0F)
                }
            }

            dimensionRadarBackgroundColor = getColor(
                    R.styleable.DimensionRadarView_dimensionRadarBackgroundColor,
                    dimensionRadarBackgroundColor)

            getTextArray(R.styleable.DimensionRadarView_dimensionRadarScoreColorList)?.forEach {
                dimensionRadarScoreColorList.add(
                        try {
                            Color.parseColor(it?.toString())
                        } catch (e: java.lang.IllegalArgumentException) {
                            Color.GRAY
                        })
            }
            diagonalLineColor = getColor(R.styleable.DimensionRadarView_diagonalLineColor, diagonalLineColor)
            diagonalLineWidth = getDimensionPixelSize(R.styleable.DimensionRadarView_diagonalLineWidth, diagonalLineWidth)

            sideLineColor = getColor(R.styleable.DimensionRadarView_sideLineColor, sideLineColor)
            sideLineWidth = getDimensionPixelSize(R.styleable.DimensionRadarView_sideLineWidth, sideLineWidth)

            recycle()
        }

        with(linePaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        with(scorePaint) {
            style = if (drawScoreLevelStyle == DIMENSION_SCORE_DRAW_FULL)
                Paint.Style.FILL_AND_STROKE
            else Paint.Style.STROKE
            isAntiAlias = true
            color = DEFAULT_SCORE_COLOR
        }
        with(textPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = dimensionTextSize.toFloat()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (Math.min(w, h) - 2 * dimensionTextSize) / 2 * 0.9F
        centerX = w / 2.0F
        centerY = h / 2.0F
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (divideCount >= 3) {
            canvas.save()
            drawScoreLevel(canvas)
            canvas.restore()
            drawScoreLines(canvas)
        }
    }

    private fun drawScoreLevel(canvas: Canvas) {

        canvas.rotate(rotateDegree, centerX, centerY)
        for (i in 0 until scoreLevel) {
            val scale = (1.0F - 1.0F / (scoreLevel - i))
            drawLines(canvas, i)
            canvas.scale(scale, scale, centerX, centerY)
        }
    }

    private fun drawLines(canvas: Canvas, index: Int) {
        val a = 360.0 / divideCount
        divideDegree = a.toFloat()
        val b = ((a / 2) / 360.0) * 2 * PI
        val c = (((180 - a) / 2.0) / 360.0) * 2 * PI
        val side = radius * Math.abs(Math.sin(b)) * 2
        val diagonalPath = Path()
        val sidePath = Path()

        for (i in 0 until divideCount) {
            diagonalPath.reset()
            sidePath.reset()
            val diagonalX = centerX
            val diagonalY = centerY - radius
            if (index == 0) {
                drawText(canvas, diagonalX, diagonalY, dimensionTextList[i], dimensionTextColorList[i])
            }
            val sideX = diagonalX + (side * Math.abs(Math.sin(c))).toFloat()
            val sideY = diagonalY + (side * Math.abs(Math.cos(c))).toFloat()

            diagonalPath.moveTo(centerX, centerY)
            diagonalPath.lineTo(diagonalX, diagonalY)
            canvas.drawPath(diagonalPath, linePaint.apply {
                color = diagonalLineColor
            })
            sidePath.moveTo(diagonalX, diagonalY)
            sidePath.lineTo(sideX, sideY)
            canvas.drawPath(sidePath, linePaint.apply {
                color = sideLineColor
            })
            canvas.rotate(a.toFloat(), centerX, centerY)
        }
    }

    /**
     * 文字居中，距底部的是一半字体大小的距离
     * */
    private fun drawText(canvas: Canvas, x: Float, y: Float, text: String, textColor: Int) {
        val tempX = x - (textPaint.measureText(text)) / 2F
        val tempY = y - dimensionTextSize / 2
        canvas.drawText(text, tempX, tempY, textPaint.apply {
            color = textColor
        })
    }


    /**
     * 各个点的信息都需要dimensionScoreLevelList来确认
     */
    private fun drawScoreLines(canvas: Canvas) {
        dimensionScoreLevelPointList.clear()

        val rotateRadian = (rotateDegree / 360.0) * 2 * PI
        val divideRadian = (divideDegree / 360.0) * 2 * PI

        val firstScoreRatio = formatScoreLevel(dimensionScoreLevelList[0]) / maxScore * 1.0
        val firstX = (centerX + radius * Math.sin(rotateRadian) * firstScoreRatio).toFloat()
        val firstY = (centerY - radius * Math.cos(rotateRadian) * firstScoreRatio).toFloat()
        var firstRadian = rotateRadian + divideRadian
        dimensionScoreLevelPointList.add(PointF(firstX, firstY))
        for (i in 1 until divideCount) {
            val scoreRatio = formatScoreLevel(dimensionScoreLevelList[i]) / maxScore * 1.0
            val x = (centerX + radius * Math.sin(firstRadian) * scoreRatio).toFloat()
            val y = (centerY - radius * Math.cos(firstRadian) * scoreRatio).toFloat()
            dimensionScoreLevelPointList.add(PointF(x, y))
            firstRadian += divideRadian
        }
        val path = Path()
        path.moveTo(firstX, firstY)
        for (i in 1 until dimensionScoreLevelPointList.size) {
            path.lineTo(dimensionScoreLevelPointList[i].x, dimensionScoreLevelPointList[i].y)
        }
        path.close()
        canvas.drawPath(path, scorePaint)
    }

    private fun formatScoreLevel(scoreLevel: Float): Float {
        return if (scoreLevel > maxScore || scoreLevel < 0) {
            0F
        } else {
            scoreLevel
        }
    }
}