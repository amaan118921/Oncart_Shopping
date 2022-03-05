package com.example.oncart.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.oncart.fragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    private var size = 4

    override fun getCount(): Int {
        return size
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> SearchFragment()
            2 -> LikedFragment()
            else -> ProfileFragment()
        }
    }
}