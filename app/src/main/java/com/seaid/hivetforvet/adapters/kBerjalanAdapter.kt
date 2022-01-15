package com.seaid.hivetforvet.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seaid.hivetforvet.R
import com.seaid.hivetforvet.models.kBerjalan

class kBerjalanAdapter (private val kBerjalanList : ArrayList<kBerjalan>) : RecyclerView.Adapter<kBerjalanAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): kBerjalanAdapter.MyViewHolder {
        TODO("Not yet implemented")
    }


    override fun onBindViewHolder(holder: kBerjalanAdapter.MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    override fun getItemCount(): Int {
        return kBerjalanList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val profile : ImageView = itemView.findViewById(R.id.photouser)
        val name : TextView = itemView.findViewById(R.id.nameTV)
        val jenisHewan : TextView = itemView.findViewById(R.id.jenisTV)
        val namaHewan : TextView = itemView.findViewById(R.id.namahewanTV)
        val chat : Button = itemView.findViewById(R.id.chatbt)


        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }
}