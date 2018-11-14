package com.aqrlei.open.views.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqrlei.open.views.R
import com.aqrlei.open.views.adapter.CommonRecyclerAdapter
import com.aqrlei.open.views.util.absoluteSize
import com.aqrlei.open.views.util.foregroundColor
import com.aqrlei.open.views.util.toSpannableString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_dialog_bottom.view.*
import kotlinx.android.synthetic.main.layout_dialog_bottom_default_item.view.*

/**
 * @author aqrlei on 2018/11/9
 */
class BottomDialog : BottomSheetDialogFragment(), DialogInterface<BottomDialog> {
    companion object {
        @JvmStatic
        fun newInstance() = BottomDialog()

        private const val TAG = "bottomDialog"
    }

    private var isOutCancelable: Boolean = false
    private var isBackCancelable: Boolean = false

    private var negativeAction: ((View) -> Unit)? = null
    private var positiveAction: ((View) -> Unit)? = null
    private var neutralAction: ((View) -> Unit)? = null
    private var adapter: CommonRecyclerAdapter<*>? = null
    private var titleText: SpannableString? = null
    private var negativeText: SpannableString? = null
    private var positiveText: SpannableString? = null
    private var neutralText: SpannableString? = null
    override fun configureTitle(text: String, textColor: Int, textSize: Float): BottomDialog {
        titleText = text.toSpannableString().foregroundColor(textColor).absoluteSize(textSize)
        return this
    }

    override fun configureNegativeButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): BottomDialog {
        negativeAction = action
        negativeText = text.toSpannableString().foregroundColor(textColor).absoluteSize(textSize)
        return this
    }

    fun configureNeutralButton(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 12F, action: ((View) -> Unit)? = null): BottomDialog {
        neutralAction = action
        neutralText = text.toSpannableString().foregroundColor(textColor).absoluteSize(textSize)
        return this
    }

    fun refreshContent() {
        adapter?.notifyDataSetChanged()
    }

    override fun configurePositiveButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): BottomDialog {
        positiveAction = action
        positiveText = text.toSpannableString().foregroundColor(textColor).absoluteSize(textSize)
        return this
    }

    override fun setOutCancelable(cancelable: Boolean): BottomDialog {
        isOutCancelable = cancelable
        return this
    }

    override fun setBackCancelable(cancelable: Boolean): BottomDialog {
        isBackCancelable = cancelable
        return this
    }

    fun show(manager: FragmentManager?, bottomDialogAdapter: CommonRecyclerAdapter<*>) {
        adapter = bottomDialogAdapter
        super.show(manager, TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.contentRv.adapter = this.adapter
        isCancelable = isBackCancelable
        setOutCancelable(isOutCancelable)
        titleText?.let {
            with(view.dialogTitleTv) {
                text = it
                visibility = View.VISIBLE
            }

        }
        negativeText?.let {
            with(view.negativeButton) {
                text = it
                visibility = View.VISIBLE
                setOnClickListener { view ->
                    dialog.dismiss()
                    negativeAction?.invoke(view)
                }
            }
        }
        positiveText?.let {
            with(view.positiveButton) {
                text = it
                visibility = View.VISIBLE
                setOnClickListener { view ->
                    dialog.dismiss()
                    positiveAction?.invoke(view)
                }
            }
        }
        neutralText?.let {
            with(view.neutralButton) {
                text = it
                visibility = View.VISIBLE
                setOnClickListener { view ->
                    dialog.dismiss()
                    neutralAction?.invoke(view)
                }
            }
        }
        adapter?.let {
            with(view.contentRv) {
                layoutManager = LinearLayoutManager(context)
                adapter = it
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //设置隐藏软键盘
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    class DefaultBottomDialogAdapter(context: Context,
                                     data: List<String>)
        : CommonRecyclerAdapter<String>(context, R.layout.layout_dialog_bottom_default_item, data) {
        override fun bindData(view: View, data: String) {
            view.contentTv.text = data
        }
    }
}