package com.aqrlei.open.retrofit.livedatacalladapter

/**
 * @author aqrlei on 2018/12/21
 */
class LiveResponse<T> {
    companion object {
        @JvmStatic
        fun <T> success(body: T?): LiveResponse<T> = LiveResponse<T>().apply { response = body }

        @JvmStatic
        fun <T> error(e: Throwable): LiveResponse<T> = LiveResponse<T>().apply { error = e }
    }

    var response: T? = null
        private set
    var error: Throwable? = null
        private set
    var isSuccess: Boolean = false
        get() = response != null && error == null
}