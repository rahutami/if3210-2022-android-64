package com.example.if3210_64

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsRecyclerAdapter(private var listener: NewsItemFrClicked): RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    private var items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        val viewHolder = ViewHolder(v)
        v.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsRecyclerAdapter.ViewHolder, position: Int) {
        val currItem = items[position]
        holder.itemTitle.text = currItem.title
        holder.itemDetail.text = currItem.date
        Glide.with(holder.itemView.context).load(currItem.imageUrl).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

}

interface NewsItemFrClicked {
    fun onItemClicked(item: News)
}