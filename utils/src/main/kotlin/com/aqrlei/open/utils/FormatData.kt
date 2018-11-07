package com.aqrlei.open.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author  aqrLei on 2018/7/6
 */

const val DATE_FORMAT_YEAR = "yyyy-MM-dd"
const val DATE_FORMAT_MINUTE = "mm:ss"

fun formatDate(time: Long, format: String): String {
    return SimpleDateFormat(format).format(Date(time))
}

fun formatSize(size: Double): String {
    return DecimalFormat("0.0").format(size / (1024 * 1024))
}