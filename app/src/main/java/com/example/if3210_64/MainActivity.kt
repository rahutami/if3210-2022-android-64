package com.example.if3210_64

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private lateinit var mAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        //adapter = RecyclerAdapter()
        mAdapter = RecyclerAdapter()
        //recyclerView.adapter = adapter
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://perludilindungi.herokuapp.com/api/get-news"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val enclosure = newsJsonObject.getJSONObject("enclosure")
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("guid"),
                        newsJsonObject.getString("pubDate"),
                        enclosure.getString("_url")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}