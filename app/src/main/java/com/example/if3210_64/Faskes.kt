package com.example.if3210_64

import java.io.Serializable

data class Faskes(
    val id: Int,
    val kode: String,
    val nama: String,
    val kota: String,
    val provinsi: String,
    val alamat: String,
    val latitude: String,
    val longitude: String,
    val telp: String,
    val jenis_faskes: String,
    val kelas_rs: String,
    val status: String
) : Serializable