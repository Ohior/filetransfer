package com.example.filetransfer.adapter

import android.graphics.drawable.Drawable

data class ApkDataClass(
    val name: String,
    val icon: Drawable,
    val apk_path: String,
    val apk_size: Long
)
data class ImageDataClass(
    val image_url: String = "",
    val title: String = "",
    val file_location: String = ""
)