package com.seaid.hivetforvet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.adapters.SaldoAdapter
import com.seaid.hivetforvet.models.Saldo


class RiwayatSaldoFragment : Fragment(){

    private lateinit var saldoList: ArrayList<Saldo>
    private lateinit var adapter: SaldoAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fgv2 = inflater.inflate(R.layout.fragment_riwayat_saldo, container, false)

        val recyclerView : RecyclerView = fgv2.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        saldoList = arrayListOf()
        adapter = SaldoAdapter(saldoList)

        mAuth = FirebaseAuth.getInstance()

        val data = FirebaseDatabase.getInstance().getReference("saldo")
        data.orderByChild("id_drh")
            .equalTo(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val item : Saldo? = snapshot.getValue(Saldo::class.java)
                        saldoList.add(item!!)
                    }
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })

        return fgv2
    }


}