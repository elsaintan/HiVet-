package com.seaid.hivetforvet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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

        db = FirebaseFirestore.getInstance()
        db.collection("saldo")
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(Saldo::class.java)
                val items = data.size
                if (items > 0){
                    for (item in data){
                        if (item.id_drh == mAuth.currentUser!!.uid){
                            saldoList.add(item)
                        }
                    }
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

        return fgv2
    }


}