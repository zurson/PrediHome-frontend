package com.betoniarze.predihome.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    selectedView: MutableState<NavigationItem>,
    scrollBehavior: TopAppBarScrollBehavior,
    showProfileButton: MutableState<Boolean>,
    onProfileClick: (MutableState<Boolean>) -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            if(selectedView.value.title != null) {
                Text(
                    text = stringResource(selectedView.value.title!!),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = dimensionResource(id = R.dimen.appbar_title_size).value.sp
                )
            }
        },
        actions = {
            if (showProfileButton.value) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = { onProfileClick.invoke(showProfileButton) },
                    enabled = showProfileButton.value
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.appbar_icon_size)),
                        contentDescription = "Profile"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}