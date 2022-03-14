package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityRiwayatKonsulBinding
import com.seaid.hivetforvet.models.konsultasi

class RiwayatKonsulActivity : AppCompatActivity() {

    lateinit var rbinding: ActivityRiwayatKonsulBinding
    private lateinit var konsultasiList: ArrayList<konsultasi>
    private lateinit var kBerjalanAdapter: kBerjalanAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rbinding = ActivityRiwayatKonsulBinding.inflate(layoutInflater)
        setContentView(rbinding.root)

        rbinding.recyclerView.layoutManager = LinearLayoutManager(this)
        rbinding.recyclerView.setHasFixedSize(true)



        konsultasiList = arrayListOf()
        kBerjalanAdapter = kBerjalanAdapter(konsultasiList)


        //kBerjalanAdapter = kBerjalanAdapter(this)
        EventChangeListener()


    }

    private fun EventChangeListener() {
        val reference = FirebaseDatabase.getInstance().getReference("konsultasi")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                konsultasiList.clear()
                for (snapshot in snapshot.children) {
                    val data: konsultasi? = snapshot.getValue(konsultasi::class.java)
                    if (data?.status!!.equals("4") || data.status.equals("5") || data.status.equals("6")) {
                        konsultasiList.add(data)
                        //Toast.makeText(this@KonsultasiActivity, "ADA DATANYA", Toast.LENGTH_SHORT).show()
                    }
                    rbinding.recyclerView.adapter = kBerjalanAdapter
                }
                kBerjalanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException();
            }
        })

    }
}