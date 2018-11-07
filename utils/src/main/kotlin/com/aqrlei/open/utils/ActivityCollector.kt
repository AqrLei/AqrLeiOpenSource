package  com.aqrlei.open.utils

import android.app.Activity

/**
 * created by AqrLei at 13:21 on 星期四, 六月 14, 2018
 */
object ActivityCollector {
    private val activities = ArrayList<Activity>()
    fun add(activity: Activity) {
        activities.add(activity)
    }

    fun remove(activity: Activity) {
        activities.remove(activity)
    }

    fun killApp() {
        activities.filter { !it.isFinishing }
                .forEach { it.finish() }
        activities.clear()
    }
}