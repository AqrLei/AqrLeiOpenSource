package com.aqrlei.open.opensource.netlivedatacalladapter.sample

import androidx.lifecycle.LiveData
import com.aqrlei.open.opensource.netlivedatacalladapter.LiveResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author aqrlei on 2018/12/20
 */

class ArticleRepository() {
    private val phoneService = NetHelper.get().createApiService(ArticleService::class.java)
    fun fetchPhoneInfo(id: String, page: String) = phoneService.fetchPhoneInfo(id, page)
}

interface ArticleService {
    @GET("wxarticle/list/{id}/{page}/json")
    fun fetchPhoneInfo(@Path("id") id: String, @Path("page") page: String): LiveData<LiveResponse<BaseRespBean<ArticleRespBean>>>
}

