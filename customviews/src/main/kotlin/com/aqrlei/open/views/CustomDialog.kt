package com.aqrlei.open.views

import android.graphics.Color
import android.os.Bundle

import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.layout_dialog_custom.*


/**
 * @author aqrlei on 2018/10/15
 */
class CustomDialog : DialogFragment() {

    companion object {
        private const val TAG = "customDialog"
        fun newInstance() = CustomDialog()
    }

    private var isOutCancelable: Boolean = false
    private var isNegativeButtonShow: Boolean = false
    private var isPositiveButtonShow: Boolean = false

    private var negativeAction: ((View) -> Unit)? = null
    private var positiveAction: ((View) -> Unit)? = null

    private var titleConfigure: TextConfigure? = null
    private var msgConfigure: TextConfigure? = null
    private var negativeButtonConfigure: TextConfigure? = null
    private var positiveButtonConfigure: TextConfigure? = null
    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val width = (metrics.widthPixels * 0.75F).toInt()
            dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.layout_dialog_custom, container, false)
    }

    fun configureTitle(text: String, textColor: String = "", textSize: Float = 0F): CustomDialog {
        titleConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    fun configureMsg(text: String, textColor: String = "", textSize: Float = 0F): CustomDialog {
        msgConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    fun configureNegativeButton(text: String, textColor: String = "", textSize: Float = 0F, action: ((View) -> Unit)? = null): CustomDialog {
        negativeAction = action
        isNegativeButtonShow = true
        negativeButtonConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    fun configurePositiveButton(text: String, textColor: String = "", textSize: Float = 0F, action: ((View) -> Unit)? = null): CustomDialog {
        positiveAction = action
        isPositiveButtonShow = true
        positiveButtonConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    fun setOutCancelable(cancelable: Boolean): CustomDialog {
        isOutCancelable = cancelable
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = isOutCancelable
        titleConfigure?.apply { configureTextView(titleTv, this) }
        msgConfigure?.apply { configureTextView(msgTv, this) }
        negativeButtonConfigure?.apply { configureTextView(negativeButton, this) }
        positiveButtonConfigure?.apply { configureTextView(positiveButton, this) }
        if (isNegativeButtonShow) {
            negativeButton.visibility = View.VISIBLE
            negativeButton.setOnClickListener {
                dismiss()
                negativeAction?.invoke(it)
            }
        }
        if (isPositiveButtonShow) {
            positiveButton.setOnClickListener {
                dismiss()
                positiveAction?.invoke(it)
            }
        }
        dividerView.visibility = if (isNegativeButtonShow && isPositiveButtonShow) View.VISIBLE else View.GONE
    }

    private fun configureTextView(view: TextView, configure: TextConfigure) {
        with(configure) {
            view.visibility = View.VISIBLE
            view.text = text
            if (textColor.isNotEmpty()) view.setTextColor(Color.parseColor(textColor))
            if (textSize != 0F) view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
        }
    }

    fun show(manager: FragmentManager) {
        super.show(manager, TAG)
    }

    data class TextConfigure(var text: String, var textColor: String, var textSize: Float)
}