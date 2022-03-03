package com.example.if3210_64

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity

class FaskesRecyclerAdapter: RecyclerView.Adapter<FaskesRecyclerAdapter.ViewHolder>() {

    private var items: ArrayList<Faskes> = arrayListOf()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaskesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.faskes_card,parent,false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: FaskesRecyclerAdapter.ViewHolder, position: Int) {
        val currItem = items[position]
        holder.namaView.text = currItem.nama
        if(currItem.jenis_faskes.equals("")){
            holder.jenis_faskesView.text = "Undefined"
        } else {
            holder.jenis_faskesView.text = currItem.jenis_faskes
        }
        holder.alamatView.text = currItem.alamat
        holder.kodeView.text = currItem.kode
        holder.telpView.text = currItem.telp

        holder.kode = currItem.kode
        holder.nama = currItem.nama
        holder.kota = currItem.kota
        holder.provinsi = currItem.provinsi
        holder.alamat = currItem.alamat
        holder.latitude = currItem.latitude
        holder.longitude = currItem.longitude
        holder.telp = currItem.telp
        holder.jenis_faskes = currItem.jenis_faskes
        holder.kelas_rs = currItem.kelas_rs
        holder.status = currItem.status

        if(currItem.jenis_faskes.equals("rumah sakit", true)){
            holder.jenis_faskesView.setBackgroundResource(R.color.rsColor)
        } else if(currItem.jenis_faskes.equals("puskesmas", true)){
            holder.jenis_faskesView.setBackgroundResource(R.color.puskesmasColor)
        } else if(currItem.jenis_faskes.equals("kkp", true)){
            holder.jenis_faskesView.setBackgroundResource(R.color.kkpColor)
        } else if(currItem.jenis_faskes.equals("klinik", true)){
            holder.jenis_faskesView.setBackgroundResource(R.color.klinikColor)
        } else {
            holder.jenis_faskesView.setBackgroundResource(R.color.otherColor)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateFaskes(updatedFaskes: ArrayList<Faskes>) {
        items.clear()
        items.addAll(updatedFaskes)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        var kode: String = ""
        var nama: String = ""
        var kota: String = ""
        var provinsi: String = ""
        var alamat: String = ""
        var latitude: String = ""
        var longitude: String = ""
        var telp: String = ""
        var jenis_faskes: String = ""
        var kelas_rs: String = ""
        var status: String = ""
        var namaView: TextView
        var jenis_faskesView: TextView
        var alamatView: TextView
        var kodeView: TextView
        var telpView: TextView

        init {
            namaView = itemView.findViewById(R.id.faskes_name)
            jenis_faskesView = itemView.findViewById(R.id.faskes_type)
            alamatView = itemView.findViewById(R.id.faskes_address)
            kodeView = itemView.findViewById(R.id.faskes_code)
            telpView = itemView.findViewById(R.id.faskes_phone)

            itemView.setOnClickListener{
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

}