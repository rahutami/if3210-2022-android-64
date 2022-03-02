package com.example.if3210_64.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.if3210_64.MySingleton
import com.example.if3210_64.News
import com.example.if3210_64.R
import com.example.if3210_64.RecyclerAdapter

class NewsFragment : Fragment() {

    private lateinit var mAdapter: RecyclerAdapter

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
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }
}