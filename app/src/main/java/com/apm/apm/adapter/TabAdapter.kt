package com.apm.apm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.apm.apm.FutureConcertsFragment
import com.apm.apm.PastConcertsFragment

class TabAdapter(fragmentManager: FragmentManager, private val artistName: String) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FutureConcertsFragment.newInstance(artistName)
            1 -> PastConcertsFragment.newInstance(artistName)
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Próximos"
            1 -> "Pasados"
            else -> null
        }
    }
}