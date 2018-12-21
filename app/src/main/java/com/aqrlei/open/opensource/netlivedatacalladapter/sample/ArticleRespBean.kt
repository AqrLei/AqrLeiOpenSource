package com.aqrlei.open.opensource.netlivedatacalladapter.sample

/**
 * @author aqrlei on 2018/12/20
 */
data class ArticleRespBean(
        var curPage: String?,
        var offset: String?,
        var over: String?,
        var pageCount: String?,
        var size: String?,
        var total: String?,
        var datas: List<Any>?
)