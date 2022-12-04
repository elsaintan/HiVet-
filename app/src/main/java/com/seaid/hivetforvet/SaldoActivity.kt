package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.seaid.hivetforvet.adapters.ViewPagerAdapter
import com.seaid.hivetforvet.models.Saldo

class SaldoActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var saldoTV : TextView
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewPager)
        saldoTV = findViewById(R.id.saldoTv)

        tabLayout.addTab(tabLayout.newTab().setText("Tarik Saldo"))
        tabLayout.addTab(tabLayout.newTab().setText("RIwayat"))

        hitungSaldo()

        val adapter = ViewPagerAdapter(this,supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }

    private fun hitungSaldo() {
        mAuth = FirebaseAuth.getInstance()
        var saldo = 0
        val data = FirebaseDatabase.getInstance().getReference("saldo")
        data.orderByChild("id_drh")
            .equalTo(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val item : Saldo? = snapshot.getValue(Saldo::class.java)
                        saldo += item!!.jumlah!!.toDouble().toInt()
                    }
                    saldoTV.text = "Rp $saldo"
                }
                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })

    }
}