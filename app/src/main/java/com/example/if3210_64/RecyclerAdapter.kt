package com.example.if3210_64

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var items: ArrayList<News> = ArrayList()

    private var title = arrayOf("Grader Build", "Chapter Two", "Chapter Three", "Chapter Four", "Chapter Five", "Chapter Six", "Chapter Seven", "Chapter Eight")
    private var details = arrayOf("Item one details", "Item two details", "Item three details", "Item four details","Item five details",
        "Item six details", "Item seven details", "Item eight details", "Item nine details", "Item ten details")
    private var images = intArrayOf(R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp, R.drawable.ic_android_black_24dp)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        // TO DO: ATUR PENEMPATAN variabel items
        holder.itemTitle.text = title[position]
        //holder.itemDetail.text = details[position]
        holder.itemDetail.text = items.toString()
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return title.size
    }

    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }
    }

}