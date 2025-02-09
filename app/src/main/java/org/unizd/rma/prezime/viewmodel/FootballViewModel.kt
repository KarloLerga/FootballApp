package com.example.footballapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footballapp.models.Competition
import com.example.footballapp.models.Team
import com.example.footballapp.network.FootballApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.unizd.rma.prezime.utils.NetworkUtils
import retrofit2.HttpException
import java.io.IOException

class FootballViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = FootballApiService.create()
    private val context = application.applicationContext

    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> get() = _competitions

    private val _competitionTable = MutableLiveData<List<Team>>()
    val competitionTable: LiveData<List<Team>> get() = _competitionTable

    /**
     * 📌 Poboljšano dohvaćanje liga s optimiziranim rate-limit retry mehanizmom.
     */
    fun fetchCompetitions() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.e("FootballViewModel", "No internet connection detected")
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            var retries = 0
            val maxRetries = 5 // Dozvoljeno najviše 5 pokušaja
            var backoffTime = 2000L // Početno čekanje 2 sekunde

            while (retries < maxRetries) {
                try {
                    Log.d("FootballViewModel", "Fetching competitions... Attempt $retries")
                    val responseBody = apiService.getCompetitions()
                    val jsonString = responseBody.string().trim()

                    if (jsonString.isEmpty()) {
                        Log.e("FootballViewModel", "⚠ API returned empty response!")
                        break // Umjesto return koristimo break
                    }

                    val competitionsList = jsonString.removeSurrounding("\"", "\"")
                        .split(",").map { it.trim() }

                    Log.d("FootballViewModel", "Parsed competitions (${competitionsList.size}): $competitionsList")

                    _competitions.postValue(competitionsList.map { Competition(it) })
                    break // Ako uspije, prekini retry petlju

                } catch (e: HttpException) {
                    Log.e("FootballViewModel", "HTTP Exception (${e.code()}): ${e.message()}")
                    if (e.code() == 429) {
                        retries++
                        Log.w("FootballViewModel", "Rate limit hit! Retrying in $backoffTime ms...")
                        delay(backoffTime)
                        backoffTime *= 2 // **Exponential backoff** (povećavamo čekanje)
                    } else {
                        break
                    }
                } catch (e: IOException) {
                    Log.e("FootballViewModel", "Network Error: ${e.message}")
                    break
                } catch (e: Exception) {
                    Log.e("FootballViewModel", "Unexpected error: ${e.message}")
                    break
                }
            }
            Log.e("FootballViewModel", "Failed to fetch competitions after $maxRetries attempts.")
        }
    }

    /**
     * 📌 Poboljšano dohvaćanje tablice s provjerom ispravnog imena natjecanja.
     */
    fun fetchCompetitionTable(championship: String) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            var retries = 0
            var success = false

            // 🔹 Ensure proper championship name formatting
            val formattedChampionship = championship.lowercase().replace(" ", "")

            while (retries < 3 && !success) {
                try {
                    Log.d("FootballViewModel", "Fetching table for $formattedChampionship...")
                    val table = apiService.getCompetitionTable(formattedChampionship)

                    if (table.isNotEmpty()) {
                        Log.d("FootballViewModel", "Fetched ${table.size} teams")
                        _competitionTable.value = table
                        success = true
                    } else {
                        Log.w("FootballViewModel", "⚠ No teams found for $formattedChampionship")
                    }
                } catch (e: HttpException) {
                    Log.e("FootballViewModel", "HTTP Exception: ${e.code()} - ${e.message()}")
                    if (e.code() == 429) {  // Rate limit error
                        retries++
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Rate limit exceeded. Retrying...", Toast.LENGTH_SHORT).show()
                        }
                        delay(2 * retries * 1000L)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error fetching data", Toast.LENGTH_LONG).show()
                        }
                        break
                    }
                } catch (e: IOException) {
                    Log.e("FootballViewModel", "Network Error: ${e.message}")
                    break
                } catch (e: Exception) {
                    Log.e("FootballViewModel", "Unexpected error: ${e.message}")
                    break
                }
            }
        }
    }


}
