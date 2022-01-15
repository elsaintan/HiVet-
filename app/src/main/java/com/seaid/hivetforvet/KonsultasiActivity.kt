package com.seaid.hivetforvet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.seaid.hivetforvet.databinding.ActivityKonsultasiBinding

class KonsultasiActivity : AppCompatActivity() {

    lateinit var kBinding: ActivityKonsultasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kBinding = ActivityKonsultasiBinding.inflate(layoutInflater)
        setContentView(kBinding.root)

        kBinding.fm1Btn.setOnClickListener {
            replaceFragment(PermintaanKonsultasiFragment())
        }

        kBinding.fm2Btn.setOnClickListener {
            replaceFragment(KonsultasiBerjalanFragment())
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_konsultasi, fragment)
        fragmentTransaction.commit()
    }
}