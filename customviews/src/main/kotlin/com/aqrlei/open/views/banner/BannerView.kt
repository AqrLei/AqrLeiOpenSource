package com.aqrlei.open.views.banner

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.aqrlei.open.views.R
import kotlinx.android.synthetic.main.layout_banner.view.*

/**
 * @author aqrlei on 2018/9/20
 */
class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs) {
    private val inflater = LayoutInflater.from(context)

    private val contentLayout: View =
            inflater.inflate(R.layout.layout_banner, this, true)

    private val dotsView = contentLayout.findViewById<LinearLayout>(R.id.dots)
    private val contentVp = contentLayout.findViewById<ViewPager>(R.id.bannerContent)

    private var count: Int = 0
    private var currentIndex: Int = 0
    private var interval = 1000L
    private var ratio: String = ""
    private var isAuto: Boolean = false

    private var adapter: PagerAdapter? = null
    private var bannerAdapterHolder: BannerAdapterHolder? = null

    private val mHandler = Handler(Looper.getMainLooper())

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
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView)

        if (attrsArray.hasValue(R.styleable.BannerView_widthHeightRatio)) {
            ratio = attrsArray.getString(R.styleable.BannerView_widthHeightRatio) ?: ""
        }
        if (attrsArray.hasValue(R.styleable.BannerView_isAuto)) {
            isAuto = attrsArray.getBoolean(R.styleable.BannerView_isAuto, false)
        }
        if (attrsArray.hasValue(R.styleable.BannerView_interval)) {
            interval = attrsArray.getInteger(R.styleable.BannerView_interval, 1000).toLong()
        }
        if (attrsArray.hasValue(R.styleable.BannerView_defaultBanner)) {
            contentLayout.bannerContent.background = attrsArray.getDrawable(R.styleable.BannerView_defaultBanner)
        }

        if (ratio.isEmpty()) {
            val lp = contentLayout.bannerContent.layoutParams
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            contentLayout.bannerContent.layoutParams = lp
        }

        if (contentLayout.container is ConstraintLayout) {
            val constraint = ConstraintSet()
            constraint.clone(contentLayout.container)
            constraint.setDimensionRatio(R.id.bannerContent, ratio)
            constraint.applyTo(contentLayout.container)
        }
        attrsArray.recycle()
        resetBannerView()
    }

    fun setAdapterHolder(adapterHolder: BannerAdapterHolder) {
        bannerAdapterHolder = adapterHolder
        this.adapter = adapterHolder.getPagerAdapter()
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

    private fun start() {
        if (isAuto) {
            cancel()
            mHandler.postDelayed(task, interval)
        }
    }

    private fun cancel() {
        if (isAuto) {
            mHandler.removeCallbacks(task)
        }
    }

    abstract class BannerViewAdapterHolder<T>(
            private val adapter: BannerViewAdapter<T>,
            private val dotLayout: Int = R.layout.layout_banner_dot) : BannerAdapterHolder {
        override fun getPagerAdapter() = adapter
        override fun setDotViews(parent: ViewGroup) {
            if (adapter.count <= 1) return
            for (i in 0 until adapter.count - 2) {
                val v = LayoutInflater.from(adapter.context).inflate(dotLayout, parent, false)
                v.isEnabled = i != 0
                parent.addView(v)
            }
        }
    }

    abstract class BannerViewAdapter<T>(
            val context: Context,
            private val data: List<T>,
            private val bannerLayout: Int = R.layout.layout_default_banner) : PagerAdapter() {
        override fun getCount() = if (data.size > 1) data.size + 2 else data.size
        override fun isViewFromObject(view: View, any: Any) = (any == view)
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View = when {
                count > 1 -> {
                    val pos = when (position) {
                        0 -> count - 3
                        count - 1 -> 0
                        else -> {
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
            dotsView.let {
                for (i in 0 until it.childCount) {
                    val v = it.getChildAt(i)
                    v.isEnabled = (index != i)
                }
            }
        }
    }
}