package com.seaid.hivetforvet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.ChatActivity
import com.seaid.hivetforvet.R
import com.seaid.hivetforvet.models.User
import com.seaid.hivetforvet.models.konsultasi
import com.seaid.hivetforvet.models.peliharaan

class kBerjalanAdapter (private val konsultasiList : ArrayList<konsultasi>) : RecyclerView.Adapter<kBerjalanAdapter.MyViewHolder>(){

    private lateinit var mDbRef: FirebaseFirestore

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.konsultasi_layout, parent, false)
       return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val konsultasi : konsultasi = konsultasiList[position]
        mDbRef = FirebaseFirestore.getInstance()
        val data1 = mDbRef.collection("users").document(konsultasi.id_user.toString())
        data1.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user != null){
                holder.nameUser.text = user.name
                if (user.photoProfile != null){
                    holder.profileUser.setImageResource(R.drawable.profile)
                }else{
                    Glide.with(holder.itemView.context).load(user.photoProfile).into(holder.profileUser)
                }
            }
        }

        val data2 = mDbRef.collection("peliharaan").document(konsultasi.id_pet.toString())
        data2.get().addOnSuccessListener {
            val pet = it.toObject(peliharaan::class.java)
            if (pet != null){
                holder.jenisHewan.text = pet.jenis
                holder.namaHewan.text = pet.nama
            }
        }
        when(konsultasi.status){
            "1" -> holder.button.text = "Terima"
            "2" -> holder.button.text = "Menunggu Pembayaran"
            "3" -> holder.button.text = "Chat"
            "4" -> holder.button.text = "Selesai"
            "5" -> holder.button.text = "Ditolak"
            "6" -> holder.button.text = "Kedaluarsa"
        }

        holder.button.setOnClickListener {
            if (holder.button.text == "Chat"){
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("Uid", konsultasi.id_user)
                //intent.putExtra("id", )
                intent.putExtra("id", konsultasi.id)
                intent.putExtra("idpet", konsultasi.id_pet)
                intent.putExtra("tanggal", konsultasi.tanggal)
                intent.putExtra("idtransaction", konsultasi.id_transaction)
                intent.putExtra("harga", konsultasi.harga.toString())
                holder.itemView.context.startActivity(intent)
            }else if (holder.button.text == "Terima"){
                changeStatus(konsultasi)
            }

        }

    }

    private fun changeStatus(konsultasi: konsultasi) {
        val konsul : konsultasi = konsultasi(konsultasi.id, konsultasi.id_drh, konsultasi.id_user, konsultasi.id_pet, konsultasi.tanggal, "2", konsultasi.id_transaction, konsultasi.harga)
        mDbRef.collection("konsultasi").document(konsultasi.id.toString()).set(konsul)
        
    }




    override fun getItemCount(): Int {
        return konsultasiList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val profileUser : ImageView = itemView.findViewById(R.id.photodrh)
        val nameUser : TextView = itemView.findViewById(R.id.namedrhTV)
        val jenisHewan : TextView = itemView.findViewById(R.id.workexpTV)
        val namaHewan : TextView = itemView.findViewById(R.id.priceTV)
        val button : Button = itemView.findViewById(R.id.konsulbt)


        override fun onClick(v: View?) {

        }

    }
}