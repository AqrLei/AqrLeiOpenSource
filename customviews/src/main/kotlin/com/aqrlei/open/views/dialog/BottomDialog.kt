package com.aqrlei.open.views.dialog

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.aqrlei.open.views.R
import com.aqrlei.open.views.adapter.BottomDialogAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_dialog_bottom.view.*

/**
 * @author aqrlei on 2018/11/9
 */
class BottomDialog : BottomSheetDialogFragment(),DialogInterface<BottomDialog> {
    companion object {
        @JvmStatic
        fun newInstance() = BottomDialog()

        private const val TAG = "bottomDialog"
    }

    private lateinit var adapter: BottomDialogAdapter<*>
    private var title: SpannableString? = null
    override fun configureTitle(text: String, textColor: Int, textSize: Float): BottomDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configureNegativeButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): BottomDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configurePositiveButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): BottomDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOutCancelable(cancelable: Boolean): BottomDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBackCancelable(cancelable: Boolean): BottomDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

/*    fun configureTitle(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 0F): BottomDialog {
        val tempText = text.toSpannableString().foregroundColor(textColor).absoluteSize(textSize)
        title = tempText
        return this
    }*/


    fun show(manager: FragmentManager?, bottomDialogAdapter: BottomDialogAdapter<*>) {
        adapter = bottomDialogAdapter
        super.show(manager, TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.contentRv.adapter = this.adapter
    }

    override fun onStart() {
        super.onStart()
        //设置隐藏软键盘
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

}