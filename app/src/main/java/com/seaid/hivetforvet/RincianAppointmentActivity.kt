package com.seaid.hivetforvet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.databinding.ActivityRincianAppointmentBinding
import com.seaid.hivetforvet.models.*

class RincianAppointmentActivity : AppCompatActivity() {

    private lateinit var rabinding : ActivityRincianAppointmentBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rabinding = ActivityRincianAppointmentBinding.inflate(layoutInflater)
        setContentView(rabinding.root)

        mAuth = FirebaseAuth.getInstance()

        val id = intent.getStringExtra("id")

        showData(id)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, AddJadwalPraktikActivity::class.java))
        finish()
    }

    private fun showData(id: String?) {
        db = FirebaseFirestore.getInstance()
        db.collection("booking_appointments").document(id.toString())
            .get()
            .addOnSuccessListener {
                if(it != null){
                    val janjitemu = it.toObject(JanjiTemu::class.java)
                    rabinding.tanggalap.text = janjitemu!!.tanggal
                    rabinding.status.text = janjitemu!!.status
                    rabinding.waktuTV.text = janjitemu!!.waktu
                    showuserdata(janjitemu.user_id)
                    //showpetdata(janjitemu.pet_id)
                }
            }
        showdrhdata()
    }

    /**private fun showpetdata(petId: String?) {
        val data2 = db.collection("peliharaan").document(petId.toString())
        data2.get().addOnSuccessListener {
            val pet = it.toObject(peliharaan::class.java)
            if (pet != null){

                holder.jenisHewan.text = pet.jenis
                holder.namaHewan.text = pet.nama
            }
        }
    **/

    private fun showuserdata(userId: String?) {
        val data1 = db.collection("users").document(userId.toString())
        data1.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user != null){
                rabinding.nameTV.text = user.name
                if (user.photoProfile != null){
                    rabinding.photodrh.setImageResource(R.drawable.profile)
                }else{
                    Glide.with(this).load(user.photoProfile).into(rabinding.photodrh)
                }
            }
        }
    }

    private fun showdrhdata(){
        val data1 = db.collection("drh").document(mAuth.currentUser!!.uid)
        data1.get().addOnSuccessListener {
            val drh = it.toObject(Vet::class.java)
            if (drh != null){
                rabinding.tempatTV.text = drh.alamat
                rabinding.tempatpraktik.text = drh.tempat
                rabinding.tempatklinik.text = drh.alamat
            }
        }
    }
}