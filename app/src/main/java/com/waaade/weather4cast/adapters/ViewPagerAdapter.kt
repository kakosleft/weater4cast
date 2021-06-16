package com.waaade.weather4cast.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waaade.weather4cast.fragments.FragmentBodyDaily
import com.waaade.weather4cast.fragments.FragmentBodyHourly

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            FragmentBodyHourly()
        }else{
            FragmentBodyDaily()
        }

    }
}

