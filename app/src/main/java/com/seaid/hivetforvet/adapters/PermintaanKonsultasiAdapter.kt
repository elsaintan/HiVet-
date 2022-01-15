package com.seaid.hivetforvet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seaid.hivetforvet.R
import com.seaid.hivetforvet.models.permintaanKonsultasi

class permintaanKonsultasiAdapter (private val permintaanList : ArrayList<permintaanKonsultasi>) : RecyclerView.Adapter<permintaanKonsultasiAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): permintaanKonsultasiAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.permintaan_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: permintaanKonsultasiAdapter.MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return permintaanList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val profile : ImageView = itemView.findViewById(R.id.photouser)
        val name : TextView = itemView.findViewById(R.id.nameTV)
        val jenisHewan : TextView = itemView.findViewById(R.id.jenisTV)
        val namaHewan : TextView = itemView.findViewById(R.id.namahewanTV)
        val konfirmB : Button = itemView.findViewById(R.id.terimabt)
        val tolakB : Button = itemView.findViewById(R.id.tolakbt)

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }
}