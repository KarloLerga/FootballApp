package org.unizd.rma.prezime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import android.util.Log
import org.unizd.rma.prezime.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    var competitions by mutableStateOf<List<String>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchCompetitions() {
        viewModelScope.launch {
            val call = ApiClient.instance.getCompetitions()
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>, response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (!data.isNullOrEmpty()) {
                            // ✅ Convert comma-separated string into a list
                            competitions = data.replace("\"", "") // Remove unnecessary quotes
                                .split(",")
                                .map { it.trim() } // Trim spaces
                                .filter { it.isNotEmpty() } // Remove empty values

                            Log.d("API_RESPONSE", "Success: ${competitions}")
                        } else {
                            errorMessage = "Error: Empty Response"
                            Log.e("API_ERROR", "Empty response")
                        }
                    } else {
                        errorMessage = "Error: ${response.message()}"
                        Log.e("API_ERROR", "Error: ${response.message()} - Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    errorMessage = "Failed to fetch data: ${t.localizedMessage}"
                    Log.e("API_ERROR", "Failure: ${t.localizedMessage}")
                }
            })
        }
    }
}
