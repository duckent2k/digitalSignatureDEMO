package com.example.digitalsignature.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.digitalsignature.R
import com.example.digitalsignature.databinding.ActivityMainBinding
import com.example.digitalsignature.fragments.elgamal.ElgamalFragment
import com.example.digitalsignature.fragments.rsa.RSAFragment

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initView()
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    private fun initView() {
        val toggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, R.color.black)

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rsa -> {
                    binding.drawerLayout.closeDrawers()
                    binding.tvTitle.visibility = View.INVISIBLE
                    fragmentReplace(RSAFragment())
                }

                R.id.elgamal -> {
                    binding.drawerLayout.closeDrawers()
                    binding.tvTitle.visibility = View.INVISIBLE
                    fragmentReplace(ElgamalFragment())
                }
            }
            true
        }
    }

    private fun fragmentReplace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}