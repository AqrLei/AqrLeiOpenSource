package com.aqrlei.open.views.banner

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.aqrlei.open.views.R

/**
 * @author aqrlei on 2018/9/20
 */
class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : ConstraintLayout(context, attrs) {
    companion object {
        private val systemMetrics = Resources.getSystem().displayMetrics
        private val DOT_DEFAULT_SIZE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, systemMetrics)
        private val DEFAULT_DOT_BACKGROUND = ColorDrawable(Color.TRANSPARENT)
    }

    private val dotsView: LinearLayout = LinearLayout(context)
    private val contentVp: ViewPager = ViewPager(context)


    private var count: Int = 0
    private var currentIndex: Int = 0
    private var interval = 1000L
    private var ratio: String = "4:3"
    private var isAuto: Boolean = false

    private var adapter: PagerAdapter? = null
    private var bannerAdapterHolder: BannerAdapterHolder? = null

    private val mHandler = Handler(Looper.getMainLooper())

    private var dotInterval = DOT_DEFAULT_SIZE
    private var dotWidth = DOT_DEFAULT_SIZE
    private var dotHeight = DOT_DEFAULT_SIZE

    private var dotBackground: Drawable = DEFAULT_DOT_BACKGROUND
    private val task = object : Runnable {
        override fun run() {
            if (count > 1) {
                currentIndex = currentIndex % (count + 1) + 1
                if (currentIndex == 1) {
                    contentVp.setCurrentItem(currentIndex, false)
                } else {
                    contentVp.currentItem = currentIndex
                }
                mHandler.postDelayed(this, interval)
            }
        }
    }

    init {
        contentVp.id = R.id.vpContent
        this.addView(contentVp, LayoutParams(0, 0).apply {
            bottomToBottom = LayoutParams.PARENT_ID
            leftToLeft = LayoutParams.PARENT_ID
            topToTop = LayoutParams.PARENT_ID
            rightToRight = LayoutParams.PARENT_ID
        })

        dotsView.id = R.id.llDots
        this.addView(dotsView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            orientation = LinearLayout.HORIZONTAL
            leftToLeft = LayoutParams.PARENT_ID
            rightToRight = LayoutParams.PARENT_ID
            bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15F, Resources.getSystem().displayMetrics).toInt()
            bottomToBottom = R.id.vpContent
        })

        context.obtainStyledAttributes(attrs, R.styleable.BannerView)?.apply {
            ratio = getString(R.styleable.BannerView_widthHeightRatio) ?: ratio
            if (ratio.isEmpty()) ratio = "4:3"
            isAuto = getBoolean(R.styleable.BannerView_isAuto, isAuto)
            interval = getInteger(R.styleable.BannerView_interval, interval.toInt()).toLong()
            contentVp.background = getDrawable(R.styleable.BannerView_defaultBanner)
            dotInterval = getDimension(R.styleable.BannerView_dotInterval, dotInterval)
            dotBackground = getDrawable(R.styleable.BannerView_dotBackground) ?: dotBackground
            dotWidth = getDimension(R.styleable.BannerView_dotWidth, dotWidth)
            dotHeight = getDimension(R.styleable.BannerView_dotHeight, dotHeight)
            recycle()
        }
        val constraint = ConstraintSet()
        constraint.clone(this)
        constraint.setDimensionRatio(contentVp.id, ratio)
        constraint.applyTo(this)

        resetBannerView()
    }

    fun <T> setAdapterWithHolder(adapter: BannerViewAdapter<T>) {
        bannerAdapterHolder = BannerViewAdapterHolder(adapter)
        this.adapter = bannerAdapterHolder?.getPagerAdapter()
        resetBannerView()
    }

    private fun resetBannerView() {
        dotsView.removeAllViews()
        contentVp.removeAllViews()
        bannerAdapterHolder?.setDotViews(dotsView)
        adapter?.let { pagerAdapter ->
            contentVp.run {
                adapter = adapter ?: let {
                    addOnPageChangeListener(BannerViewPagerChangeListener())
                    pagerAdapter
                }
            }
            count = pagerAdapter.count - 2
            contentVp.currentItem = if (count > 1) 1 else 0
        }
        if (isAuto) {
            start()
        }
    }

    fun refresh() {
        adapter?.notifyDataSetChanged()
        resetBannerView()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> cancel()
            MotionEvent.ACTION_UP -> start()
        }
        return super.onInterceptTouchEvent(ev)
    }

    private var isAutoTaskRemove: Boolean = false
    private fun start() {
        if (isAuto) {
            cancel()
            isAutoTaskRemove = false
            mHandler.postDelayed(task, interval)
        }
    }

    private fun cancel() {
        if (isAuto) {
            isAutoTaskRemove = true
            mHandler.removeCallbacks(task)
        }
    }

    private inner class BannerViewAdapterHolder<T>(
            private val adapter: BannerViewAdapter<T>) : BannerAdapterHolder {
        override fun getPagerAdapter() = adapter
        override fun setDotViews(parent: ViewGroup) {
            if (adapter.count <= 1) return
            for (i in 0 until adapter.count - 2) {
                val v = TextView(adapter.context)
                parent.addView(v, -1, LinearLayout.LayoutParams(dotWidth.toInt(), dotHeight.toInt()).apply {
                    leftMargin = dotInterval.toInt()
                })
                //        v.setBackgroundResource(R.drawable.shape_banner_dot) 没问题
                v.setBackground(dotBackground) //有问题
//                val v = LayoutInflater.from(adapter.context).inflate(R.layout.layout_banner_dot, parent)
                v.isEnabled = i != 0
//                  with(v){
//                      background = dotBackground
//                      isEnabled = i != 0
//                  }
            }
        }
    }

    abstract class BannerViewAdapter<T>(
            val context: Context,
            private val data: List<T>,
            private val bannerLayout: Int = R.layout.layout_default_banner) : PagerAdapter() {
        // size+2,在首尾各加一个占位用于轮播
        override fun getCount() = if (data.size > 1) data.size + 2 else data.size

        override fun isViewFromObject(view: View, any: Any) = (any == view)
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View = when {
                count > 1 -> {// 展示的内容数量大于1（最小值是3）
                    val pos = when (position) {
                        0 -> count - 3 // 到最后一张图
                        count - 1 -> 0 // 回到第一张图
                        else -> { //其它情况
                            position - 1
                        }
                    }
                    bindData(bannerLayout, pos, data[pos])
                }
                else -> {
                    bindData(bannerLayout, 0, data[0])
                }
            }
            container.addView(view)
            return view
        }

        abstract fun bindData(bannerLayout: Int, pos: Int, data: T): View

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView((any as View))
        }
    }

    inner class BannerViewPagerChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            if (count > 1) {
                when (state) {
                    ViewPager.SCROLL_STATE_IDLE -> {
                        if (currentIndex == 0) {
                            contentVp.setCurrentItem(count, false)
                        } else if (currentIndex == count + 1) {
                            contentVp.setCurrentItem(1, false)
                        }
                        if (isAutoTaskRemove) {
                            start()
                        }
                    }
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                        if (currentIndex == count + 1) {
                            contentVp.setCurrentItem(1, false)
                        } else if (currentIndex == 0) {
                            contentVp.setCurrentItem(count, false)
                        }
                    }
                    ViewPager.SCROLL_STATE_SETTLING -> {
                    }
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            currentIndex = position
            val index = when (position) {
                0 -> count - 1
                count + 1 -> 0
                else -> position - 1
            }

            Log.d("fxYan", "${dotsView.childCount}")

            for (i in 0 until dotsView.childCount) {
                val v = dotsView.getChildAt(i)
                v.isEnabled = (index != i)
                Log.d("fxYan", "${index != i}")
            }


        }
    }
}