package com.aqrlei.open.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import java.text.DateFormat
import java.util.*

/**
 * @author aqrlei on 2018/11/14
 */
object DialogUtil {
    /**
     * @param context [android.content.Context]
     * */
    fun simpleDialogBuilder(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    /**
     * @param singleChoiceItems
     * @param selectItemPosition
     * @param selectAction 点击后要响应的自定义Action
     * */
    fun singleChoiceDialogBuilder(
            context: Context,
            singleChoiceItems: Array<String>,
            selectItemPosition: Int,
            selectAction: (Int) -> Unit): AlertDialog.Builder {
        return AlertDialog.Builder(context)
                .setSingleChoiceItems(singleChoiceItems, selectItemPosition) { dialog, which ->
                    dialog.dismiss()
                    selectAction(which)
                }
    }

    fun multiChoiceItemsDialogBuilder(
            context: Context,
            multiChoiceItems: Array<String>,
            checkedItems: BooleanArray? = null,
            checkedAction: (Int, Boolean) -> Unit): AlertDialog.Builder? {
        if (checkedItems != null) {
            if (checkedItems.size != multiChoiceItems.size) {
                return null
            }
        }
        return AlertDialog.Builder(context)
                .setMultiChoiceItems(multiChoiceItems, checkedItems) { dialog, which, isChecked ->
                    dialog.dismiss()
                    checkedAction(which, isChecked)
                }
    }

    fun dataPickerDialog(context: Context,
                         action: (String) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(context, { _, year, month, dayOfMonth ->
            calendar?.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            action(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }


    fun timePickerDialog(context: Context,
                         action: (String) -> Unit): TimePickerDialog {
        val calendar = Calendar.getInstance()
        return TimePickerDialog(context, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            action(DateFormat.getTimeInstance(DateFormat.MEDIUM).format(calendar.time))

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
    }

    /**
     * @param[anchor] 作为锚点的View
     * @param[menuResId]
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun popupMenu(context: Context,
                  anchor: View,
                  @MenuRes menuResId: Int,
                  action: (MenuItem) -> Unit
    ) {
        PopupMenu(context, anchor).apply {
            menuInflater.inflate(menuResId, this.menu)
            setOnMenuItemClickListener { item ->
                action(item)
                false
            }
        }
    }
}