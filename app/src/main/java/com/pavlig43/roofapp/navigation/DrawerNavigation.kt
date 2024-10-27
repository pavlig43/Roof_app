package com.pavlig43.roofapp.navigation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerNavigation(
    drawerState: DrawerState,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    content: @Composable () -> Unit,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen =
        currentBackStackEntry?.destination?.route ?: AllDestination.TileLayout.route
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    AllDestination.getAllDestination().forEach {
                        DrawerItem(
                            screenDestination = it,
                            selectedScreen = selectedScreen,
                            toNavigate = { route ->
                                navController.navigate(route)
                                coroutineScope.launch { drawerState.close() }
                            },
                        )
                    }
                }
            }
        },
        content = content,
    )
}

@Composable
fun DrawerItem(
    screenDestination: ScreensDestination,
    selectedScreen: String,
    toNavigate: (String) -> Unit = {},
) {
    NavigationDrawerItem(
        label = { Text(text = stringResource(screenDestination.title)) },
        selected = screenDestination.route == selectedScreen,
        icon = { },
        onClick = { toNavigate(screenDestination.route) },
    )
}
