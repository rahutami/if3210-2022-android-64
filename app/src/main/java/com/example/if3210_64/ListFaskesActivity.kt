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

//        sending data to province fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val kFragment = kabupatenFragment()

        val bundle = Bundle()
        fetchKabupaten()
        bundle.putStringArrayList("kabupatenList", kabupatens)
        kFragment.arguments  = bundle
        fragmentTransaction.replace(R.id.kabupaten_fragment, kFragment).commit()
    }
    fun changeKabupaten(view: View){
        kabupaten = findViewById<AutoCompleteTextView>(R.id.autoCompleteKabupaten)?.text.toString()
        fetchFaskes()
    }
    fun fetchFaskes(){
        faskesArray = arrayListOf<Faskes>(
            Faskes("9020107","KEL. MANGGARAI SELATAN","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","Jl. Doktor Saharjo RT.04/RW.07, Manggarai Selatan, Tebet, RT.4/RW.7, Manggarai Sel., Kec. Tebet, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12860, Indonesia","-6.2175146","106.8472515","(021) 8352992","PUSKESMAS","","Siap Vaksinasi"),
            Faskes("N0000624","SILOAM HOSPITALS TB SIMATUPANG","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","Jl. R.A.Kartini No.8, RT.10/RW.4, Cilandak Bar., Kec. Cilandak, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12430, Indonesia","-6.2921084","106.7843444","(021) 29531900","RUMAH SAKIT","","Siap Vaksinasi"),
            Faskes("9020903","KEL. CIKOKO","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","6, Jl. Tebet Barat IX No.64, RT.6/RW.4, Tebet Bar., Kec. Tebet, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12810, Indonesia","-6.2390342","106.8478032","(021) 8295976","PUSKESMAS","","Belum Siap Vaksinasi"),
            Faskes("0112R063","RSUD PESANGGRAHAN","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","5, Jl. Cenek I No.1, RT.5/RW.3, Pesanggrahan, Kec. Pesanggrahan, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12320, Indonesia","-6.2581378","106.756779","(021) 7356087","RUMAH SAKIT","C","Siap Vaksinasi"),
            Faskes("N0002066","RISTRA CLINIC","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","10, Jl. Radio Dalam Raya No.5, RT.10/RW.1, Gandaria Utara, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12140, Indonesia","-6.2503169","106.7918604","(021) 7226673","","","Siap Vaksinasi"),
            Faskes("0112R077","RS MAYAPADA JAKARTA SELATAN","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","No.Kav. 29, Jl. Lb. Bulus I, RT.6/RW.4, Lb. Bulus, Cilandak, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12440, Indonesia","-6.2981092","106.7857609","150770","RUMAH SAKIT","B","Siap Vaksinasi"),
            Faskes("9020705","KEL. CILANDAK BARAT","KOTA ADM. JAKARTA SELATAN","DKI JAKARTA","Jl. Komplek BNI 46 No.57, Cilandak Barat, Cilandak, RT.4/RW.5, Cilandak Barat, RT.4/RW.5, Cilandak Bar., Kec. Cilandak, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12430, Indonesia","-6.2868199","106.7932134","(021) 7694279","PUSKESMAS","","Siap Vaksinasi")
        )
        adapter?.updateFaskes(faskesArray)
//        val url = "https://perludilindungi.herokuapp.com/api/get-faskes-vaksinasi?province="+province+"&city="+kabupaten
//        println(url)
//        val faskesArray = ArrayList<Faskes>()
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET,
//            url,
//            null,
//            {
//                println("good")
//                val faskesJsonArray = it.getJSONArray("data")
//                val provinces = ArrayList<String>()
//                for (i in 0 until faskesJsonArray.length()) {
//                    val provinceJsonObject = faskesJsonArray.getJSONObject(i)
//                    provinces.add(provinceJsonObject.getString("key"))
//                    println(provinceJsonObject.getString("key"))
//                    val faskes = Faskes(
//                        provinceJsonObject.getString("kode"),
//                        provinceJsonObject.getString("nama"),
//                        provinceJsonObject.getString("kota"),
//                        provinceJsonObject.getString("provinsi"),
//                        provinceJsonObject.getString("alamat"),
//                        provinceJsonObject.getString("latitude"),
//                        provinceJsonObject.getString("longitude"),
//                        provinceJsonObject.getString("telp"),
//                        provinceJsonObject.getString("jenis_faskes"),
//                        provinceJsonObject.getString("kelas_rs"),
//                        provinceJsonObject.getString("status")
//                    )
//                    println(provinceJsonObject.getString("nama"))
//                    faskesArray.add(faskes)
//                }
//                adapter?.updateFaskes(faskesArray)
//            },
//            {
//                println("error")
//
//            }
//        )
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun fetchKabupaten(){
        kabupatens.clear()
        if(province == "Jakarta"){
            kabupatens.add("Jakarta Selatan")
            kabupatens.add("Jakarta Utara")
        } else {
            kabupatens.add("Sleman")
            kabupatens.add("Pacitan")
        }

//        val url = "https://perludilindungi.herokuapp.com/api/get-city?start_id="+province
//        println(url)
//        val faskesArray = ArrayList<Faskes>()
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET,
//            url,
//            null,
//            {
//                println("good")
//                val faskesJsonArray = it.getJSONArray("data")
//                val provinces = ArrayList<String>()
//                for (i in 0 until faskesJsonArray.length()) {
//                    val provinceJsonObject = faskesJsonArray.getJSONObject(i)
//                    provinces.add(provinceJsonObject.getString("key"))
//                    println(provinceJsonObject.getString("key"))
//                    val faskes = Faskes(
//                        provinceJsonObject.getString("kode"),
//                        provinceJsonObject.getString("nama"),
//                        provinceJsonObject.getString("kota"),
//                        provinceJsonObject.getString("provinsi"),
//                        provinceJsonObject.getString("alamat"),
//                        provinceJsonObject.getString("latitude"),
//                        provinceJsonObject.getString("longitude"),
//                        provinceJsonObject.getString("telp"),
//                        provinceJsonObject.getString("jenis_faskes"),
//                        provinceJsonObject.getString("kelas_rs"),
//                        provinceJsonObject.getString("status")
//                    )
//                    println(provinceJsonObject.getString("nama"))
//                    faskesArray.add(faskes)
//                }
//                adapter?.updateFaskes(faskesArray)
//            },
//            {
//                println("error")
//
//            }
//        )
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun fetchProvince(){
        provinces.add("Jakarta")
        provinces.add("Yogyakarta")
    }
}