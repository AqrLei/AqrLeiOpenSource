package com.aqrlei.open.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * @author aqrlei on 2018/11/14
 */
object DialogUtil {
    //TODO
    fun simpleDialogBuilder(context: Context) {
        AlertDialog.Builder(context)
    }

    fun singleChoiceDialogBuilder(
            context: Context,
            singleChoiceItems: Array<String>,
            selectItemPosition: Int,
            selectAction: (Int) -> Unit) {
        AlertDialog.Builder(context)
                .setSingleChoiceItems(singleChoiceItems, selectItemPosition) { dialog, which ->
                    dialog.dismiss()
                    selectAction(which)
                }
    }

    fun multiChoiceItemsDialogBuilder(
            context: Context,
            multiChoiceItems: Array<String>,
            checkedItems: BooleanArray? = null,
            checkedAction: (Int, Boolean) -> Unit) {
        if (checkedItems != null) {
            if (checkedItems.size != multiChoiceItems.size) {
                return
            }
        }
        AlertDialog.Builder(context)
                .setMultiChoiceItems(multiChoiceItems, checkedItems) { dialog, which, isChecked ->
                    dialog.dismiss()
                    checkedAction(which, isChecked)
                }
    }

    fun dataPickerDialog(){

    }

    /* @Override







             case R.id.btn_dialog_5:
             ProgressDialog progressDialog = new ProgressDialog(getContext());
             progressDialog.setMessage(getString(R.string.main_dialog_progress_title));
             progressDialog.show();
             break;

             case R.id.btn_dialog_6:
             final ProgressDialog horizontalProgressDialog = new ProgressDialog(getContext());
             horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
             horizontalProgressDialog.setMessage(getString(R.string.main_dialog_progress_title));
             horizontalProgressDialog.setCancelable(false);
             horizontalProgressDialog.setMax(100);
             horizontalProgressDialog.show();

             new Thread(new Runnable() {
                 int progress = 0;

                 @Override
                 public void run() {
                     while (progress <= 100) {
                         horizontalProgressDialog.setProgress(progress);
                         if (progress == 100) {
                             horizontalProgressDialog.dismiss();
                         }
                         try {
                             Thread.sleep(35);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         progress++;
                     }
                 }
             }).start();
             break;

             case R.id.btn_dialog_7:
             DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                     calendar.set(Calendar.YEAR, year);
                     calendar.set(Calendar.MONTH, monthOfYear);
                     calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                     String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                     btn_dialog_7.setText(date);
                 }
             }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
             datePickerDialog.show();
             break;

             case R.id.btn_dialog_8:
             TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                 @Override
                 public void onTimeSet(TimePicker timePicker, int i, int i1) {
                     calendar.set(Calendar.HOUR_OF_DAY, i);
                     calendar.set(Calendar.MINUTE, i1);
                     String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                     btn_dialog_8.setText(time);
                 }
             }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
             timePickerDialog.show();
             break;

             case R.id.btn_dialog_9:
             final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getContext());
             View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
             Button btn_dialog_bottom_sheet_ok = dialogView.findViewById(R.id.btn_dialog_bottom_sheet_ok);
             Button btn_dialog_bottom_sheet_cancel = dialogView.findViewById(R.id.btn_dialog_bottom_sheet_cancel);
             ImageView img_bottom_dialog = dialogView.findViewById(R.id.img_bottom_dialog);
             Glide.with(getContext()).load(R.drawable.bottom_dialog).into(img_bottom_dialog);
             mBottomSheetDialog.setContentView(dialogView);

             btn_dialog_bottom_sheet_ok.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mBottomSheetDialog.dismiss();
                 }
             });
             btn_dialog_bottom_sheet_cancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mBottomSheetDialog.dismiss();
                 }
             });
             mBottomSheetDialog.show();
             break;

             case R.id.btn_dialog_10:
             final Dialog fullscreenDialog = new Dialog(getContext(), R.style.DialogFullscreen);
             fullscreenDialog.setContentView(R.layout.dialog_fullscreen);
             ImageView img_full_screen_dialog = fullscreenDialog.findViewById(R.id.img_full_screen_dialog);
             Glide.with(getContext()).load(R.drawable.google_assistant).into(img_full_screen_dialog);
             ImageView img_dialog_fullscreen_close = fullscreenDialog.findViewById(R.id.img_dialog_fullscreen_close);
             img_dialog_fullscreen_close.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     fullscreenDialog.dismiss();
                 }
             });
             fullscreenDialog.show();
             break;

             case R.id.btn_dialog_11:
             PopupMenu popupMenu = new PopupMenu(getContext(), btn_dialog_11);
             popupMenu.getMenuInflater().inflate(R.menu.popup_menu_main, popupMenu.getMenu());
             popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     return false;
                 }
             });
             popupMenu.show();
             break;

         }*/
}