package com.vk.imageuploade

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {

    lateinit var  imgView:ImageView
    lateinit var btnChange:Button
    lateinit var btnUpload:Button
    lateinit var imageUri:Uri
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it!!
        imgView.setImageURI(it)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          setup()

        btnChange.setOnClickListener {
            contract.launch("image/*")
        }

    }

    private fun setup() {
        imgView = findViewById(R.id.imgView)
        btnChange = findViewById(R.id.btnChange)
        btnUpload = findViewById(R.id.btnUpload)

        btnUpload.setOnClickListener {
            upload()
        }

    }

    private fun upload() {
        val fileDir = applicationContext.filesDir
        val file = File(fileDir,"image.png")
        val inputStream =contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("profile",file.name,requestBody)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().apply { level =HttpLoggingInterceptor.Level.BODY })

        val retrofit = Retrofit.Builder().baseUrl("https://image-upload-api-retrofit.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build().create(UploadService::class.java)
        CoroutineScope(Dispatchers.IO).launch {

           val response = retrofit.uploadImage(part)
            Log.d("vishwnath",response.toString())

        }



    }

}
