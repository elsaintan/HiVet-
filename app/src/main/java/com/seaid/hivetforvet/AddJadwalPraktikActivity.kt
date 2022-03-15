package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.JanjitemuAdapter
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityAddJadwalPraktikBinding
import com.seaid.hivetforvet.models.JanjiTemu
import com.seaid.hivetforvet.models.Vet
import com.seaid.hivetforvet.models.konsultasi

class AddJadwalPraktikActivity : AppCompatActivity() {

    private lateinit var abinding : ActivityAddJadwalPraktikBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var janjiTemuList: ArrayList<JanjiTemu>
    private lateinit var adapter: JanjitemuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abinding = ActivityAddJadwalPraktikBinding.inflate(layoutInflater)
        val view = abinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        abinding.recyclerView.layoutManager = LinearLayoutManager(this)
        abinding.recyclerView.setHasFixedSize(true)
        janjiTemuList = arrayListOf()
        adapter = JanjitemuAdapter(janjiTemuList)
        EventChangeListener()

        abinding.add.setOnClickListener {
            saveData()
        }

        showData()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("booking_appointments")
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(JanjiTemu::class.java)
                val items = data.size
                if (items > 0){
                    for (item in data){
                        if(item.drh_id == mAuth.uid){
                            janjiTemuList.add(item)
                        }
                    }
                    abinding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun showData() {
        val uidRef  = db.collection("drh").document(mAuth.currentUser!!.uid)

        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(Vet::class.java)
                abinding.usernametv.text = user!!.Name
                abinding.tempatpraktikTV.text = user!!.tempat
                //abinding.jampraktikTV.text = user!!.
                //Toast.makeText(this, "{$user.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "get failed with "+exception, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
       db.collection("drh").document(mAuth.currentUser!!.uid)
        .update("status", abinding.jampraktikTV.text)
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                throw it
            }
    }
}