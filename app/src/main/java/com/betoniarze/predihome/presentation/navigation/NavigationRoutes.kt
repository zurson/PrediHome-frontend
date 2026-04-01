package com.betoniarze.predihome.presentation.navigation

object NavigationRoutes {
    val navBarRoutes = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.PopularRegions,
        NavigationItem.Profile,
        NavigationItem.Predict
    )

    val predictionRoutes = listOf(
        NavigationItem.PredictionMap ,
        NavigationItem.PredictionParameters,
        NavigationItem.PredictionResult
    )
}