package org.unizd.rma.prezime.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://football98.p.rapidapi.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-key", "a16b5f29e5msh2638426965d8425p17a27bjsnae1e050bca75") // 🚨 ZAMIJENI API KLJUČ!
                .addHeader("x-rapidapi-host", "football98.p.rapidapi.com")
                .build()
            chain.proceed(request)
        })
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }) // Logiranje API odgovora
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
