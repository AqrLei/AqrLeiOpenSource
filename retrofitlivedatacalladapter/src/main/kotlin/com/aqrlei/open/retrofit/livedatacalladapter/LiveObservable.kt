package com.aqrlei.open.retrofit.livedatacalladapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
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

    fun observe(action: (T?) -> Unit) {
        liveData.observeForever(object : Observer<T> {
            override fun onChanged(t: T) {
                action(t)
                liveData.removeObserver(this)
            }
        })
    }

    /**
     * @param mediatorLiveData
     * should invoked observe(LifecycleOwner,Observe) or observeForever(Observe) already,
     * means have a active Observe before call addSource().
     * */
    fun observe(mediatorLiveData: MediatorLiveData<Any>, action: (T?) -> Unit) {
        mediatorLiveData.addSource(this.liveData, Observer(action))
    }

    fun observe(lifecycleOwner: LifecycleOwner, action: (T?) -> Unit) {
        liveData.observe(lifecycleOwner, Observer(action))
    }

    fun onComplete(data: T) {
        liveData.postValue(data)
    }
}