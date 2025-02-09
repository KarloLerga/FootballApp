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
import com.google.gson.Gson
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

    fun fetchCompetitionTable(championship: String) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            var retries = 0
            var success = false

            while (retries < 3 && !success) {
                try {
                    Log.d("FootballViewModel", "Fetching table for $championship... Attempt $retries")
                    val table = apiService.getCompetitionTable(championship)
                    Log.d("FootballViewModel", "Fetched ${table.size} teams")
                    _competitionTable.value = table
                    success = true
                } catch (e: HttpException) {
                    Log.e("FootballViewModel", "HTTP Exception: ${e.code()} - ${e.message()}")
                    if (e.code() == 429) {
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

    fun fetchCompetitions() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.e("FootballViewModel", "No internet connection detected")
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            var retries = 0
            var success = false

            while (retries < 3 && !success) {
                try {
                    Log.d("FootballViewModel", "Fetching competitions... Attempt $retries")
                    val responseBody = apiService.getCompetitions()
                    val jsonString = responseBody.string()
                    Log.d("FootballViewModel", "Raw API Response: $jsonString")

                    if (jsonString.isEmpty()) {
                        Log.e("FootballViewModel", "⚠ API returned empty response!")
                        return@launch
                    }

                    // 🚀 Ispravljen JSON parsing
                    val cleanedString = jsonString.trim().removeSurrounding("\"", "\"")
                    val competitionsList = cleanedString.split(",").map { it.trim() }

                    Log.d("FootballViewModel", "Parsed competitions (${competitionsList.size}): $competitionsList")

                    _competitions.postValue(competitionsList.map { Competition(it) })
                    success = true
                } catch (e: HttpException) {
                    Log.e("FootballViewModel", "HTTP Exception (${e.code()}): ${e.message()}")
                    if (e.code() == 429) {
                        retries++
                        delay(2 * retries * 1000L)
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
        }
    }



}
