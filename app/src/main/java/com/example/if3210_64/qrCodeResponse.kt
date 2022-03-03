package com.example.if3210_64

data class QrCodeResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val `data`: Data
)