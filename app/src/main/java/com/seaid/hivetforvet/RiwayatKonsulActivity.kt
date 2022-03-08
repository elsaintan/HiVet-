package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
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
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("konsultasi")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if (error != null){
                        Log.e("Error: ", error.message.toString())
                        return
                    }

                    var listkonsul = ArrayList<konsultasi>()
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            val data: konsultasi = dc.document.toObject(konsultasi::class.java)
                            if (data.status!!.equals("4") || data.status.equals("5") || data.status.equals("6")) {
                                konsultasiList.add(dc.document.toObject(konsultasi::class.java))
                            }
                        }
                    }
                    rbinding.recyclerView.adapter = kBerjalanAdapter
                    kBerjalanAdapter.notifyDataSetChanged()
                }
            })
    }
}