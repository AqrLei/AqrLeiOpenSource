package com.aqrlei.open.retrofit.livedatacalladapter

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author aqrlei on 2018/12/20
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        fun create() = LiveDataCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveObservable::class.java) {
            throw IllegalStateException("Response must be parametrized as LiveObservable<LiveResponse>")
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException("Response must be parametrized as LiveObservable<LiveResponse>")
        }

        return LiveDataCallAdapter<ParameterizedType>(getParameterUpperBound(0, returnType))
    }
}