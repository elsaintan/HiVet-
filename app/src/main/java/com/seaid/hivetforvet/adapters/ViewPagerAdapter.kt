package com.seaid.hivetforvet.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.seaid.hivetforvet.RiwayatSaldoFragment
import com.seaid.hivetforvet.TarikSaldoFragment

class ViewPagerAdapter (var context: Context, fm: FragmentManager, var totalTabs: Int ) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TarikSaldoFragment()
            }

            1 -> {
                RiwayatSaldoFragment()
            }

            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}