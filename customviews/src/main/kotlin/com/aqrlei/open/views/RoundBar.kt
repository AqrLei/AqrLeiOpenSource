package com.aqrlei.open.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View


/**
 * @author  aqrLei on 2018/4/25
 */

/**
 * @param context 上下文关系
 * @param attrs 属性
 * @param defStyleAttr 默认属性
 * @param defStyleRes 默认资源ID
 * @description: 画一个圆形或半圆的简单进度条
 * */

@Suppress("unused")
class RoundBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null) :
        View(context, attrs) {
    /**
     * 标明画的时候是当前进度开始还是从头开始
     * */
    companion object {

        /**异常销毁需要保存的数据的key*/
        private const val INSTANCE = "instance"
        private const val INSTANCE_BOOLEAN = "boolean"
        private const val INSTANCE_DEGREE = "degree"

        private const val DEFAULT_BG_COLOR = "#bbbbbb"
        private const val DEFAULT_PROGRESS_COLOR = "#41a9f8"

        private const val GRADIENT_COLOR_START = "#21adf1"
        private const val GRADIENT_COLOR_END = "#2287ee"
    }

    enum class CapStyle {
        BUTT, ROUND, SQUARE
    }

    private var mGradientPositions: ArrayList<Float>? = null//渐变色对应的位置
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//画笔
    private var mBackgroundColor: Int = 0//背景颜色
    private var mProgressColor: Int = 0//进度颜色
    private val mGradientListColor = ArrayList<Int>()//渐变色
    private var mRadius: Float = 100F//圆半径
    private var mMaxProgress: Float = 10F//最大的进度
    private var mCurrentProgress: Float = 5f//当前进度
    private var mIsOpenAnimation: Boolean = false//是否开启动画
    private var mIsUseGradientColor: Boolean = false//是否使用渐变
    private var mStartDegree: Float = 0F// 画圆开始的角度
    private var mSweepDegree: Float = 0F//画圆扫过的角度
    private var mRotateDegree: Float = 0F// 画布旋转的角度
    private var mProgressDegree: Float = 0F//当前进度该画的角度
    private var mDrawDegree: Float = 0F//画的起始角度
    private var mAnimationV: Float = 0F//画的速度
    private var mChangeListener: OnDrawProgressChangeListener? = null//当前进度监听器


    init {
        mDrawDegree = 0F

        mPaint.style = Paint.Style.STROKE
        /**
         * val typedArray = TintTypedArray.obtainStyledAttributes(
        mContext,
        attrs,
        R.styleable.RoundBar)*/
        context.obtainStyledAttributes(attrs, R.styleable.RoundBar)?.run {
            getTextArray(R.styleable.RoundBar_positions)?.let {
                if (it.isNotEmpty()) {
                    mGradientPositions = ArrayList()
                    it.forEach { pos ->
                        mGradientPositions?.add(pos.toString().toFloat())
                    }
                }

            }
            getTextArray(R.styleable.RoundBar_gradientColors)?.let {
                it.forEach { color ->
                    mGradientListColor.add(Color.parseColor(color.toString()))
                }
            } ?: let {
                mGradientListColor.add(Color.parseColor(GRADIENT_COLOR_START))
                mGradientListColor.add(Color.parseColor(GRADIENT_COLOR_END))
            }
            setBackgroundColor(getColor(
                    R.styleable.RoundBar_backgroundColor,
                    Color.parseColor(DEFAULT_BG_COLOR)))
            setProgressColor(
                    getColor(R.styleable.RoundBar_progressColor,
                            Color.parseColor(DEFAULT_PROGRESS_COLOR)))
            setRadius(getFloat(R.styleable.RoundBar_radius, mRadius))
            mMaxProgress = getFloat(R.styleable.RoundBar_maxProgress, mMaxProgress)
            mCurrentProgress = getFloat(R.styleable.RoundBar_progress, mCurrentProgress)
            setOpenAnimation(getBoolean(R.styleable.RoundBar_openAnimation, false))
            setUseGradientColor(getBoolean(R.styleable.RoundBar_useGradientColor, false))
            setRoundWidth(getFloat(R.styleable.RoundBar_roundWidth, 10f))

            setCapStyle(CapStyle.values()[getInteger(R.styleable.RoundBar_capStyle, CapStyle.BUTT.ordinal)])
            setStartDegree(getFloat(R.styleable.RoundBar_startDegree, 0f))
            setSweepDegree(getFloat(R.styleable.RoundBar_sweepDegree, 360f))
            setRotateDegree(getFloat(R.styleable.RoundBar_rotateDegree, 0f))
            setAnimationV(getFloat(R.styleable.RoundBar_animationVelocity, 1f))
            recycle()
        }
    }

    fun setAnimationV(v: Float) {
        mAnimationV = v
    }

    fun setStartDegree(degree: Float) {
        mStartDegree = degree
    }

    fun setSweepDegree(degree: Float) {
        mSweepDegree = degree
    }

    fun setRotateDegree(degree: Float) {
        mRotateDegree = degree
    }

    fun setCapStyle(capStyle: CapStyle) {
        when (capStyle) {
            CapStyle.BUTT -> mPaint.strokeCap = Paint.Cap.BUTT
            CapStyle.ROUND -> mPaint.strokeCap = Paint.Cap.ROUND
            CapStyle.SQUARE -> mPaint.strokeCap = Paint.Cap.SQUARE
        }
    }

    fun setRoundWidth(width: Float) {
        mPaint.strokeWidth = width
    }

    fun setRadius(radius: Float) {
        mRadius = radius
    }

    fun setGradientColor(gradientColor: ArrayList<Int>?) {
        if (gradientColor?.size ?: 0 < 2) {
            throw IllegalArgumentException("needs >= 2 number of colors")
        }
        mGradientListColor.clear()
        mGradientListColor.addAll(gradientColor!!)
    }

    fun getGradientColor(): ArrayList<Int> {
        return mGradientListColor
    }

    @Synchronized
    fun setProgress(progress: Float) {
        mCurrentProgress = progress
        mProgressDegree = (mSweepDegree * (mCurrentProgress / mMaxProgress)).toInt().toFloat()
        mProgressDegree = if (mProgressDegree > mSweepDegree) mSweepDegree else mProgressDegree
        mDrawDegree = 0F
        postInvalidate()
    }

    @Synchronized
    fun setMaxProgress(maxProgress: Float) {
        mMaxProgress = maxProgress
        setProgress(mCurrentProgress)
    }

    @Synchronized
    fun getProgress(): Float {
        return mCurrentProgress
    }

    @Synchronized
    fun getMaxProgress(): Float {
        return mMaxProgress
    }

    fun setProgressColor(color: Int) {
        mProgressColor = color
    }

    override fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
    }

    fun setUseGradientColor(useGradientColor: Boolean) {
        mIsUseGradientColor = useGradientColor
    }

    fun setOpenAnimation(openAnimation: Boolean) {
        mIsOpenAnimation = openAnimation
    }

    fun setOnDrawProgressListener(changeListener: OnDrawProgressChangeListener) {
        mChangeListener = changeListener
    }

    @SuppressLint("DrawAllocation", "NewApi")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val left = centerX - mRadius
        val top = centerY - mRadius
        val right = centerX + mRadius
        val bottom = centerY + mRadius
        val rStartDegree = mStartDegree - mRotateDegree
        canvas?.save()
        canvas?.rotate(mRotateDegree, centerX, centerY)
        mPaint.shader = null
        mPaint.color = mBackgroundColor
        canvas?.drawArc(left, top, right, bottom, rStartDegree,
                mSweepDegree, false, mPaint)
        if (mIsUseGradientColor) {
            mPaint.shader = SweepGradient(centerX, centerY, mGradientListColor.toIntArray(), mGradientPositions?.toFloatArray())
        } else {
            mPaint.color = mProgressColor
        }
        if (mIsOpenAnimation) {
            mChangeListener?.onDrawProgressChange(
                    if (mDrawDegree >= mProgressDegree) mCurrentProgress
                    else (mCurrentProgress * mDrawDegree / mProgressDegree)
            )
            canvas?.drawArc(left, top, right, bottom, mStartDegree, mDrawDegree, false, mPaint)
            if (mDrawDegree < mProgressDegree) {
                mDrawDegree += mAnimationV
                mDrawDegree = Math.min(mDrawDegree, mProgressDegree)
                invalidate()
            }
        } else {
            canvas?.drawArc(left, top, right, bottom, mStartDegree, mProgressDegree, false, mPaint)
        }
        canvas?.restore()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState())
        val bool = booleanArrayOf(mIsOpenAnimation, mIsUseGradientColor)
        val degrees = floatArrayOf(mStartDegree, mSweepDegree, mProgressDegree, mRotateDegree)

        with(bundle) {
            putBooleanArray(INSTANCE_BOOLEAN, bool)
            putFloatArray(INSTANCE_DEGREE, degrees)
        }
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            with(state) {
                getBooleanArray(INSTANCE_BOOLEAN)?.apply {
                    mIsOpenAnimation = this[0]
                    mIsUseGradientColor = this[1]
                }
                getFloatArray(INSTANCE_DEGREE)?.apply {
                    mStartDegree = this[0]
                    mSweepDegree = this[1]
                    mProgressDegree = this[2]
                    mRotateDegree = this[3]
                }
                super.onRestoreInstanceState(getParcelable(INSTANCE))
            }
            return
        }
        super.onRestoreInstanceState(state)
    }

    /**
     * 回调接口，传递 当前进度
     * */
    interface OnDrawProgressChangeListener {
        fun onDrawProgressChange(currentProgress: Float)
    }
}