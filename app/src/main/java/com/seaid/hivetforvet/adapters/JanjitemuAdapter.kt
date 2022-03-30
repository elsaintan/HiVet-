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
import com.seaid.hivetforvet.RincianAppointmentActivity
import com.seaid.hivetforvet.models.JanjiTemu
import com.seaid.hivetforvet.models.User
import com.seaid.hivetforvet.models.konsultasi
import com.seaid.hivetforvet.models.peliharaan
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class JanjitemuAdapter(private val janjitemuList: ArrayList<JanjiTemu>) : RecyclerView.Adapter<JanjitemuAdapter.MyViewHolder>(){

    private lateinit var mDbRef: FirebaseFirestore

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JanjitemuAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.janji_temu_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data : JanjiTemu = janjitemuList[position]

        holder.tanggal.text = data.tanggal

        val dateInString = data.tanggal

        if (isDateValid(dateInString.toString())){
            holder.button.text = "Selesai"
        }else{
            holder.button.text = "Rincian"
        }

        mDbRef = FirebaseFirestore.getInstance()
        val data1 = mDbRef.collection("users").document(data.user_id.toString())
        data1.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user != null){
                holder.nama.text = user.name
                if (user.photoProfile != null){
                    holder.foto.setImageResource(R.drawable.profile)
                }else{
                    Glide.with(holder.itemView.context).load(user.photoProfile).into(holder.foto)
                }
            }
        }

        val data2 = mDbRef.collection("peliharaan").document(data.pet_id.toString())
        data2.get().addOnSuccessListener {
            val pet = it.toObject(peliharaan::class.java)
            if (pet != null){
                holder.namah.text = pet.jenis
            }
        }

        holder.button.setOnClickListener {
            val intent = Intent(holder.itemView.context, RincianAppointmentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("id", data.kode_booking)
            holder.itemView.context.startActivity(intent)
        }

    }

    fun isDateValid(myDate: String) : Boolean {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd").parse(myDate)
            return !date.before(Date())
        } catch(ignored: java.text.ParseException) {
            return false
        }
    }

    override fun getItemCount(): Int {
        return janjitemuList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val nama : TextView = itemView.findViewById(R.id.nameTV)
        val namah : TextView = itemView.findViewById(R.id.jenisTV)
        val tanggal : TextView = itemView.findViewById(R.id.namahewanTV)
        val foto : ImageView = itemView.findViewById(R.id.photouser)
        val button : Button = itemView.findViewById(R.id.chatbt)
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }

}