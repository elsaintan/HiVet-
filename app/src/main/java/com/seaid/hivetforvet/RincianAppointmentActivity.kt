package com.seaid.hivetforvet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val ref = FirebaseDatabase.getInstance().getReference("booking_appointments")
        ref.child(id.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val janjitemu : JanjiTemu? = snapshot.getValue(JanjiTemu::class.java)
                    rabinding.tanggalap.text = janjitemu!!.tanggal
                    rabinding.status.text = janjitemu.status
                    rabinding.waktuTV.text = janjitemu.waktu
                    showuserdata(janjitemu.user_id)
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })
        showdrhdata()
    }

    private fun showuserdata(userId: String?) {
        val data1 = FirebaseDatabase.getInstance().getReference("users")
        data1.child(userId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)
                    rabinding.nameTV.text = user!!.name
                    if (user.photoProfile != null){
                        rabinding.photodrh.setImageResource(R.drawable.icon_user_profile)
                    }else{
                        Glide.with(this@RincianAppointmentActivity).load(user.photoProfile).into(rabinding.photodrh)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }

            })
    }

    private fun showdrhdata(){
        val data1 = FirebaseDatabase.getInstance().getReference("drh")
            data1.child(mAuth.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val drh: Vet? = snapshot.getValue(Vet::class.java)
                        rabinding.tempatTV.text = drh!!.alamat
                        rabinding.tempatpraktik.text = drh.tempat
                        rabinding.tempatklinik.text = drh.alamat
                    }
                    override fun onCancelled(error: DatabaseError) {
                        throw error.toException()
                    }

                })
    }
}