package com.example.if3210_64

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetailFaskesActivity : AppCompatActivity() {
    private lateinit var faskes: Faskes
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_faskes)

        db = DBHelper(this, null)

        val faskesName = findViewById<TextView>(R.id.faskes_name)
        val faskesPhone = findViewById<TextView>(R.id.faskes_phone)
        val faskesCode = findViewById<TextView>(R.id.faskes_code)
        val faskesAddress = findViewById<TextView>(R.id.faskes_address)
        val faskesType = findViewById<TextView>(R.id.faskes_type)
        val faskesStatus = findViewById<TextView>(R.id.status_faskes)
        val logoStatus = findViewById<ImageView>(R.id.status_logo)

        val bundle : Bundle? = intent.extras
        val faskesDetail : Faskes = bundle!!.getSerializable("faskes") as Faskes
        faskes = faskesDetail

        faskesName.text = faskesDetail.nama
        faskesPhone.text = faskesDetail.telp
        faskesCode.text = faskesDetail.kode
        faskesAddress.text = faskesDetail.alamat
        if(faskesDetail.jenis_faskes == ""){
            faskesType.text = "Undefined"
        } else {
            faskesType.text = faskesDetail.jenis_faskes
        }
        faskesStatus.text = faskesDetail.status

        if(faskesDetail.status.equals("Siap Vaksinasi", true)){
            logoStatus.setImageResource(R.drawable.green_check)
        } else {
            logoStatus.setImageResource(R.drawable.red_cross)
        }

        if(faskesDetail.jenis_faskes.equals("rumah sakit", true)){
            faskesType.setBackgroundResource(R.color.rsColor)
        } else if(faskesDetail.jenis_faskes.equals("puskesmas", true)){
            faskesType.setBackgroundResource(R.color.puskesmasColor)
        } else if(faskesDetail.jenis_faskes.equals("kkp", true)){
            faskesType.setBackgroundResource(R.color.kkpColor)
        } else if(faskesDetail.jenis_faskes.equals("klinik", true)){
            faskesType.setBackgroundResource(R.color.klinikColor)
        } else {
            faskesType.setBackgroundResource(R.color.otherColor)
        }
    }

    @SuppressLint("Range")
    fun onClickBookmark(view: View){
        if(db.checkDuplicate(faskes)){
            Toast.makeText(this, faskes.nama + " has already added", Toast.LENGTH_LONG).show()
            return
        }

        db.addBokmark(faskes)
    }

    fun onClickMap(view: View){
        // Create a Uri from an intent string. Use the result to create an Intent.
        val uri = "geo:${faskes.latitude},${faskes.longitude}?q=${Uri.encode(faskes.nama)}?z=20"
        val gmmIntentUri = Uri.parse(uri)

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }
}