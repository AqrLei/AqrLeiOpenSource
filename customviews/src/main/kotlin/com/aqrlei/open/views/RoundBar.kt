package com.aqrlei.open.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.os.Parcel
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
//TODO improve
@Suppress("unused")
class RoundBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null) :
        View(context, attrs) {
    companion object {
        private const val DEFAULT_BG_COLOR = "#bbbbbb"
        private const val DEFAULT_PROGRESS_COLOR = "#41a9f8"

        private const val GRADIENT_COLOR_START = "#21adf1"
        private const val GRADIENT_COLOR_END = "#2287ee"

        //当前进度监听器,开启初始绘图动画时有效
        var onDrawProgressChangeAction: ((Float) -> Unit)? = null
    }

    enum class CapStyle {
        BUTT, ROUND, SQUARE
    }

    //渐变色对应的位置
    private var mGradientPositions: ArrayList<Float>? = null
    //渐变色
    private val mGradientListColor = ArrayList<Int>()
    //背景颜色
    var mRoundBarBackgroundColor: Int = 0
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    //进度颜色
    var mProgressColor: Int = 0
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    //最大的进度
    var mMaxProgress: Float = 10F
        set(value) {
            if (value >= 0 && value != field) {
                field = value
                progressChange()
            }
        }
    //当前进度
    var mCurrentProgress: Float = 5f
        set(value) {
            if (value >= 0 && value != field) {
                field = value
                progressChange()
            }
        }
    //画的速度
    var mAnimationV: Float = 0F
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    //进度条是否使用渐变色
    var mIsUseGradientColor: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    //第一次绘图时是否开启动画
    var mIsOpenAnimation: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    //画圆扫过的角度
    var mSweepDegree: Float = 0F
        private set
    //圆半径
    var mRadius: Float = 100F
        private set
    // 画布旋转的角度
    var mRotateDegree: Float = 0F
        private set
    // 画圆开始的角度
    var mStartDegree: Float = 0F
        private set
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//画笔
    private var mProgressDegree: Float = 0F//当前进度该画的角度
    private var mDrawDegree: Float = 0F//画的起始角度
    private var isCanRefresh: Boolean = false

    init {
        mDrawDegree = 0F

        mPaint.style = Paint.Style.STROKE
        /**
         * val typedArray = TintTypedArray.obtainStyledAttributes(mContext,attrs,R.styleable.RoundBar)
         * */
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
            mRoundBarBackgroundColor = getColor(R.styleable.RoundBar_roundBarBackgroundColor, Color.parseColor(DEFAULT_BG_COLOR))
            mProgressColor = getColor(R.styleable.RoundBar_progressColor, Color.parseColor(DEFAULT_PROGRESS_COLOR))
            mRadius = getFloat(R.styleable.RoundBar_radius, mRadius)
            mMaxProgress = getFloat(R.styleable.RoundBar_maxProgress, mMaxProgress)
            mCurrentProgress = getFloat(R.styleable.RoundBar_progress, mCurrentProgress)
            mIsOpenAnimation = getBoolean(R.styleable.RoundBar_openAnimation, false)
            mIsUseGradientColor = getBoolean(R.styleable.RoundBar_useGradientColor, false)
            mStartDegree = getFloat(R.styleable.RoundBar_startDegree, 0f)
            mSweepDegree = getFloat(R.styleable.RoundBar_sweepDegree, 360f)
            mRotateDegree = getFloat(R.styleable.RoundBar_rotateDegree, 0f)
            mAnimationV = getFloat(R.styleable.RoundBar_animationVelocity, 1f)

            setRoundWidth(getFloat(R.styleable.RoundBar_roundWidth, 10f))
            setCapStyle(CapStyle.values()[getInteger(R.styleable.RoundBar_capStyle, CapStyle.BUTT.ordinal)])

            recycle()
        }
    }

    private fun progressChange() {
        mProgressDegree = (mSweepDegree * (mCurrentProgress / mMaxProgress)).toInt().toFloat()
        mProgressDegree = if (mProgressDegree > mSweepDegree) mSweepDegree else mProgressDegree
        mDrawDegree = 0F
        invalidate()
    }

    private fun setCapStyle(capStyle: CapStyle) {
        when (capStyle) {
            CapStyle.BUTT -> mPaint.strokeCap = Paint.Cap.BUTT
            CapStyle.ROUND -> mPaint.strokeCap = Paint.Cap.ROUND
            CapStyle.SQUARE -> mPaint.strokeCap = Paint.Cap.SQUARE
        }
    }

    private fun setRoundWidth(width: Float) {
        mPaint.strokeWidth = width
    }

    fun setGradientColor(gradientColor: List<Int>) {
        if (gradientColor.size < 2) {
            return
        }
        mGradientListColor.clear()
        mGradientListColor.addAll(gradientColor)
        invalidate()
    }

    fun setGradientPositions(gradientPositions: List<Float>) {
        if (gradientPositions.size < 2) {
            return
        }
        mGradientPositions = ArrayList()
        mGradientPositions?.addAll(gradientPositions)
        invalidate()
    }

    @SuppressLint("DrawAllocation", "NewApi")
    override fun onDraw(canvas: Canvas?) {
        isCanRefresh = true
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
        mPaint.color = mRoundBarBackgroundColor
        canvas?.drawArc(left, top, right, bottom, rStartDegree,
                mSweepDegree, false, mPaint)
        if (mIsUseGradientColor) {
            mPaint.shader = SweepGradient(centerX, centerY, mGradientListColor.toIntArray(), mGradientPositions?.toFloatArray())
        } else {
            mPaint.color = mProgressColor
        }
        if (mIsOpenAnimation) {
            onDrawProgressChangeAction?.invoke(
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

    override fun invalidate() {
        if (isCanRefresh) {
            super.invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            SavedState(it).apply {
                currentProgress = mCurrentProgress
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        this.mCurrentProgress = state.currentProgress
    }

    class SavedState : View.BaseSavedState {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel?): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

        internal var currentProgress: Float = 5f

        internal constructor(superState: Parcelable) : super(superState)
        private constructor(saveState: Parcel?) : super(saveState) {
            currentProgress = saveState?.readFloat() ?: currentProgress
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeFloat(currentProgress)
        }
    }
}