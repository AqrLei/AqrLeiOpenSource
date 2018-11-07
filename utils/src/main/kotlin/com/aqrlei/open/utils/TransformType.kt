package com.aqrlei.open.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * @author  aqrLei on 2018/4/24
 */
@Suppress("unused")
fun byteArrayToSequence(data: ByteArray): Any? {
    val result: Any?
    val arrayInputStream = ByteArrayInputStream(data)
    var inputStream: ObjectInputStream? = null

    try {
        inputStream = ObjectInputStream(arrayInputStream)
        result = inputStream.readObject()
    } finally {
        arrayInputStream.close()
        inputStream?.close()
    }
    return result
}

@Suppress("unused")
fun sequenceToByteArray(data: Any): ByteArray? {
    val result: ByteArray?
    val arrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(arrayOutputStream)

    try {
        objectOutputStream.writeObject(data)
        objectOutputStream.flush()
        result = arrayOutputStream.toByteArray()
    } finally {
        arrayOutputStream.close()
        objectOutputStream.close()
    }
    return result
}

@Suppress("unused")
fun byteArrayToBitmap(byte: ByteArray?, offset: Int = 0): Bitmap? {
    return byte?.let {
        BitmapFactory.decodeByteArray(it, offset, it.size)
    }
}

@Suppress("unused")
fun bitmapToDrawable(bitmap: Bitmap?, res: Resources? = null): Drawable? {
    return bitmap?.let {
        BitmapDrawable(res, it)
    }
}

@Suppress("unused")
fun byteArrayToDrawable(byte: ByteArray?, offset: Int = 0): Drawable? {
    return byte?.let {
        BitmapDrawable(null, BitmapFactory.decodeByteArray(it, offset, it.size))
    }
}