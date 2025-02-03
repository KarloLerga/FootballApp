package org.unizd.rma.prezime.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.unizd.rma.prezime.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    LaunchedEffect(Unit) { viewModel.fetchCompetitions() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Nogometna natjecanja", style = MaterialTheme.typography.headlineMedium)

        if (viewModel.competitions.isEmpty()) {
            Text("Nema podataka...", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(viewModel.competitions) { competition ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("detail/$competition") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = competition,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
