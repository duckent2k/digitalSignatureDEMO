package com.example.digitalsignature.adapter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.digitalsignature.fragments.rsa.KeyFragment
import com.example.digitalsignature.fragments.rsa.ReceiverFragment
import com.example.digitalsignature.fragments.rsa.SenderFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        Log.d("POSITION", "viewPagerPosition:  $position")
        return when (position) {
            0 -> KeyFragment()
            1 -> SenderFragment()
            else -> ReceiverFragment()
        }
    }
}