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
        "X-RapidAPI-Key: 5d1cd30a5bmsh45da67630d2260bp1709eajsn431b76edb7b0",
        "X-RapidAPI-Host: football98.p.rapidapi.com"
    )
    @GET("competitions")
    suspend fun getCompetitions(): ResponseBody

    @Headers(
        "X-RapidAPI-Key: 5d1cd30a5bmsh45da67630d2260bp1709eajsn431b76edb7b0",
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
