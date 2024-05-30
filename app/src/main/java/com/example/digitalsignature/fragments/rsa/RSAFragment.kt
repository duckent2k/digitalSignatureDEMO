package com.example.digitalsignature.fragments.rsa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.digitalsignature.adapter.ViewPagerAdapter
import com.example.digitalsignature.databinding.FragmentRSABinding
import com.google.android.material.tabs.TabLayout

class RSAFragment : Fragment() {

    private lateinit var binding: FragmentRSABinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRSABinding.inflate(layoutInflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() {
        adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)

        with(binding) {
            tabLayout.addTab(tabLayout.newTab().setText("Key"))
            tabLayout.addTab(tabLayout.newTab().setText("Sender"))
            tabLayout.addTab(tabLayout.newTab().setText("Receiver"))

            viewPager2.adapter = adapter
        }

        initAction()
    }

    private fun initAction() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager2.currentItem = tab?.position!!
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                Log.d("POSITION", "tabLayoutPosition:  $position")
            }
        })
    }

}