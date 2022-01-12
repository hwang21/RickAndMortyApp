package com.clover.rickandmorty.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.clover.rickandmorty.R
import com.clover.rickandmorty.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, MainFragment.newInstance())
            .commit()
    }
}