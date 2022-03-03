package com.example.if3210_64.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.if3210_64.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ListFaskesFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: FaskesRecyclerAdapter? = null
    private var province = ""
    private var kabupaten = ""
    var provinces = ArrayList<String>()
    var kabupatens = ArrayList<String>()
    var faskesArray = ArrayList<Faskes>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location->
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                    }

                }
            return
        }

    }

    fun fetchFaskes(){
        val distances = arrayListOf<Double>(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        val url = "https://perludilindungi.herokuapp.com/api/get-faskes-vaksinasi?province="+province+"&city="+kabupaten
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                println("good")
                val faskesJsonArray = it.getJSONArray("data")
                val provinces = ArrayList<String>()
                for (i in 0 until faskesJsonArray.length()) {
                    val faskesJsonObject = faskesJsonArray.getJSONObject(i)
                    val faskes = Faskes(
                        faskesJsonObject.getInt("id"),
                        faskesJsonObject.getString("kode"),
                        faskesJsonObject.getString("nama"),
                        faskesJsonObject.getString("kota"),
                        faskesJsonObject.getString("provinsi"),
                        faskesJsonObject.getString("alamat"),
                        faskesJsonObject.getString("latitude"),
                        faskesJsonObject.getString("longitude"),
                        faskesJsonObject.getString("telp"),
                        faskesJsonObject.getString("jenis_faskes"),
                        faskesJsonObject.getString("kelas_rs"),
                        faskesJsonObject.getString("status")
                    )
                    val curLat = faskesJsonObject.getString("latitude").toDouble()
                    val curLong = faskesJsonObject.getString("longitude").toDouble()
                    val curDist = distance(latitude, longitude, curLat, curLong)

                    for (j in 0 until distances.size){
                        if(curDist < distances[j]){
                            distances.add(j, curDist)
                            distances.removeLast()
                            if(faskesArray.size <= j){
                                faskesArray.add(faskes)
                            } else {
                                faskesArray.add(j, faskes)
                            }

                            if(faskesArray.size > 5){
                                faskesArray.removeLast()
                            }
                            break
                        }
                    }
                }
                adapter?.updateFaskes(faskesArray)
            },
            {
                println("error")

            }
        )
        MySingleton.getInstance(requireActivity()).addToRequestQueue(jsonObjectRequest)
    }

    fun fetchKabupaten(){
        kabupatens.clear()

        val url = "https://perludilindungi.herokuapp.com/api/get-city?start_id="+province
        println(url)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                println("good")
                val kabupatenJsonArray = it.getJSONArray("results")
                for (i in 0 until kabupatenJsonArray.length()) {
                    val provinceJsonObject = kabupatenJsonArray.getJSONObject(i)
                    kabupatens.add(provinceJsonObject.getString("key"))
                }

//          sending data to province fragment
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                val kFragment = kabupatenFragment()

                val bundle = Bundle()
                bundle.putStringArrayList("kabupatenList", kabupatens)
                kFragment.arguments  = bundle
                fragmentTransaction.replace(R.id.kabupaten_fragment, kFragment).commit()
            },
            {
                println("error")

            }
        )
        MySingleton.getInstance(requireActivity()).addToRequestQueue(jsonObjectRequest)
    }
    fun fetchProvince(){
        val url = "https://perludilindungi.herokuapp.com/api/get-province"
        println(url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                println("good")
                val provinceJsonArray = it.getJSONArray("results")
                for (i in 0 until provinceJsonArray.length()) {
                    val provinceJsonObject = provinceJsonArray.getJSONObject(i)
                    provinces.add(provinceJsonObject.getString("key"))
                }

                //        sending data to province fragment
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                val provinceFragment = ProvinceFragment()

                val bundle = Bundle()
                bundle.putStringArrayList("provinceList", provinces)
                provinceFragment.arguments  = bundle
                fragmentTransaction.replace(R.id.province_fragment, provinceFragment).commit()
            },
            {
                println("error")

            }
        )
        MySingleton.getInstance(requireActivity()).addToRequestQueue(jsonObjectRequest)
    }

    fun changeProvince(view: View){
        province = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteProvince)?.text.toString()
        fetchKabupaten()
    }

    fun changeKabupaten(view: View){
        kabupaten = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteKabupaten)?.text.toString()
        fetchFaskes()
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_faskes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(activity)

        //        sending data to province fragment
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val provinceFragment = ProvinceFragment()

        val bundle = Bundle()
        fetchProvince()
        bundle.putStringArrayList("provinceList", provinces)
        provinceFragment.arguments  = bundle
        fragmentTransaction.replace(R.id.province_fragment, provinceFragment).commit()

        //        recycler view stuff
        val faskesRecyclerView = view.findViewById<RecyclerView>(R.id.faskesRecyclerView)
        faskesRecyclerView.layoutManager = layoutManager
        adapter = FaskesRecyclerAdapter()
        adapter!!.setOnItemClickListener(object: FaskesRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                // Use Bundle to exchange data between two fragments
                val input = faskesArray[position]
                val bundle = Bundle()
                bundle.putParcelableArrayList("input",faskesArray[position] as ArrayList<out Parcelable>)

                /*// Change the fragment
                val fragment = DetailFaskesFragment()
                fragment.arguments = bundle
                val fr = requireActivity().supportFragmentManager.beginTransaction()
                fr.replace(R.id.fragment_container,fragment)
                fr.addToBackStack(null)
                fr.commit()*/


                //val intent = Intent(@ListFaskesActivity, DetailFaskesActivity::class.java)
                //intent.putExtra("faskes", faskesArray[position])
                //startActivity(intent)
            }

        })
        faskesRecyclerView.adapter = adapter

        //        Location stuff
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }
}