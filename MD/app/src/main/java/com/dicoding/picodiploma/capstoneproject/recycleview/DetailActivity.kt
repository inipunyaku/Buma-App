package com.dicoding.picodiploma.capstoneproject.recycleview

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.capstoneproject.R
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgBuma = findViewById<ImageView>(R.id.iv_detail)

        supportActionBar?.title = intent.getStringExtra("intent_name")

        binding.tvJudul.text = intent.getStringExtra("intent_judul")
        binding.tvTitle.text = intent.getStringExtra("intent_title")
        binding.tvArticle.text = intent.getStringExtra("intent_content")
        Glide.with(this@DetailActivity)
            .load(intent.getStringExtra("intent_image"))
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(imgBuma)
    }


}