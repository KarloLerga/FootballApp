package org.unizd.rma.prezime.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(navController: NavController, competitionName: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Detalji za $competitionName", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ovdje mogu ići dodatni podaci API-ja")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Natrag")
        }
    }
}
