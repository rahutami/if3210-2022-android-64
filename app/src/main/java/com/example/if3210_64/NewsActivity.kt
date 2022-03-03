package com.example.if3210_64

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.news_activity.*

class NewsActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = RecyclerAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://perludilindungi.herokuapp.com/api/get-news"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
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
            {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val intent = Intent(this,WebView::class.java)
        intent.putExtra("url",item.url)
        startActivity(intent)
    }
}