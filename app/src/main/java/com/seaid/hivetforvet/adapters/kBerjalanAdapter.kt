package com.seaid.hivetforvet.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.ChatActivity
import com.seaid.hivetforvet.KonsultasiActivity
import com.seaid.hivetforvet.R
import com.seaid.hivetforvet.models.*
import com.seaid.hivetforvet.viewmodel.KonsultasiViewModel
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class kBerjalanAdapter (private val konsultasiList : ArrayList<konsultasi>) : RecyclerView.Adapter<kBerjalanAdapter.MyViewHolder>() {

    private lateinit var mDbRef: FirebaseFirestore
    private lateinit var viewModel: KonsultasiViewModel
    var reference: DatabaseReference? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.konsultasi_layout, parent, false)
       return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val konsultasi : konsultasi = konsultasiList[position]

        val dateInString = konsultasi.tanggal
        if (isDateValid(dateInString.toString())){
            changeStatus(konsultasi.id.toString(), holder, "6")
        }

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(konsultasi.id_user.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user : User? = snapshot.getValue(User::class.java)
                    holder.nameUser.text = user!!.name
                    if (user.photoProfile.equals(null) || user.photoProfile.equals("")){
                        holder.profileUser.setImageResource(R.drawable.profile3)
                    }else{
                        Glide.with(holder.itemView.context).load(user.photoProfile).into(holder.profileUser)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })

        val data2 = FirebaseDatabase.getInstance().getReference("peliharaan")
        data2.child(konsultasi.id_pet.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pet: peliharaan? = snapshot.getValue(peliharaan::class.java)
                    if (pet != null) {
                        holder.jenisHewan.text = pet.jenis
                        holder.namaHewan.text = pet.nama
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })

        when(konsultasi.status){
            "1" -> holder.button.text = "Terima"
            "2" -> holder.button.text = "Menunggu Pembayaran"
            "3" -> holder.button.text = "Chat"
            "4" -> holder.button.text = "Selesai"
            "5" -> holder.button.text = "Ditolak"
            "6" -> holder.button.text = "Kadaluwarsa"
        }

        if (konsultasi.status == "1"){
            holder.tolak.visibility = View.VISIBLE
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
                changeStatus(konsultasi.id.toString(), holder, "2")
            }else if(holder.button.text == "Selesai"){
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("Uid", konsultasi.id_user)
                intent.putExtra("type", "0")
                intent.putExtra("id", konsultasi.id)
                intent.putExtra("idpet", konsultasi.id_pet)
                intent.putExtra("tanggal", konsultasi.tanggal)
                holder.itemView.context.startActivity(intent)
            }

        }

        holder.tolak.setOnClickListener {
            changeStatus(konsultasi.id.toString(), holder, "5")
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

    private fun changeStatus(id: String, holder: MyViewHolder, status: String) {

        reference = FirebaseDatabase.getInstance().getReference("konsultasi")
        reference!!.child(id).child("status").setValue(status)
            .addOnSuccessListener {
                holder.button.text = "Menunggu Pembayaran"
            }
            .addOnFailureListener {
                throw it
            }

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
        val tolak : Button = itemView.findViewById(R.id.tolakbt)


        override fun onClick(v: View?) {

        }

    }

    /**
     * Returns the Lifecycle of the provider.
     *
     * @return The lifecycle of the provider.
     */

}