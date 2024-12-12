package com.pavlig43.roofapp.navigation.ui

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pavlig43.roofapp.navigation.destination.ScreenDestination

@Composable
fun NestedDrawerItem(
    screenDestination: ScreenDestination,
    selectedScreen: String,
    toNavigate: (String) -> Unit = {},
) {
    NavigationDrawerItem(
        label = { Text(text = stringResource(screenDestination.title)) },
        selected = screenDestination.route == selectedScreen,
        onClick = { toNavigate(screenDestination.route) },
    )
}
