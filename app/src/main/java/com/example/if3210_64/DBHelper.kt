package com.example.if3210_64

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                KODE_COL + " TEXT," +
                TIPE_COl + " TEXT," +
                ALAMAT_COL + " TEXT," +
                TELP_COL + " TEXT," +
                STATUS_COL + " TEXT," +
                KOTA_COL + " TEXT," +
                PROVINCE_COL + " TEXT," +
                LATITUDE_COL + " TEXT," +
                LONGITUDE_COL + " TEXT," +
                KELAS_COL + " TEXT" +
                ")")

        println(query)
        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addBokmark(faskes: Faskes) {

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(ID_COL, faskes.id)
        values.put(NAME_COl, faskes.nama)
        values.put(KODE_COL, faskes.kode)
        values.put(TIPE_COl, faskes.jenis_faskes)
        values.put(ALAMAT_COL, faskes.alamat)
        values.put(TELP_COL, faskes.telp)
        values.put(STATUS_COL, faskes.status)
        values.put(KOTA_COL, faskes.kota)
        values.put(PROVINCE_COL, faskes.provinsi)
        values.put(LATITUDE_COL, faskes.latitude)
        values.put(LONGITUDE_COL, faskes.longitude)
        values.put(KELAS_COL, faskes.kelas_rs)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getBookmark(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    fun deleteBookmark(faskes: Faskes): Cursor? {
        val db = this.readableDatabase
        val query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL + " = " + faskes.id
        val result = db.rawQuery(query, null)
        println(query)
        println(result.count)
        return result
    }

    fun checkDuplicate(faskes: Faskes): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery(
            "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COL + " = " + faskes.id,
            null
        )

        return result.count == 1
    }

    companion object {
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "PERLU_DILINDUNGI"
        private val DATABASE_VERSION = 2
        val TABLE_NAME = "bookmark_faskes"
        val ID_COL = "id"
        val NAME_COl = "nama"
        val KODE_COL = "kode"
        val TIPE_COl = "jenis_faskes"
        val ALAMAT_COL = "alamat"
        val TELP_COL = "telp"
        val STATUS_COL = "status"
        val KOTA_COL = "kota"
        val PROVINCE_COL = "provinsi"
        val LATITUDE_COL = "latitude"
        val LONGITUDE_COL = "longitude"
        val KELAS_COL = "kelas_rs"
    }
}