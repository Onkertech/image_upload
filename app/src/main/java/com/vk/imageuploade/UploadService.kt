package com.vk.imageuploade

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface UploadService {

    //https://image-upload-api-retrofit.herokuapp.com

    @Multipart
    @POST("/single")
    suspend fun uploadImage(
        @Part image:MultipartBody.Part): ImageResponse



}