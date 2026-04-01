package com.betoniarze.predihome.presentation.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.presentation.components.AppScaffold
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.ui.form.ProfileView
import com.betoniarze.predihome.presentation.ui.home.HomeView
import com.betoniarze.predihome.presentation.ui.lists.PopularRegionsView
import com.betoniarze.predihome.presentation.ui.lists.history.HistoryView
import com.betoniarze.predihome.presentation.ui.prediction.Predict
import com.betoniarze.predihome.presentation.viewmodel.PredictionViewModel

@Preview(
    name = "main", group = "main",
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    apiLevel = 34
)
@Composable
fun MainView() {
    Theme.MainTheme {
        val navBarController = rememberNavController()
        val predictionViewModel: PredictionViewModel = viewModel()

        AppScaffold(
            navBarController = navBarController,
        ) { contentPadding ->
            NavHost(
                navController = navBarController,
                startDestination = NavigationItem.Home.route,
                modifier = Modifier.padding(contentPadding),
                enterTransition = {
                    if (this.targetState.destination.route == NavigationItem.Profile.route)
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(200)
                        )
                    else
                        EnterTransition.None
                },
                exitTransition = {
                    if (this.initialState.destination.route == NavigationItem.Profile.route)
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(200)
                        )
                    else
                        ExitTransition.KeepUntilTransitionsFinished }
            ) {
                composable(NavigationItem.Home.route) {
                    navBarController.clearBackStack(NavigationItem.Home.route)
                    HomeView()
                }
                composable(NavigationItem.History.route) { HistoryView() }
                composable(NavigationItem.PopularRegions.route) { PopularRegionsView() }
                composable(NavigationItem.Profile.route) {
                    ProfileView()
                }
                composable(NavigationItem.Predict.route) {
                    navBarController.clearBackStack(NavigationItem.Home.route)
                    Predict(
                        parentNavController = navBarController,
                        predictionViewModel = predictionViewModel
                    )
                }
            }
        }
    }
}
