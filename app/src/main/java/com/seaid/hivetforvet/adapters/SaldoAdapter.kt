package com.seaid.hivetforvet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seaid.hivetforvet.R
import com.seaid.hivetforvet.models.Saldo

class SaldoAdapter(private val penarikanList: ArrayList<Saldo>) : RecyclerView.Adapter<SaldoAdapter.ThisViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ThisViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.riwayat_saldo_layout, parent, false)
        return ThisViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ThisViewHolder, position: Int) {
        val penarikan : Saldo = penarikanList[position]

        holder.saldo.text = penarikan.jumlah
        holder.tanggal.text = penarikan.tanggal
        holder.status.text = "Status: " +penarikan.status
        holder.jenis.text = penarikan.jenis
    }

    override fun getItemCount(): Int {
        return penarikanList.size
    }

    class ThisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val saldo = itemView.findViewById<TextView>(R.id.jumlahTV)
        val tanggal = itemView.findViewById<TextView>(R.id.tanggalTV)
        val status = itemView.findViewById<TextView>(R.id.statusTV)
        val jenis = itemView.findViewById<TextView>(R.id.jenis)

        override fun onClick(v: View?) {

        }

    }

}