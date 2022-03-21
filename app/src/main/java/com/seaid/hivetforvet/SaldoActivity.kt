package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
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
        var saldo = 0
        db = FirebaseFirestore.getInstance()
        db.collection("saldo")
            .get()
            .addOnSuccessListener {
                val data = it.toObjects(Saldo::class.java)
                val items = data.size
                if (items > 0) {
                    for (item in data) {
                        if (item.id_drh == mAuth.currentUser!!.uid) {
                            saldo += item.jumlah!!.toDouble().toInt()
                        }
                    }
                }
                saldoTV.text = "Rp $saldo"
            }
    }
}