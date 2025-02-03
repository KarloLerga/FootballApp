package org.unizd.rma.prezime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import org.unizd.rma.prezime.network.ApiClient

class MainViewModel : ViewModel() {
    var competitions by mutableStateOf<List<String>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchCompetitions() {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCompetitions().execute()
                if (response.isSuccessful) {
                    competitions = response.body() ?: emptyList()
                } else {
                    errorMessage = "Neuspješno dohvaćanje podataka"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }
}
