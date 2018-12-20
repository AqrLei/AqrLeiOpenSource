package com.aqrlei.open.opensource.netlivedatacalladapter.sample

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author aqrlei on 2018/12/20
 */

class PhoneRepository() {
    private val phoneService = NetHelper.get().createApiService(PhoneService::class.java)
    fun fetchPhoneInfo(tel: String) = phoneService.fetchPhoneInfo(tel)
}

interface PhoneService {
    @GET("cc/json/mobile_tel_segment.htm")
    fun fetchPhoneInfo(@Query("tel") tel: String): LiveData<Response<PhoneRespBean>>
}

