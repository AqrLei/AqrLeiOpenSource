package com.aqrlei.open.retrofit.livedatacalladapter

import retrofit2.*
import java.lang.reflect.Type

/**
 * @author aqrlei on 2018/12/20
 */
class LiveDataCallAdapter<R>(private val type: Type) : CallAdapter<R, LiveObservable<LiveResponse<R>>> {
    override fun adapt(call: Call<R>): LiveObservable<LiveResponse<R>> {
        val liveDataResponse = LiveObservable<LiveResponse<R>>()
        liveDataResponse.call = call
        call.enqueue(LiveDataCallBack(liveDataResponse))
        return liveDataResponse
    }

    override fun responseType(): Type {
        return type
    }

    private class LiveDataCallBack<T>(private val liveObservable: LiveObservable<LiveResponse<T>>) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (call.isCanceled) return
            if (response.isSuccessful) {
                liveObservable.onComplete(LiveResponse.success(response.body()))
            } else {
                liveObservable.onComplete(LiveResponse.error(HttpException(response)))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) return
            liveObservable.onComplete(LiveResponse.error(t))
        }
    }

}