package com.aqrlei.open.utils.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.lang.reflect.Method

/**
 * @author aqrlei on 2018/10/15
 */
class PermissionProcessor<T : AppCompatActivity> private constructor(activity: T) {
    companion object {
        private const val PERMISSION_DENIED_KEY = "permissionDenied"
        private const val PERMISSION_GRANTED_KEY = "permissionGranted"
        private const val PERMISSION_RATIONALE_KEY = "permissionRationale"
        private const val PERMISSION_REQ_CODE = 11
        fun <T : AppCompatActivity> create(activity: T) = PermissionProcessor(activity)
    }

    private val instance: AppCompatActivity
    private val methods = HashMap<String, Method>()
    private val permissionsMap = HashMap<String, List<String>>()
    private val permissions = ArrayList<String>()

    private var rationaleResult: Boolean? = null

    init {
        val clazz = activity.javaClass
        instance = activity
        for (method in clazz.declaredMethods) {
            method.getAnnotation(Required::class.java)?.let {
                permissions.addAll(it.permissions)
                permissionsMap[method.name] = permissions
            }
            method.getAnnotation(OnPermissionGranted::class.java)?.let {
                methods[PERMISSION_GRANTED_KEY] = method
            }
            method.getAnnotation(OnPermissionDenied::class.java)?.let {
                methods[PERMISSION_DENIED_KEY] = method
            }
            method.getAnnotation(OnPermissionRationale::class.java)?.let {
                methods[PERMISSION_RATIONALE_KEY] = method
            }
        }
    }

    fun processPermission(methodName: String) {
        val needRequiredPermissions = ArrayList<String>()
        val tempPermissions = permissionsMap[methodName]
        tempPermissions?.apply {
            for (permission in this) {
                if (ActivityCompat.checkSelfPermission(instance, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(instance, permission)) {
                        rationaleResult = methods[PERMISSION_RATIONALE_KEY]?.invoke(instance, permission) as? Boolean
                    }
                    if (rationaleResult != true) {
                        needRequiredPermissions.add(permission)
                    }
                }
            }
        }
        if (needRequiredPermissions.isNotEmpty()) {
            requestPermission(needRequiredPermissions.toTypedArray())
        }
    }

    private fun requestPermission(needRequiredPermissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            instance.requestPermissions(needRequiredPermissions, PERMISSION_REQ_CODE)
        }
    }

    fun dispatch(reqCode: Int, grantResults: IntArray) {
        if (reqCode == PERMISSION_REQ_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                methods[PERMISSION_DENIED_KEY]?.invoke(instance)
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                methods[PERMISSION_GRANTED_KEY]?.invoke(instance)
            }
        }
    }

}