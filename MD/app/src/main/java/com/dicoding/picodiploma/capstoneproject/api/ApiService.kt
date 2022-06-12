package com.dicoding.picodiploma.capstoneproject.api

import com.dicoding.picodiploma.capstoneproject.recycleview.Buma
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<Buma>
}
