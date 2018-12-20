package com.aqrlei.open.opensource.netlivedatacalladapter

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * @author aqrlei on 2018/12/20
 */
class LiveDataCallAdapter<R>(private val type: Type) : CallAdapter<R, LiveData<Response<R>>> {
    override fun adapt(call: Call<R>): LiveData<Response<R>> {
        val liveDataResponse = NetMutableLiveData<Response<R>>()
        call.enqueue(LiveDataCallBack(liveDataResponse))
        return liveDataResponse
    }

    override fun responseType(): Type {
        return type
    }

    private class LiveDataCallBack<T>(private val liveData: NetMutableLiveData<Response<T>>) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (call.isCanceled) return
            liveData.postValue(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) return
            liveData.error = t
        }
    }

}