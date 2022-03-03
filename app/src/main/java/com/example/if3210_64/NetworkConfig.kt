package com.example.if3210_64

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class NetworkConfig {
    // set interceptor
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://perludilindungi.herokuapp.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(Api::class.java)
}

interface Api {

    @FormUrlEncoded
    @POST("check-in")
    fun createPost(
        @Field("qrCode") qrCode: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<QrCodeResponse>
}