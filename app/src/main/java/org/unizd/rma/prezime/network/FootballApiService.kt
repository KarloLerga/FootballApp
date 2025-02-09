package com.example.footballapp.network

import com.example.footballapp.models.Competition
import com.example.footballapp.models.Team
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Headers

interface FootballApiService {

    @Headers(
        "X-RapidAPI-Key: 0ccfd10ea5msh8fb67c7b8828041p15dd4fjsn5ebc3a0585ea",
        "X-RapidAPI-Host: football98.p.rapidapi.com"
    )
    @GET("competitions")
    suspend fun getCompetitions(): ResponseBody

    @Headers(
        "X-RapidAPI-Key: 0ccfd10ea5msh8fb67c7b8828041p15dd4fjsn5ebc3a0585ea",
        "X-RapidAPI-Host: football98.p.rapidapi.com"
    )
    @GET("{championship}/table")
    suspend fun getCompetitionTable(@Path("championship") championship: String): List<Team>

    companion object {
        private const val BASE_URL = "https://football98.p.rapidapi.com/"

        fun create(): FootballApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FootballApiService::class.java)
        }
    }
}
