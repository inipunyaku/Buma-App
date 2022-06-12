package com.dicoding.picodiploma.capstoneproject.recycleview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.capstoneproject.R
import com.dicoding.picodiploma.capstoneproject.api.ApiConfig
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityBumaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BumaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBumaBinding
    private lateinit var bumaAdapter: BumaAdapter
    private val TAG: String = "BumaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBumaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "HERBS"
    }

    override fun onStart() {
        super.onStart()
        getDataFromApi()
        setupAdapter()
    }

    private fun setupAdapter() {
        val buma = findViewById<RecyclerView>(R.id.rv_buma)
        bumaAdapter = BumaAdapter(arrayListOf(), object : BumaAdapter.OnAdapterListener {
            override fun onClick(result: Buma.Result) {
                startActivity(Intent(this@BumaActivity, DetailActivity::class.java)
                    .putExtra("intent_image", result.image)
                    .putExtra("intent_judul", result.tags)
                    .putExtra("intent_title", result.title)
                    .putExtra("intent_content", result.body)

                )
            }

        })
        buma.apply {
            layoutManager = LinearLayoutManager(this@BumaActivity)
            adapter = bumaAdapter
        }


    }

    private fun getDataFromApi() {
        binding.progressBar.visibility = View.VISIBLE
        ApiConfig.endpoint.getArticles()
            .enqueue(object : Callback<Buma> {
                override fun onResponse(call: Call<Buma>, response: Response<Buma>) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        showResult(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<Buma>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    printLog(t.toString())
                }

            })
    }

    private fun printLog(message: String) {
        Log.d(TAG, message)
    }

    private fun showResult(model: Buma) {
        val result = model.data
        bumaAdapter.setData(result)
    }
}


