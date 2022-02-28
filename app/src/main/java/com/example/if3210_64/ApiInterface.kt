package com.example.if3210_64

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("check-in")
    fun postQrCode(
        @Body
        qrCode: String,
        latitude: Double,
        longitude: Double
    ): Response<QrCodeResponse>
}