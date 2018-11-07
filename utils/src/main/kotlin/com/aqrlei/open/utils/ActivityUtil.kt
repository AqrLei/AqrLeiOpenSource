package com.aqrlei.open.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author  aqrLei on 2018/8/1
 */
object ActivityUtil {
    fun addFragmentToActivity(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.remove(fragment)
        }
        transaction.add(frameId, fragment)
        transaction.commit()
    }
}