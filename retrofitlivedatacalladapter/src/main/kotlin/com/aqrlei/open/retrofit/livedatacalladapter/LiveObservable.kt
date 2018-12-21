package com.aqrlei.open.retrofit.livedatacalladapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import retrofit2.Call

/**
 * @author aqrlei on 2018/12/21
 */
 class LiveObservable<T> : LiveObservableSource {
    var call: Call<*>? = null
    var isCanceled: Boolean = false
        private set
    private var liveData: MutableLiveData<T> = MutableLiveData()

    override fun cancel() {
        isCanceled = true
        call?.cancel()
    }

    fun observable(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
        liveData.observe(lifecycleOwner, Observer(action))
    }

    fun onComplete(data: T) {
        liveData.postValue(data)
    }
}