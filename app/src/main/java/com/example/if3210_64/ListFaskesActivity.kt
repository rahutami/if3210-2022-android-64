package com.example.if3210_64

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class ListFaskesActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: FaskesRecyclerAdapter? = null
    private var province = ""
    private var kabupaten = ""
    var provinces = ArrayList<String>()
    var kabupatens = ArrayList<String>()
    var faskesArray = ArrayList<Faskes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_faskes)
        layoutManager = LinearLayoutManager(this)

//        sending data to province fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val provinceFragment = ProvinceFragment()

        val bundle = Bundle()
        fetchProvince()
        bundle.putStringArrayList("provinceList", provinces)
        provinceFragment.arguments  = bundle
        fragmentTransaction.replace(R.id.province_fragment, provinceFragment).commit()

//        recycler view stuff
        val faskesRecyclerView = findViewById<RecyclerView>(R.id.faskesRecyclerView)
        faskesRecyclerView.layoutManager = layoutManager
        adapter = FaskesRecyclerAdapter()
        adapter!!.setOnItemClickListener(object: FaskesRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@ListFaskesActivity, DetailFaskesActivity::class.java)
                intent.putExtra("faskes", faskesArray[position])
                startActivity(intent)
            }

        })
        faskesRecyclerView.adapter = adapter
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
    fun changeProvince(view: View){
        province = findViewById<AutoCompleteTextView>(R.id.autoCompleteProvince)?.text.toString()

        fetchKabupaten()
    }
    fun changeKabupaten(view: View){
        kabupaten = findViewById<AutoCompleteTextView>(R.id.autoCompleteKabupaten)?.text.toString()
        fetchFaskes()
    }
    fun fetchFaskes(){
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
                    faskesArray.add(faskes)
                }
                adapter?.updateFaskes(faskesArray)
            },
            {
                println("error")

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
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
                val fragmentManager: FragmentManager = supportFragmentManager
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
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
                val fragmentManager: FragmentManager = supportFragmentManager
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
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}