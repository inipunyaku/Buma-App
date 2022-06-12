package com.dicoding.picodiploma.capstoneproject.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityBackgroundBinding

class BackgroundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBackgroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBack()
    }

    private fun setupBack() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}