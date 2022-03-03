package com.example.if3210_64

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("check-in")
    fun createPost(
        @Field("qrCode") qrCode: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<QrCodeResponse>
}