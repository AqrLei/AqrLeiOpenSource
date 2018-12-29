package com.aqrlei.open.opensource.netlivedatacalladapter.sample

/**
 * @author aqrlei on 2018/12/21
 */
class BaseRespBean<T>(
        var errorCode: String,
        var errorMsg: String,
        var data: T
)