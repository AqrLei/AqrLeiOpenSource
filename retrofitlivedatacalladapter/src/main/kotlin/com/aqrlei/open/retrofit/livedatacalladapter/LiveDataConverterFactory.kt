package com.aqrlei.open.retrofit.livedatacalladapter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author aqrlei on 2018/12/21
 */
class LiveDataConverterFactory : Converter.Factory() {
    companion object {
        @JvmStatic
        fun create() = LiveDataConverterFactory()
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        (type as? ParameterizedType)?.run {
            if (this.rawType == LiveResponse::class.java) {
                val realType = this.actualTypeArguments[0]
                return retrofit.nextResponseBodyConverter<ParameterizedType>(this@LiveDataConverterFactory, realType, annotations)
            }
        }
        return retrofit.nextResponseBodyConverter<ParameterizedType>(this, type, annotations)
    }
}