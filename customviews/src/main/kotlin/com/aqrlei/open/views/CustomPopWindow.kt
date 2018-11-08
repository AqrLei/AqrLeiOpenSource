package com.aqrlei.open.views

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_custom_pop.view.*
import kotlinx.android.synthetic.main.layout_custom_pop_item.view.*


/**
 * @author aqrlei on 2018/10/10
 */
class CustomPopWindow private constructor(
        context: Context,
        private val popWindowConfigure: PopConfigure,
        private val popCorner: PopCorner,
        private val itemAction: ((Int, PopContent, PopupWindow) -> Unit)? = null) {
    private val inflater = LayoutInflater.from(context)
    private val rootView = inflater.inflate(R.layout.layout_custom_pop, null)
    private val data = ArrayList<PopContent>()
    private val popupWindow = PopupWindow(context)
    private val mAdapter = CustomPopRecyclerAdapter(R.layout.layout_custom_pop_item)

    init {
        rootView.outsideCl.setOnClickListener { popupWindow.dismiss() }
        with(rootView.contentRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        rootView.contentRv.adapter = mAdapter

        (rootView.contentRv.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            rightMargin = dip2px(popWindowConfigure.marginRight)
        }
        when (popCorner) {
            PopCorner.TOP -> {
                with(rootView.topIv) {
                    visibility = View.VISIBLE
                    setImageResource(R.mipmap.ic_arrow_top)
                }
            }
            PopCorner.BOTH_NOT_SHOW -> {
                rootView.bottomIv.visibility = View.INVISIBLE
                rootView.topIv.visibility = View.INVISIBLE
            }
            PopCorner.BOTTOM -> {
                with(rootView.bottomIv) {
                    visibility = View.VISIBLE
                    setImageResource(R.mipmap.ic_arrow_bottom)
                }
            }
        }
        with(popupWindow) {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.MATCH_PARENT
            isOutsideTouchable = true
            isFocusable = true
            contentView = rootView
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        }
    }

    inner class CustomPopRecyclerAdapter(
            @LayoutRes private val resId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = inflater.inflate(resId, parent, false)
            popWindowConfigure.run {
                val paddingLeft = dip2px(itemPaddingLeft)
                val paddingTop = dip2px(itemPaddingTop)
                val paddingRight = dip2px(itemPaddingRight)
                val paddingBottom = dip2px(itemPaddingBottom)
                val drawablePadding = dip2px(itemDrawablePadding)
                view.popContentTv.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
                view.popContentTv.compoundDrawablePadding = drawablePadding
            }
            return CustomPopRecyclerViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                itemAction?.invoke(position, data[position], popupWindow)
            }
            holder.itemView.popContentTv.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, popWindowConfigure.itemTextSize)
                text = data[position].subtitle

                val length = popWindowConfigure.run {
                    dip2px(itemTextSize + itemPaddingBottom + itemPaddingTop)
                }

                val drawable = data[position].icon?.apply {
                    setBounds(0, 0, length, length)
                }
                setCompoundDrawables(drawable, null, null, null)
            }
            if (position == data.size - 1) {
                holder.itemView.dividerBottom.visibility = View.GONE
            }
        }
    }


    fun dismiss() {
        popupWindow.dismiss()
    }

    fun show(data: List<PopContent>, anchor: View, noArrowGravity: Int = Gravity.TOP) {
        this.data.clear()
        this.data.addAll(data)
        mAdapter.notifyDataSetChanged()
        when (popCorner) {
            PopCorner.BOTH_NOT_SHOW -> {
                setShowGravity(noArrowGravity, anchor.height)
            }
            PopCorner.BOTTOM -> {
                setShowGravity(Gravity.BOTTOM, anchor.height)
            }
            PopCorner.TOP -> {
                setShowGravity(Gravity.TOP, anchor.height)
            }
        }
        popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, 0, 0)
    }

    private fun setShowGravity(gravity: Int, anchorHeight: Int) {
        val topLp = rootView.topIv.layoutParams as? ConstraintLayout.LayoutParams
        val bothRightMargin = dip2px(popWindowConfigure.arrowMarginRight)
        topLp?.apply {
            verticalBias = if (gravity == Gravity.TOP) 0.0F else 1.0F
            rightMargin = bothRightMargin
        }
        val bottomLp = rootView.bottomIv.layoutParams as? ConstraintLayout.LayoutParams
        bottomLp?.apply {
            rightMargin = bothRightMargin
        }
        if (gravity == Gravity.TOP) {
            topLp?.apply {
                topMargin = dip2px(popWindowConfigure.marginTop) + anchorHeight
            }
        } else {
            bottomLp?.apply {
                bottomMargin = dip2px(popWindowConfigure.marginTop) + anchorHeight
            }
        }

        rootView.topIv.layoutParams = topLp
        rootView.bottomIv.layoutParams = bottomLp
    }

    inner class CustomPopRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view)
    data class PopContent(
            var icon: Drawable? = null,
            var subtitle: String
    )

    data class PopConfigure(
            var arrowMarginRight: Float = 0F,
            var marginTop: Float = 0F,
            var marginRight: Float = 0F,
            var itemPaddingLeft: Float = 0F,
            var itemPaddingTop: Float = 0F,
            var itemPaddingRight: Float = 0F,
            var itemPaddingBottom: Float = 0F,
            var itemDrawablePadding: Float = 0F,
            var itemTextSize: Float = 0F,
            var backgroundColor: String = "")

    enum class PopCorner {
        TOP, BOTTOM, BOTH_NOT_SHOW
    }

    object Builder {
        private var itemAction: ((Int, PopContent, PopupWindow) -> Unit)? = null
        private var popCorner: PopCorner = PopCorner.BOTH_NOT_SHOW
        private var popConfigure = PopConfigure()
        fun addPopWindowConfigure(configure: PopConfigure): Builder {
            popConfigure = configure
            return this
        }

        fun setPopCorner(popCorner: PopCorner): Builder {
            this.popCorner = popCorner
            return this
        }

        fun addItemAction(action: (Int, PopContent, PopupWindow) -> Unit): Builder {
            itemAction = action
            return this
        }

        fun builder(context: Context): CustomPopWindow {
            return CustomPopWindow(context, popConfigure, popCorner, itemAction)
        }
    }

    fun dip2px(dpValue: Float): Int {
        val density: Float = Resources.getSystem().displayMetrics.density
        return (0.5f + dpValue * density).toInt()
    }
}

