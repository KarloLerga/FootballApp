package org.unizd.rma.prezime.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("competitions")
    fun getCompetitions(): Call<String> // ✅ Get raw string response
}
