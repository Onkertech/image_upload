package com.vk.imageuploade

data class ImageResponse(
    val destination:String,
    val encoding:String,
    val fieldname:String,
    val filename:String,
    val mimetype:String,
    val originalname:String,
    val size:String
)
