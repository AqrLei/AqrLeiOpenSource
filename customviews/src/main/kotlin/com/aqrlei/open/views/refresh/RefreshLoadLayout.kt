package com.aqrlei.open.views.refresh

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by AqrLei on 2019-05-14
 */
class RefreshLoadLayout @JvmOverloads constructor(context: Context,
                                    attrs:AttributeSet?=null):LinearLayout(context, attrs){
    init {
        this.orientation = VERTICAL
    }
}