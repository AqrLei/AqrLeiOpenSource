package com.aqrlei.open.opensource.netlivedatacalladapter

import androidx.lifecycle.MutableLiveData

/**
 * @author aqrlei on 2018/12/20
 */
class NetMutableLiveData<T> : MutableLiveData<T>(), NetLiveDataSource {
    var error: Throwable? = null
    override fun onError(e: Throwable) {
        this.error = e
    }
}