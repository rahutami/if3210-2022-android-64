package com.example.if3210_64.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.if3210_64.*
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment(), NewsItemFrClicked {

    private lateinit var mAdapter: NewsRecyclerAdapter

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
        MySingleton.getInstance(requireActivity()).addToRequestQueue(jsonObjectRequest)
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

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        fetchData()
        mAdapter = NewsRecyclerAdapter(this)
        recyclerView.adapter = mAdapter
    }

    override fun onItemClicked(item: News) {

        // Use bundle to exchange data between two fragments
        val input = item.url
        val bundle = Bundle()
        bundle.putString("url",input)

        // Change the fragment
        val fragment = NewsArticleFragment()
        fragment.arguments = bundle
        val fr = requireActivity().supportFragmentManager.beginTransaction()
        fr.replace(R.id.fragment_container,fragment)
        fr.addToBackStack(null)
        fr.commit()
    }
}