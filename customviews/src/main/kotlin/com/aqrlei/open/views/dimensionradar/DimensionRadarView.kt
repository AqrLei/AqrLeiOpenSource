package com.aqrlei.open.views.dimensionradar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.aqrlei.open.views.R
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
 * dimensionTextColorArray
 * dimensionScoreLevelArray 维度评分 this.size == dimensionTextList.size
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

    var scoreLevel: Int = 2
        set(value) {
            if (value > 2 && value != field) {
                field = value
                invalidate()
            }
        }
    var maxScore: Int = 100
        set(value) {
            if (value > 0 && value != field) {
                field = value
                invalidate()
            }
        }
    var rotateDegree: Float = 0F
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var dimensionTextSize: Int = DensityUtil.dip2px(14F)
        set(value) {
            if (value > 0 && value != field) {
                field = value
                invalidate()
            }
        }
    var diagonalLineColor: Int = DEFAULT_LINE_COLOR
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var diagonalLineWidth: Int = DensityUtil.dip2px(1F)
        set(value) {
            if (value > 0 && value != field) {
                field = value
                invalidate()
            }
        }
    var sideLineColor: Int = DEFAULT_LINE_COLOR
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var sideLineWidth: Int = DensityUtil.dip2px(1F)
        set(value) {
            if (value > 0 && value != field) {
                field = value
                invalidate()
            }
        }
    var maxSupportScoreNumber: Int = 1
        set(value) {
            if (value > 0 && value != field) {
                field = value
                maxSupportScoreNumberChanged()
            }
        }
    var dimensionRadarBackgroundColor: Int = DEFAULT_BACKGROUND_COLOR
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var drawScoreLevelStyle = DIMENSION_SCORE_DRAW_FULL
        set(value) {
            if (value != field) {
                when (value) {
                    DIMENSION_SCORE_DRAW_FULL, DIMENSION_SCORE_DRAW_STROKE -> {
                        field = value
                        invalidate()
                    }
                    else -> {
                    }
                }
            }
        }


    private val dimensionTextList = ArrayList<String>()
    private lateinit var dimensionTextColorArray: Array<Int?>
    private lateinit var dimensionScoreLevelArray: Array<Float>
    private val scoreColorArray = ArrayList<Int?>()

    private lateinit var supportScoreLevelArrays: Array<Array<Float>>


    private var isCanRefresh: Boolean = false
    private var radius: Float = 0F
    private var centerX: Float = 0F
    private var centerY: Float = 0F

    private val dimensionScoreLevelPointList by lazy {
        ArrayList<PointF>()
    }
    private val dimensionMaxScorePointList by lazy {
        ArrayList<PointF>()
    }

    private var divideDegree: Float = 120F
        get() = 360.0F / divideCount
    private var divideCount: Int = 3
        get() = dimensionTextList.size

    private val linePaint = Paint()
    private val scorePaint = Paint()
    private val textPaint = Paint()
    private val dimensionBackgroundPaint = Paint()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DimensionRadarView)?.run {

            getTextArray(R.styleable.DimensionRadarView_dimensionTextArray)?.forEach {
                dimensionTextList.add(it.toString())
            }

            getTextArray(R.styleable.DimensionRadarView_dimensionRadarScoreColorArray)?.forEachIndexed { index, it ->
                scoreColorArray.add(
                        try {
                            Color.parseColor(it?.toString())
                        } catch (e: java.lang.IllegalArgumentException) {
                            DEFAULT_SCORE_COLOR
                        })
            }

            dimensionTextColorArray = Array(divideCount) { DEFAULT_TEXT_COLOR }
            getTextArray(R.styleable.DimensionRadarView_dimensionTextColorArray)?.forEachIndexed { index, it ->
                if (index < divideCount) {
                    dimensionTextColorArray[index] =
                            try {
                                Color.parseColor(it.toString())
                            } catch (e: IllegalArgumentException) {
                                DEFAULT_TEXT_COLOR
                            }
                }
            }

            maxSupportScoreNumber = getInteger(R.styleable.DimensionRadarView_maxSupportScoreNumber, maxSupportScoreNumber)

            supportScoreLevelArrays = Array(maxSupportScoreNumber) { Array(divideCount) { 0F } }

            dimensionScoreLevelArray = Array(divideCount) { 0F }
            getTextArray(R.styleable.DimensionRadarView_dimensionScoreLevelArray)?.forEachIndexed { index, it ->
                if (index < divideCount) {
                    dimensionScoreLevelArray[index] = formatScoreLevel(it?.toString()?.toFloatOrNull()
                            ?: 0F)
                }
            }
            supportScoreLevelArrays[0] = dimensionScoreLevelArray

            scoreLevel = getInteger(R.styleable.DimensionRadarView_scoreLevel, scoreLevel)
            maxScore = getInteger(R.styleable.DimensionRadarView_maxScore, maxScore)

            dimensionTextSize = getDimensionPixelSize(R.styleable.DimensionRadarView_dimensionTextSize, dimensionTextSize)
            rotateDegree = getFloat(R.styleable.DimensionRadarView_dimensionRotateDegree, rotateDegree)
            drawScoreLevelStyle = getInteger(R.styleable.DimensionRadarView_scoreLevelDrawStyle, drawScoreLevelStyle)
            dimensionRadarBackgroundColor = getColor(R.styleable.DimensionRadarView_dimensionRadarBackgroundColor, dimensionRadarBackgroundColor)
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
        }
        with(textPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = dimensionTextSize.toFloat()
        }
        with(dimensionBackgroundPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            color = dimensionRadarBackgroundColor
        }
    }

    fun addDimensionText(text: String) {
        dimensionTextList.add(text)
        divideCountChanged()
    }

    fun replaceDimensionText(position: Int, text: String) {
        if (position < divideCount) {
            dimensionTextList[position] = text
            invalidate()
        }
    }

    fun replaceAllDimensionText(textArray: Array<String>) {
        dimensionTextList.clear()
        dimensionTextList.addAll(textArray)
        divideCountChanged()
    }

    fun replaceTextColor(position: Int, color: Int, isInternalCall: Boolean = false) {
        if (position < dimensionTextColorArray.size) {
            dimensionTextColorArray[position] = color
            if (!isInternalCall) {
                invalidate()
            }
        }
    }

    fun replaceAllTextColor(textColorArray: Array<Int>) {
        textColorArray.forEachIndexed { index, color ->
            replaceTextColor(index, color, true)
        }
        invalidate()
    }

    fun adjustItemSingleScoreLevel(parentIndex: Int, index: Int, scoreLevel: Float, isInternalCall: Boolean = false) {
        if (parentIndex < maxSupportScoreNumber && index < divideCount) {
            supportScoreLevelArrays[parentIndex][index] = scoreLevel
            if (!isInternalCall) {
                invalidate()
            }
        }
    }

    fun adjustItemScoreLevel(parentIndex: Int, scoreArray: Array<Float>, isInternalCall: Boolean = false) {
        scoreArray.forEachIndexed { index, fl ->
            adjustItemSingleScoreLevel(parentIndex, index, fl, true)
        }
        if (!isInternalCall) {
            invalidate()
        }
    }

    fun adjustAllItemScoreLevel(allScoreArray: Array<Array<Float>>) {
        allScoreArray.forEachIndexed { index, floats ->
            adjustItemScoreLevel(index, floats, true)
        }
        postInvalidate()
    }

    fun changeScoreColor(parentIndex: Int, scoreColor: Int, isInternalCall: Boolean = false) {
        if (parentIndex < maxSupportScoreNumber) {
            scoreColorArray[parentIndex] = scoreColor
            if (!isInternalCall) {
                invalidate()
            }
        }
    }

    fun changeAllScoreColor(scoreColorArray: Array<Int>) {
        scoreColorArray.forEachIndexed { index, i ->
            changeScoreColor(index, i, true)
        }
        invalidate()
    }

    private fun maxSupportScoreNumberChanged() {
        if (isCanRefresh) {
            val temp = supportScoreLevelArrays.copyOf(maxSupportScoreNumber)
            supportScoreLevelArrays = Array(maxSupportScoreNumber) { Array(divideCount) { 0F } }
            temp.forEachIndexed { index, floats ->
                supportScoreLevelArrays[index] = floats ?: supportScoreLevelArrays[index]
            }
            invalidate()
        }
    }

    private fun divideCountChanged() {
        dimensionTextColorArray = dimensionTextColorArray.copyOf(divideCount)
        supportScoreLevelArrays.forEachIndexed { index, floats ->
            supportScoreLevelArrays[index] = floats.copyOf(divideCount).map {
                it ?: 0F
            }.toTypedArray()
        }
        invalidate()
    }

    private fun formatScoreLevel(scoreLevel: Float): Float {
        return if (scoreLevel > maxScore || scoreLevel < 0) {
            maxScore.toFloat()
        } else if (scoreLevel < 0) {
            0F
        } else {
            scoreLevel
        }
    }


    private fun addFirstMaxScoreLevelPoint(radian: Double) {
        val firstMaxScoreX = (centerX + radius * Math.sin(radian)).toFloat()
        val firstMaxScoreY = (centerY - radius * Math.cos(radian)).toFloat()
        dimensionMaxScorePointList.add(PointF(firstMaxScoreX, firstMaxScoreY))
    }

    private fun addMaxScoreLevelPoint(radian: Double) {
        val maxScoreX = (centerX + radius * Math.sin(radian)).toFloat()
        val maxScoreY = (centerY - radius * Math.cos(radian)).toFloat()
        dimensionMaxScorePointList.add(PointF(maxScoreX, maxScoreY))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (Math.min(w, h) - 2 * dimensionTextSize) / 2 * 0.9F
        centerX = w / 2.0F
        centerY = h / 2.0F
        isCanRefresh = true
        invalidate()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun invalidate() {
        if (isCanRefresh) {
            super.invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (divideCount >= 3) {
            drawDimensionBackground(canvas)
            canvas.save()
            drawScoreLevel(canvas)
            canvas.restore()
            drawScoreLines(canvas)
        }
    }

    private fun drawDimensionBackground(canvas: Canvas) {
        canvas.drawPath(getMaxScorePath(), dimensionBackgroundPaint)
    }

    private fun getMaxScorePath(): Path {
        dimensionMaxScorePointList.clear()
        val rotateRadian = (rotateDegree / 360.0) * 2 * PI
        val divideRadian = (divideDegree / 360.0) * 2 * PI
        addFirstMaxScoreLevelPoint(rotateRadian)
        var firstRadian = rotateRadian + divideRadian
        for (i in 1 until divideCount) {
            addMaxScoreLevelPoint(firstRadian)
            firstRadian += divideRadian
        }
        return Path().apply {
            reset()
            for (i in 0 until dimensionMaxScorePointList.size) {
                if (i == 0) {
                    moveTo(dimensionMaxScorePointList[i].x, dimensionMaxScorePointList[i].y)
                } else {
                    lineTo(dimensionMaxScorePointList[i].x, dimensionMaxScorePointList[i].y)
                }
            }
            close()
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
        val b = ((divideDegree / 2) / 360.0) * 2 * PI
        val c = (((180 - divideDegree) / 2.0) / 360.0) * 2 * PI
        val side = radius * Math.abs(Math.sin(b)) * 2
        val diagonalPath = Path()
        val sidePath = Path()

        for (i in 0 until divideCount) {
            diagonalPath.reset()
            sidePath.reset()
            val diagonalX = centerX
            val diagonalY = centerY - radius
            if (index == 0) {
                drawText(canvas, diagonalX, diagonalY, dimensionTextList[i], dimensionTextColorArray[i]
                        ?: DEFAULT_TEXT_COLOR)
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
            canvas.rotate(divideDegree, centerX, centerY)
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

    private fun drawScoreLines(canvas: Canvas) {
        for (i in 0 until supportScoreLevelArrays.size) {
            canvas.drawPath(getScorePath(i), scorePaint.apply {
                color = scoreColorArray[i] ?: DEFAULT_SCORE_COLOR
            })
        }
    }

    private fun getScorePath(index: Int): Path {
        dimensionScoreLevelPointList.clear()
        val rotateRadian = (rotateDegree / 360.0) * 2 * PI
        val divideRadian = (divideDegree / 360.0) * 2 * PI
        addFirstScoreLevelPoint(rotateRadian, index)
        var firstRadian = rotateRadian + divideRadian
        for (i in 1 until divideCount) {
            addScoreLevelPoint(firstRadian, i, index)
            firstRadian += divideRadian
        }
        return Path().apply {
            reset()
            for (i in 0 until dimensionScoreLevelPointList.size) {
                if (i == 0) {
                    moveTo(dimensionScoreLevelPointList[i].x, dimensionScoreLevelPointList[i].y)
                } else {
                    lineTo(dimensionScoreLevelPointList[i].x, dimensionScoreLevelPointList[i].y)
                }
            }
            close()
        }
    }

    private fun addFirstScoreLevelPoint(radian: Double, index: Int) {
        val scoreLevelArray = supportScoreLevelArrays[index]
        scoreLevelArray.run {
            val firstScoreRatio = this[0] / maxScore * 1.0
            val firstX = (centerX + radius * Math.sin(radian) * firstScoreRatio).toFloat()
            val firstY = (centerY - radius * Math.cos(radian) * firstScoreRatio).toFloat()
            dimensionScoreLevelPointList.add(PointF(firstX, firstY))
        }
    }

    private fun addScoreLevelPoint(radian: Double, index: Int, parentIndex: Int) {
        val scoreLevelArray = supportScoreLevelArrays[parentIndex]
        scoreLevelArray.run {
            val scoreRatio = this[index] / maxScore * 1.0
            val x = (centerX + radius * Math.sin(radian) * scoreRatio).toFloat()
            val y = (centerY - radius * Math.cos(radian) * scoreRatio).toFloat()
            dimensionScoreLevelPointList.add(PointF(x, y))
        }
    }
}