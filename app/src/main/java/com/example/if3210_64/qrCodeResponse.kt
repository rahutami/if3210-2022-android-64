package com.example.if3210_64

data class QrCodeResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)