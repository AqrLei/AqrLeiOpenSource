package com.aqrlei.open.views.dialog

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aqrlei.open.views.KParcelable
import com.aqrlei.open.views.R
import com.aqrlei.open.views.generateCreator
import kotlinx.android.synthetic.main.layout_dialog_iphone.*


/**
 * @author aqrlei on 2018/10/15
 */
class IPhoneStyleDialog : DialogFragment(), DialogInterface<IPhoneStyleDialog> {

    companion object {
        private const val TAG = "IPhoneStyleDialog"
        fun newInstance() = IPhoneStyleDialog()
        private var negativeAction: ((View) -> Unit)? = null
        private var positiveAction: ((View) -> Unit)? = null
    }

    private var isMCancelable: Boolean = false
    private var isNegativeButtonShow: Boolean = false
    private var isPositiveButtonShow: Boolean = false


    private var titleConfigure: TextConfigure? = null
    private var msgConfigure: TextConfigure? = null
    private var negativeButtonConfigure: TextConfigure? = null
    private var positiveButtonConfigure: TextConfigure? = null


    fun show(manager: FragmentManager) {
        super.show(manager, TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.IPhoneDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.layout_dialog_iphone, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val width = (metrics.widthPixels * 0.75F).toInt()
            dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun configureTitle(text: String, textColor: Int, textSize: Float): IPhoneStyleDialog {
        titleConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    fun configureMsg(text: String, textColor: Int = Color.parseColor("#333333"), textSize: Float = 0F): IPhoneStyleDialog {
        msgConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    override fun configureNegativeButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): IPhoneStyleDialog {
        negativeAction = action
        isNegativeButtonShow = true
        negativeButtonConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    override fun configurePositiveButton(text: String, textColor: Int, textSize: Float, action: ((View) -> Unit)?): IPhoneStyleDialog {
        positiveAction = action
        isPositiveButtonShow = true
        positiveButtonConfigure = TextConfigure(text, textColor, textSize)
        return this
    }

    override fun setMCancelable(cancelable: Boolean): IPhoneStyleDialog {
        isMCancelable = cancelable
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            isMCancelable = getBoolean(DialogInterface.CANCELABLE_KEY)
            titleConfigure = getParcelable(DialogInterface.TITLE_KEY)
            negativeButtonConfigure = getParcelable(DialogInterface.NEGATIVE_KEY)
            positiveButtonConfigure = getParcelable(DialogInterface.POSITIVE_KEY)
            isNegativeButtonShow = getBoolean(DialogInterface.NEGATIVE_SHOW_KEY)
            isPositiveButtonShow = getBoolean(DialogInterface.POSITIVE_SHOW_KEY)
            msgConfigure = getParcelable(DialogInterface.MESSAGE_KEY)
        }
        isCancelable = isMCancelable
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (dialog?.isShowing == true) {
            with(outState) {
                putBoolean(DialogInterface.CANCELABLE_KEY, isMCancelable)
                putBoolean(DialogInterface.NEGATIVE_SHOW_KEY, isNegativeButtonShow)
                putBoolean(DialogInterface.POSITIVE_SHOW_KEY, isPositiveButtonShow)
                putParcelable(DialogInterface.TITLE_KEY, titleConfigure)
                putParcelable(DialogInterface.MESSAGE_KEY, msgConfigure)
                putParcelable(DialogInterface.NEGATIVE_KEY, negativeButtonConfigure)
                putParcelable(DialogInterface.POSITIVE_KEY, positiveButtonConfigure)
            }
        }
    }

    private fun configureTextView(view: TextView, configure: TextConfigure) {
        with(configure) {
            view.visibility = View.VISIBLE
            view.text = text
            view.setTextColor(textColor)
            if (textSize != 0F) view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
        }
    }


    data class TextConfigure(var text: String, var textColor: Int, var textSize: Float) : KParcelable {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<TextConfigure> = generateCreator(::TextConfigure)
        }

        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readFloat()
        )

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(text)
            writeInt(textColor)
            writeFloat(textSize)
        }
    }
}