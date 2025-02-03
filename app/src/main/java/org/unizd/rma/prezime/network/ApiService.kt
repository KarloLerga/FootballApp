package org.unizd.rma.prezime.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.Call

interface ApiService {
    @Headers(
        "X-RapidAPI-Key: a16b5f29e5msh2638426965d8425p17a27bjsnae1e050bca75",
        "X-RapidAPI-Host: football98.p.rapidapi.com"
    )
    @GET("competitions")
    fun getCompetitions(): Call<List<String>>
}
