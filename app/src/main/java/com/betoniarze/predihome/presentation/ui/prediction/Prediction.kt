package com.betoniarze.predihome.presentation.ui.prediction

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.ui.prediction.map.MapView
import com.betoniarze.predihome.presentation.viewmodel.PredictionViewModel

@Composable
fun Predict(
    parentNavController: NavHostController,
    predictionViewModel: PredictionViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationItem.PredictionMap.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.KeepUntilTransitionsFinished }
    ) {
        composable(NavigationItem.Home.route) {
            navController.clearBackStack(NavigationItem.PredictionMap.route)
            parentNavController.navigate(NavigationItem.Home.route)
        }
        composable(NavigationItem.PredictionMap.route) {
            MapView(navController, predictionViewModel)
        }
        composable(NavigationItem.PredictionParameters.route) {
            PredictionParametersView(
                navController,
                predictionViewModel
            )
        }
        composable(NavigationItem.PredictionResult.route) {
            PredictionResultView(navController, predictionViewModel)
        }
    }
}