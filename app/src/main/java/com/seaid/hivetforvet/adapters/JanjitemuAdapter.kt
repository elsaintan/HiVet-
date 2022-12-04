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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        val ref = FirebaseDatabase.getInstance().getReference("users").child(data.user_id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)
                    if (user != null){
                        holder.nama.text = user.name
                        if (user.photoProfile != null){
                            holder.foto.setImageResource(R.drawable.icon_user_profile)
                        }else{
                            Glide.with(holder.itemView.context).load(user.photoProfile).into(holder.foto)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })

        val peliharaan = FirebaseDatabase.getInstance().getReference("peliharaan").child(data.pet_id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pet: peliharaan? = snapshot.getValue(peliharaan::class.java)
                    if (pet != null){
                        holder.namah.text = pet.jenis
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })

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