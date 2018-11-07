package com.aqrlei.open.utils

import kotlin.random.Random


/**
 * @author  aqrLei on 2018/7/10
 */

object MathUtil {
    fun random(minLimit: Int, maxLimit: Int): Int {
        val temp = maxLimit - minLimit
        return Random.nextInt(temp) + minLimit
    }
}