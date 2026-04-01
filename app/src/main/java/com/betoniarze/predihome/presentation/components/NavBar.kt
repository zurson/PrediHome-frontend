package com.betoniarze.predihome.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem

@Composable
fun NavBar(
    navController: NavHostController,
    navigationItems: List<NavigationItem>,
    selectedView: MutableState<NavigationItem>,
    showNavBar: MutableState<Boolean>
) {
    val navigationItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.inversePrimary,
        disabledIconColor = MaterialTheme.colorScheme.tertiary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedTextColor = MaterialTheme.colorScheme.inversePrimary,
        disabledTextColor = MaterialTheme.colorScheme.tertiary,
        indicatorColor = Color.Transparent
    )

    val strokeSize = integerResource(R.integer.navbar_stroke_size).toFloat()

    NavigationBar(
        modifier = Modifier
            .alpha(if (showNavBar.value) 1f else 0f)
            .fillMaxHeight(if(showNavBar.value) 0.15f else 0f)
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    navigationItemColors.unselectedIconColor,
                    Offset(0f, 0f),
                    Offset(size.width, 0f),
                    strokeSize,
                )
            },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        navigationItems.forEach { view ->
            NavigationBarItem(
                modifier = Modifier.wrapContentHeight(),
                label = {
                    Text(
                        text = stringResource(view.title!!),
                        fontSize = dimensionResource(R.dimen.navbar_text_size).value.sp
                    )
                },
                alwaysShowLabel = true,
                selected = view.route == selectedView.value.route,
                icon = {
                    if(view.route == selectedView.value.route)
                        Icon(
                            painter = painterResource(view.selectedIcon!!),
                            contentDescription = view.route,
                            modifier = Modifier.size(dimensionResource(R.dimen.navbar_icon_size))
                        )
                    else Icon(
                        painter = painterResource(view.unselectedIcon!!),
                        contentDescription = view.route,
                        modifier = Modifier.size(dimensionResource(R.dimen.navbar_icon_size))
                    )
                },
                onClick = {
                    navController.navigate(view.route) {
                        popUpTo(selectedView.value.route) { inclusive = true }
                    }

                    selectedView.value = view
                },
                colors = navigationItemColors
            )
        }
    }
}