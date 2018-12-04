package com.aqrlei.open.utils

import android.app.ActivityManager
import android.content.Context

/**
 * @author aqrlei on 2018/12/4
 */
fun getProcessId() = android.os.Process.myPid()

fun getTaskId() = android.os.Process.myTid()
fun getUid() = android.os.Process.myUid()
fun getProcessName(context: Context): String {
    val pid = getProcessId()
    val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val list = manager.runningAppProcesses
    val iterator = list.iterator()
    while (iterator.hasNext()) {
        val info = iterator.next() as ActivityManager.RunningAppProcessInfo
        if (info.pid == pid) {
            return info.processName
        }
    }
    return ""
}