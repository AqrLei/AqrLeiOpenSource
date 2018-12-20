package com.aqrlei.open.opensource.netlivedatacalladapter.sample

/**
 * @author aqrlei on 2018/12/20
 */
data class PhoneRespBean(
        var mts:String,
        var province:String,
        var catName:String,
        var telString:String,
        var areaVid:String,
        var ispVid:String,
        var carrier:String
) {


    /*
__GetZoneResult_ = {
    mts:'1337241',
    province:'浙江',
    catName:'中国电信',
    telString:'13372414424',
    areaVid:'30510',
    ispVid:'138238560',
    carrier:'浙江电信'
}*/
}