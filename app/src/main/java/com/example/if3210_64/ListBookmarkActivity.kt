package com.example.if3210_64

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListBookmarkActivity : AppCompatActivity() {
    private var adapter: FaskesRecyclerAdapter? = null
    var faskesArray = ArrayList<Faskes>()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_bookmark)
        layoutManager = LinearLayoutManager(this)

        db = DBHelper(this, null)

//        recycler view stuff
        val bookmarkRecyclerView = findViewById<RecyclerView>(R.id.bookmarkRecyclerView)
        bookmarkRecyclerView.layoutManager = layoutManager
        adapter = FaskesRecyclerAdapter()
        adapter!!.setOnItemClickListener(object: FaskesRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@ListBookmarkActivity, DetailFaskesActivity::class.java)
                intent.putExtra("faskes", faskesArray[position])
                startActivity(intent)
            }

        })
        bookmarkRecyclerView.adapter = adapter
        db = DBHelper(this, null)
        fetchBookmark()
        if(faskesArray.size > 0){
            val warningBookmark = findViewById<TextView>(R.id.warning_bookmark)
            warningBookmark.setVisibility(View.GONE)
        }
    }

    @SuppressLint("Range")
    fun fetchBookmark(){
        val cursor = db.getBookmark()

        if (cursor != null) {
            if(cursor.count == 0){
                return
            }
        }

        cursor!!.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
        val kode = cursor.getString(cursor.getColumnIndex(DBHelper.KODE_COL))
        val nama = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
        val kota = cursor.getString(cursor.getColumnIndex(DBHelper.KOTA_COL))
        val provinsi = cursor.getString(cursor.getColumnIndex(DBHelper.PROVINCE_COL))
        val alamat = cursor.getString(cursor.getColumnIndex(DBHelper.ALAMAT_COL))
        val latitude = cursor.getString(cursor.getColumnIndex(DBHelper.LATITUDE_COL))
        val longitude = cursor.getString(cursor.getColumnIndex(DBHelper.LONGITUDE_COL))
        val telp = cursor.getString(cursor.getColumnIndex(DBHelper.TELP_COL))
        val jenis_faskes = cursor.getString(cursor.getColumnIndex(DBHelper.TIPE_COl))
        val kelas_rs = cursor.getString(cursor.getColumnIndex(DBHelper.KELAS_COL))
        val status = cursor.getString(cursor.getColumnIndex(DBHelper.STATUS_COL))

        faskesArray.add(Faskes(id, kode, nama, kota, provinsi, alamat, latitude, longitude, telp, jenis_faskes, kelas_rs, status))
        // moving our cursor to next
        // position and appending values
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
            val kode = cursor.getString(cursor.getColumnIndex(DBHelper.KODE_COL))
            val nama = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
            val kota = cursor.getString(cursor.getColumnIndex(DBHelper.KOTA_COL))
            val provinsi = cursor.getString(cursor.getColumnIndex(DBHelper.PROVINCE_COL))
            val alamat = cursor.getString(cursor.getColumnIndex(DBHelper.ALAMAT_COL))
            val latitude = cursor.getString(cursor.getColumnIndex(DBHelper.LATITUDE_COL))
            val longitude = cursor.getString(cursor.getColumnIndex(DBHelper.LONGITUDE_COL))
            val telp = cursor.getString(cursor.getColumnIndex(DBHelper.TELP_COL))
            val jenis_faskes = cursor.getString(cursor.getColumnIndex(DBHelper.TIPE_COl))
            val kelas_rs = cursor.getString(cursor.getColumnIndex(DBHelper.KELAS_COL))
            val status = cursor.getString(cursor.getColumnIndex(DBHelper.STATUS_COL))

            faskesArray.add(Faskes(id, kode, nama, kota, provinsi, alamat, latitude, longitude, telp, jenis_faskes, kelas_rs, status))
        }

        // at last we close our cursor
        cursor.close()

        adapter?.updateFaskes(faskesArray)
    }
}