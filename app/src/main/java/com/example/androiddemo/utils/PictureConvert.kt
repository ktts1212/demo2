package com.example.androiddemo.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun bitmaptoblob( bitmap:Bitmap):ByteArray{
    val stream=ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
    return stream.toByteArray()
}