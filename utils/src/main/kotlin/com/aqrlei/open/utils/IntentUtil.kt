package com.aqrlei.open.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import java.io.File


/**
 * @author aqrlei on 2018/10/15
 */
object IntentUtil {

    /**
     * 查询是否有Activity可以响应Intent的跳转
     *
     * @param context 上下文
     * @param intent  需要查询的Intent
     * @return
     */
    fun queryActivities(context: Context, intent: Intent): Boolean {
        return context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY) != null
    }

    /**
     * 跳转到设置界面
     * @param activity
     * @param reqCode
     */
    fun toAppSetting(activity: Activity, reqCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", activity.packageName, null))
        if (queryActivities(activity, intent)) {
            activity.startActivityForResult(intent, reqCode)
        }
    }


    /**
     * 发送短信
     * @param tel     电话uri
     * @param content 短信内容
     */
    fun toSendSms(context: Context, tel: Uri, content: String) {
        val intent = Intent(Intent.ACTION_SENDTO, tel)
        intent.putExtra("sms_body", content)
        if (queryActivities(context, intent)) {
            context.startActivity(intent)
        }
    }

    /**
     * 跳转到拨号界面
     */
    fun toDialApp(context: Context, tel: Uri) {
        val intent = Intent(Intent.ACTION_DIAL, tel)
        if (queryActivities(context, intent)) {
            context.startActivity(intent)
        }
    }

    /**
     * 跳转到市场app
     */
    fun toMarketApp(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName))
        if (queryActivities(context, intent)) {
            context.startActivity(Intent.createChooser(intent, "请选择应用市场"))
        }
    }

    /**
     * 跳转到系统相机 ，不需要Camera权限
     * @param fileUri 拍摄图片存放的位置
     */
    fun toCameraApp(context: Activity, fileUri: Uri, reqCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)//指定拍摄图片后的文件存储位置
        if (queryActivities(context, intent)) {
            context.startActivityForResult(intent, reqCode)
        }
    }

    /**
     * 跳系统图库
     */
    fun toGetContentPhoto(activity: Activity, reqCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (queryActivities(activity, intent)) {
            activity.startActivityForResult(intent, reqCode)
        }
    }


    fun toPickContentImage(activity: Activity, reqCode: Int) {
        val intent = Intent(Intent.ACTION_PICK).setType("image/*")
        activity.startActivityForResult(intent, reqCode)
    }

    /**
     * 跳转到联系人
     * @param fragment
     */
    fun toContactApp(context: Activity, fragment: Fragment?, reqCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        if (queryActivities(context, intent)) {
            fragment?.startActivityForResult(intent, reqCode)
                    ?: context.startActivityForResult(intent, reqCode)
        }
    }

    fun toCropPic(context: Activity, uri: Uri, size: Int, saveFile: File, reqCode: Int) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true")

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size)
        intent.putExtra("outputY", size)
        intent.putExtra("return-data", false)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile))

        val resultIntent = Intent.createChooser(intent, "请选择剪切程序")
        if (queryActivities(context, resultIntent)) {
            context.startActivityForResult(resultIntent, reqCode)
        }

    }
}