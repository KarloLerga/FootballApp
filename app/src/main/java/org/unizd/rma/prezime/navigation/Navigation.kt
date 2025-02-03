package org.unizd.rma.prezime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.unizd.rma.prezime.ui.DetailScreen
import org.unizd.rma.prezime.ui.MainScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("detail/{competition}") { backStackEntry ->
            val competitionName = backStackEntry.arguments?.getString("competition") ?: ""
            DetailScreen(navController, competitionName)
        }
    }
}
