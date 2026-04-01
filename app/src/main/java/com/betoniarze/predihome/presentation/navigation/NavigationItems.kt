package com.betoniarze.predihome.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.betoniarze.predihome.R

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    @DrawableRes val unselectedIcon: Int? = null,
    val badgeCount: Int? = null
) {

    /** Main navigation **/
    data object Home : NavigationItem (
        route = "home",
        title = R.string.nav_home,
        selectedIcon = R.drawable.baseline_home_24,
        unselectedIcon = R.drawable.outline_home_24
    )
    data object Predict : NavigationItem(
        route = "predict",
        title = R.string.nav_predict,
        selectedIcon = R.drawable.baseline_location_pin_24,
        unselectedIcon = R.drawable.outline_location_on_24
    )
    data object History : NavigationItem (
        route = "history",
        title = R.string.nav_history,
        selectedIcon = R.drawable.baseline_event_note_24,
        unselectedIcon = R.drawable.baseline_event_note_24,
        badgeCount = 10
    )
    data object Profile : NavigationItem (
        route = "profile",
        title = R.string.nav_profile,
        selectedIcon = R.drawable.baseline_settings_24,
        unselectedIcon = R.drawable.outline_settings_24
    )
    data object PopularRegions : NavigationItem (
        route = "regions",
        title = R.string.nav_popular_regions,
        selectedIcon = R.drawable.baseline_groups_24,
        unselectedIcon = R.drawable.outline_groups_24
    )

    /** Predict navigation **/
    data object PredictionMap : NavigationItem (
        route = "prediction_map"
    )
    data object PredictionParameters : NavigationItem (
        route = "prediction_parameters"
    )
    data object PredictionResult : NavigationItem (
        route = "prediction_result"
    )

    /** Login navigation **/
    data object Login : NavigationItem (
        route = "login",
        title = R.string.nav_home
    )
    data object Register : NavigationItem (
        route = "register",
        title = R.string.nav_home
    )
    data object Select : NavigationItem(
        route = "select_view"
    )
}