package com.example.if3210_64

import com.google.android.gms.common.api.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KProperty


object RetrofitClient {
    private const val BASE_URL = "https://perludilindungi.herokuapp.com/check-in"
/*
    val instance: ApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }
 */
}
