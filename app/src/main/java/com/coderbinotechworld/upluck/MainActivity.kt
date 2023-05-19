package com.coderbinotechworld.upluck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.coderbinotechworld.upluck.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.BNV.setOnItemSelectedListener(){

            when(it.itemId){

                R.id.home -> replaceFragment(HomeFragment())

                R.id.more -> replaceFragment(MoreFragment())

                else -> {}

            }
            true

        }

    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FL, fragment)
        fragmentTransaction.commit()

    }
}