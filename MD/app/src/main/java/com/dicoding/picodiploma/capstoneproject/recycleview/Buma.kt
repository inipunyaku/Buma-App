package com.dicoding.picodiploma.capstoneproject.recycleview

data class Buma(val data: ArrayList<Result>) {
    data class Result(
        val title: String,
        val tags: String,
        val body: String,
        val image: String,
    )
}
