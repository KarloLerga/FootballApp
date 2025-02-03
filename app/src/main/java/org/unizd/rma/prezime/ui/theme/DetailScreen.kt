package org.unizd.rma.prezime.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, competitionName: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Detalji za $competitionName", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { navController.popBackStack() }) {
            Text("Natrag")
        }
    }
}
