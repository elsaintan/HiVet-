package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.seaid.hivetforvet.adapters.MessageAdapter
import com.seaid.hivetforvet.adapters.kBerjalanAdapter
import com.seaid.hivetforvet.databinding.ActivityKonsultasiBinding
import com.seaid.hivetforvet.models.Chat
import com.seaid.hivetforvet.models.konsultasi

class KonsultasiActivity : AppCompatActivity() {

    lateinit var kBinding: ActivityKonsultasiBinding
    private lateinit var konsultasiList: ArrayList<konsultasi>
    private lateinit var kBerjalanAdapter: kBerjalanAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var reference: DatabaseReference
    var counter: Int = 0

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
        val reference = FirebaseDatabase.getInstance().getReference("konsultasi").orderByKey().limitToLast(100)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                konsultasiList.clear()
                for (snapshot in snapshot.children) {
                    val data: konsultasi? = snapshot.getValue(konsultasi::class.java)
                    if (data?.status!!.equals("1") || data.status.equals("2") || data.status.equals("3")) {
                        konsultasiList.add(data)
                        //Toast.makeText(this@KonsultasiActivity, "ADA DATANYA", Toast.LENGTH_SHORT).show()
                    }
                    kBinding.recyclerView.adapter = kBerjalanAdapter
                }
                kBerjalanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException();
            }
        })

        /**reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                konsultasiList.clear()
                for (snapshot in snapshot.children) {
                    val data: konsultasi? = snapshot.getValue(konsultasi::class.java)
                    if (data?.status!!.equals("1") || data.status.equals("2") || data.status.equals("3")) {
                        konsultasiList.add(data)
                        //Toast.makeText(this@KonsultasiActivity, "ADA DATANYA", Toast.LENGTH_SHORT).show()
                    }
                    kBinding.recyclerView.adapter = kBerjalanAdapter
                }
                kBerjalanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException();
            }

        })**/
    }

    /**private fun EventChangeListener() {
        //val list : MutableList<konsultasi> = mutableListOf<konsultasi>()
        val konsulList = ArrayList<konsultasi>()
        val reference = FirebaseDatabase.getInstance().getReference("konsultasi")
        reference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
        konsultasiList.clear()
        for (snapshot in dataSnapshot.children) {
        val data: konsultasi? = snapshot.getValue(konsultasi::class.java)
        if (data?.status!!.equals("1") || data.status.equals("2") || data.status.equals("3")) {
        konsulList.add(data)
        Toast.makeText(this@KonsultasiActivity, "ADA DATANYA", Toast.LENGTH_SHORT).show()
        }
        kBinding.recyclerView.adapter = kBerjalanAdapter
        kBerjalanAdapter.notifyDataSetChanged()
        }

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
        })

        konsulList.forEach(System.out::print)

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
                            val data = dc.document.toObject(konsultasi::class.java)
                            if (data.status!!.equals("1") || data.status.equals("2") || data.status.equals("3")) {
                                konsultasiList.add(dc.document.toObject(konsultasi::class.java))
                            }
                        }
                    }
                    kBinding.recyclerView.adapter = kBerjalanAdapter
                    kBerjalanAdapter.notifyDataSetChanged()
                }
            })
    }**/


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
