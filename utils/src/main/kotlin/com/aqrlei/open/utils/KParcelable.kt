package com.aqrlei.open.utils

import android.os.Parcel
import android.os.Parcelable

/**
 * @author  aqrLei on 2018/7/3
 */
interface KParcelable : Parcelable {
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int)

}

inline fun <reified T> generateParcelable(crossinline parcel: (Parcel) -> T): Parcelable.Creator<T> {
    return object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel): T = parcel(source)

        override fun newArray(size: Int): Array<T?> = arrayOfNulls(size)
    }
}