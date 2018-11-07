package com.aqrlei.open.utils

import java.util.*

/**
 * @author  aqrLei on 2018/7/10
 */

object MathUtil {
    fun random(minLimit: Int, maxLimit: Int): Int {
        val random = Random()
        val temp = maxLimit - minLimit
        return random.nextInt(temp) + minLimit
    }
}