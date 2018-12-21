package com.aqrlei.open.opensource.netlivedatacalladapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import java.lang.reflect.Type

/**
 * @author aqrlei on 2018/12/20
 */
class LiveDataCallAdapter<R>(private val type: Type) : CallAdapter<R, LiveData<LiveResponse<R>>> {
    override fun adapt(call: Call<R>): LiveData<LiveResponse<R>> {
        val liveDataResponse = MutableLiveData<LiveResponse<R>>()
        call.enqueue(LiveDataCallBack(liveDataResponse))
        return liveDataResponse
    }

    override fun responseType(): Type {
        return type
    }

    private class LiveDataCallBack<T>(private val liveData: MutableLiveData<LiveResponse<T>>) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (call.isCanceled) return
            if (response.isSuccessful) {
                liveData.postValue(LiveResponse.success(response.body()))
            } else {
                liveData.postValue(LiveResponse.error(HttpException(response)))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) return
            liveData.postValue(LiveResponse.error(t))
        }
    }

}