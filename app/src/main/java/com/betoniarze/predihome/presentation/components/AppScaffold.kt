package com.betoniarze.predihome.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.betoniarze.predihome.presentation.navigation.NavigationRoutes
import com.betoniarze.predihome.presentation.navigation.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navBarController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navbarDestinations = listOf(
        NavigationItem.Home,
        NavigationItem.Predict,
        NavigationItem.History,
        NavigationItem.PopularRegions,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedView = remember { mutableStateOf(navbarDestinations[0]) }
    val navbarStack by navBarController.currentBackStackEntryAsState()

    /* Selected view */
    selectedView.value = NavigationRoutes.navBarRoutes.firstOrNull {
        it.route == navbarStack?.destination?.route
    } ?: NavigationItem.Home

    /* App bar */
    var showAppBar by rememberSaveable { mutableStateOf(true) }
    showAppBar = when(selectedView.value) {
        else -> true
    }

    /* Nav bar */
    val showNavBar = rememberSaveable { mutableStateOf(true) }
    showNavBar.value = when(selectedView.value) {
        NavigationItem.Profile -> false
        else -> true
    }

    /* Show profile button */
    val showProfileButton = rememberSaveable { mutableStateOf(true) }
    showProfileButton.value = selectedView.value != NavigationItem.Profile

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if(showAppBar) {
                AppBar(
                    selectedView = selectedView,
                    scrollBehavior = scrollBehavior,
                    showProfileButton = showProfileButton,
                    onProfileClick = { showProfileButton ->
                        if(selectedView.value != NavigationItem.Profile) {
                            showNavBar.value = false
                            showProfileButton.value = false
                            navBarController.navigate(NavigationItem.Profile.route)
                        }
                     },
                )
            }
        },
        bottomBar = {
            NavBar(navBarController, navbarDestinations, selectedView, showNavBar)
        }
    ) { contentPadding ->
        content(contentPadding)
    }
}