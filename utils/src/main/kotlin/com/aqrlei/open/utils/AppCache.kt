package com.aqrlei.open.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.lang.ref.WeakReference

/**
 * @author  aqrLei on 2018/4/24
 */
@Suppress("unused")
class AppCache private constructor() {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var shared: SharedPreferences

    companion object {
        fun get(): AppCache {
            return Holder.cache
        }

        private var reference: WeakReference<Context>? = null
        private var name: String = ""
        fun init(context: Context, name: String = "") {
            reference = WeakReference(context)
            this.name = name
        }
    }

    object Holder {
        val cache = AppCache()
    }

    init {
        reference?.get()?.let {
            shared = it.getSharedPreferences(name, Context.MODE_PRIVATE)
            editor = shared.edit().apply { apply() }
        } ?: Log.e(logTag, "Null Object Error")
    }

    fun putString(key: String, value: String): AppCache {
        editor.putString(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): AppCache {
        editor.putFloat(key, value)
        return this
    }

    fun putLong(key: String, value: Long): AppCache {
        editor.putLong(key, value)
        return this
    }

    fun putInt(key: String, value: Int): AppCache {
        editor.putInt(key, value)
        return this
    }

    fun putBoolean(key: String, value: Boolean): AppCache {
        editor.putBoolean(key, value)
        return this
    }

    fun commit() {
        editor.commit()
    }

    fun remove(key: String): AppCache {
        editor.remove(key)
        return this
    }

    fun removeAll(): AppCache {
        editor.clear()
        return this
    }

    fun getString(key: String, defValue: String): String = shared.getString(key, defValue) ?: ""
    fun getFloat(key: String, defValue: Float): Float = shared.getFloat(key, defValue)
    fun getLong(key: String, defValue: Long): Long = shared.getLong(key, defValue)
    fun getInt(key: String, defValue: Int): Int = shared.getInt(key, defValue)
    fun getBoolean(key: String, defValue: Boolean): Boolean = shared.getBoolean(key, defValue)
}