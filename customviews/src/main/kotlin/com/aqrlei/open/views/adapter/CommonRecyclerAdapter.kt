package com.aqrlei.open.views.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author  aqrLei on 2018/7/9
 */
abstract class CommonRecyclerAdapter<T>(
        context: Context,
        private val resId: Int,
        protected val data: List<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        var HEADER_START_VIEW = 100
        const val ITEM_VIEW = 1
        var FOOTER_START_VIEW = 200
    }

    private var mItemClickListener: ItemClickListener<T>? = null
    private var mItemLongClickListener: ItemLongClickListener<T>? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val headerView: SparseArray<View> = SparseArray()
    private val footerView: SparseArray<View> = SparseArray()

    fun addHeaderView(@LayoutRes resId: Int) {
        addHeaderView(inflater.inflate(resId, null))
    }

    fun addHeaderView(view: View) {
        if (headerView.indexOfValue(view) < 0) {
            headerView.put(HEADER_START_VIEW + headerView.size(), view)
        }
    }

    fun addFooterView(@LayoutRes resId: Int) {
        addFooterView(inflater.inflate(resId, null))
    }

    fun addFooterView(view: View) {
        if (footerView.indexOfValue(view) < 0) {
            footerView.put(FOOTER_START_VIEW + footerView.size(), view)
        }
    }

    fun setItemClickListener(listener: ItemClickListener<T>) {
        mItemClickListener = listener
    }

    fun setItemClicklistener(onItemClick: (View, Int, T) -> Unit) {
        mItemClickListener = object : ItemClickListener<T> {
            override fun onItemClickListener(view: View, position: Int, data: T) {
                onItemClick(view, position, data)
            }
        }
    }

    fun setItemLongClickListener(listener: ItemLongClickListener<T>) {
        mItemLongClickListener = listener
    }

    fun setItemLongClickListener(onItemLongClick: (View, Int, T) -> Boolean) {
        mItemLongClickListener = object : ItemLongClickListener<T> {
            override fun onItemLongClickListener(view: View, position: Int, data: T) {
                onItemLongClick(view, position, data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < headerView.size() -> headerView.keyAt(position)
            position - headerView.size() < data.size -> ITEM_VIEW
            else -> footerView.keyAt(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            headerView[viewType] != null -> CommonViewHolder(headerView[viewType])
            footerView[viewType] != null -> CommonViewHolder(footerView[viewType])
            else -> CommonViewHolder(inflater.inflate(resId, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW) {
            mItemClickListener?.apply {
                holder.itemView.setOnClickListener { this.onItemClickListener(holder.itemView, position, data[position]) }
            }
            mItemLongClickListener?.apply {
                holder.itemView.setOnLongClickListener {
                    this.onItemLongClickListener(holder.itemView, position, data[position])
                    true
                }
            }
            bindData(holder.itemView, data[position])
        }
    }

    override fun getItemCount() = headerView.size() + data.size + footerView.size()

    abstract fun bindData(view: View, data: T)

    interface ItemClickListener<in T> {
        fun onItemClickListener(view: View, position: Int, data: T)
    }

    interface ItemLongClickListener<in T> {
        fun onItemLongClickListener(view: View, position: Int, data: T)
    }

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}