package  com.aqrlei.open.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author  aqrLei on 2018/7/3
 */

fun <T> observerData(action: () -> T): T? {
    var tempData: T? = null
    try {
        val job = GlobalScope.launch {
            tempData = action()
        }
        while (job.isActive);
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return tempData
}
