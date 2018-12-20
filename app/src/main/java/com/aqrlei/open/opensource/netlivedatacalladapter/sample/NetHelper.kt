package com.aqrlei.open.opensource.netlivedatacalladapter.sample

import com.aqrlei.open.opensource.netlivedatacalladapter.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author aqrlei on 2018/12/20
 */

class NetHelper private constructor() {
    companion object {
        fun get(): NetHelper {
            return Holder.instance
        }
    }

    private val apiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(3000L, TimeUnit.MILLISECONDS)
                        .connectTimeout(3000L, TimeUnit.MILLISECONDS)
                        .build())
                .baseUrl("https://tcc.taobao.com/")
                .build()
    }

    fun <T> createApiService(clazz: Class<T>): T = apiRetrofit.create(clazz)

    private object Holder {
        val instance = NetHelper()
    }

}