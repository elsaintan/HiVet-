package com.seaid.hivetforvet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityKonsultasiBinding
import com.seaid.hivetforvet.models.konsultasi

class KonsultasiActivity : AppCompatActivity() {

    lateinit var kBinding: ActivityKonsultasiBinding
    private lateinit var konsultasiList: ArrayList<konsultasi>
    private lateinit var kBerjalanAdapter: kBerjalanAdapter
    private lateinit var db : FirebaseFirestore
    var counter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kBinding = ActivityKonsultasiBinding.inflate(layoutInflater)
        setContentView(kBinding.root)

        kBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        kBinding.recyclerView.setHasFixedSize(true)

        konsultasiList = arrayListOf()
        kBerjalanAdapter = kBerjalanAdapter(konsultasiList)
        EventChangeListener()

        /**kBinding.fm1Btn.setOnClickListener {
            replaceFragment(PermintaanKonsultasiFragment())
        }

        kBinding.fm2Btn.setOnClickListener {
            replaceFragment(KonsultasiBerjalanFragment())
        } **/
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("konsultasi")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
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
                            konsultasiList.add(dc.document.toObject(konsultasi::class.java))
                        }
                    }
                    kBinding.recyclerView.adapter = kBerjalanAdapter
                    kBerjalanAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    /**private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_konsultasi, fragment)
        fragmentTransaction.commit()
    }**/
}
