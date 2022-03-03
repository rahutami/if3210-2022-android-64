package com.example.if3210_64.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.if3210_64.DBHelper
import com.example.if3210_64.Faskes
import com.example.if3210_64.R

class DetailFaskesFragment : Fragment() {
    private lateinit var faskes: Faskes
    private lateinit var db: DBHelper
    private var bookmarked = false

    @SuppressLint("Range")
    fun onClickBookmark(view: View) {
        if (bookmarked) {
            db.deleteBookmark(faskes)
            Toast.makeText(activity, faskes.nama + " telah dihapus bookmark", Toast.LENGTH_LONG).show()
            val bookmarkBtn = view.findViewById<Button>(R.id.bookmark_btn)
            bookmarkBtn.text = "+ Bookmark"
            bookmarkBtn.setBackgroundResource(R.color.bookmark)
            return
        } else {
            db.addBokmark(faskes)
            Toast.makeText(activity, faskes.nama + " telah ditambahkan ke bookmark", Toast.LENGTH_LONG)
                .show()
            val bookmarkBtn = view.findViewById<Button>(R.id.bookmark_btn)
            bookmarkBtn.text = "- Hapus Bookmark"
            bookmarkBtn.setBackgroundResource(R.color.unbookmark)
        }
        bookmarked = !bookmarked
    }

    fun onClickMap(view: View) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_detail_faskes, container, false)

        view.findViewById<Button>(R.id.bookmark_btn)
            .setOnClickListener {
                onClickBookmark(view)
            }

        view.findViewById<Button>(R.id.map_button)
            .setOnClickListener {
                onClickMap(view)
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onCreate(savedInstanceState)

        val bundle: Bundle? = this.arguments
        val faskesDetail: Faskes = bundle!!.getSerializable("faskes") as Faskes
        faskes = faskesDetail

        db = DBHelper(requireActivity(), null)
        bookmarked = db.checkDuplicate(faskes)
        println(bookmarked)

        val faskesName = view.findViewById<TextView>(R.id.faskes_name)
        val faskesPhone = view.findViewById<TextView>(R.id.faskes_phone)
        val faskesCode = view.findViewById<TextView>(R.id.faskes_code)
        val faskesAddress = view.findViewById<TextView>(R.id.faskes_address)
        val faskesType = view.findViewById<TextView>(R.id.faskes_type)
        val faskesStatus = view.findViewById<TextView>(R.id.status_faskes)
        val logoStatus = view.findViewById<ImageView>(R.id.status_logo)
        val bookmarkBtn = view.findViewById<Button>(R.id.bookmark_btn)

        if (bookmarked) {
            bookmarkBtn.text = "- Hapus Bookmark"
            bookmarkBtn.setBackgroundResource(R.color.unbookmark)
        } else {
            bookmarkBtn.text = "+ Bookmark"
            bookmarkBtn.setBackgroundResource(R.color.bookmark)
        }

        faskesName.text = faskesDetail.nama
        faskesPhone.text = faskesDetail.telp
        faskesCode.text = faskesDetail.kode
        faskesAddress.text = faskesDetail.alamat
        if (faskesDetail.jenis_faskes == "") {
            faskesType.text = "Undefined"
        } else {
            faskesType.text = faskesDetail.jenis_faskes
        }
        faskesStatus.text = faskesDetail.status

        if (faskesDetail.status.equals("Siap Vaksinasi", true)) {
            logoStatus.setImageResource(R.drawable.green_check)
        } else {
            logoStatus.setImageResource(R.drawable.red_cross)
        }

        if (faskesDetail.jenis_faskes.equals("rumah sakit", true)) {
            faskesType.setBackgroundResource(R.color.rsColor)
        } else if (faskesDetail.jenis_faskes.equals("puskesmas", true)) {
            faskesType.setBackgroundResource(R.color.puskesmasColor)
        } else if (faskesDetail.jenis_faskes.equals("kkp", true)) {
            faskesType.setBackgroundResource(R.color.kkpColor)
        } else if (faskesDetail.jenis_faskes.equals("klinik", true)) {
            faskesType.setBackgroundResource(R.color.klinikColor)
        } else {
            faskesType.setBackgroundResource(R.color.otherColor)
        }
    }
}