package com.aqrlei.open.utils

import android.text.SpannableString
import android.text.style.RelativeSizeSpan

/**
 * @author aqrlei on 2018/11/9
 */

fun String.toSpannableString() = SpannableString(this)

fun SpannableString.foregroundColor(color:String,start:Int,end:Int):SpannableString{

    return this
}
fun SpannableString.foregroundColor(color:Int,start:Int,end:Int):SpannableString{

    return this
}

fun SpannableString.backgroundColor(color:String,start:Int,end:Int):SpannableString{

    return this
}
fun SpannableString.backgroundColor(color:Int,start:Int,end:Int):SpannableString{

    return this
}
fun SpannableString.relativeSize(size:Float,start:Int,end:Int):SpannableString{
    val span = RelativeSizeSpan
    return this
}

