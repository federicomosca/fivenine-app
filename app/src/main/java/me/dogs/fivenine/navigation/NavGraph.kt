package me.dogs.fivenine.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.dogs.fivenine.data.local.AppDatabase
import me.dogs.fivenine.presentation.details.DetailsScreen
import me.dogs.fivenine.presentation.details.DetailsViewModel
import me.dogs.fivenine.presentation.home.HomeScreen
import me.dogs.fivenine.presentation.home.HomeViewModel

@Composable
fun NavGraph(database: AppDatabase) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            val viewModel = HomeViewModel(database)
            HomeScreen(
                viewModel = viewModel,
                onListClick = { listId ->
                    navController.navigate("details/$listId")
                }
            )
        }
        composable("details/{listId}") { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId")?.toInt() ?: 0
            val viewModel = DetailsViewModel(database, listId)
            DetailsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
