package com.pavlig43.roof_app.navigation

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen = currentBackStackEntry?.destination?.route ?: TileLayout.route
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    DrawerItem(
                        screenDestination = TileLayout,
                        selectedScreen = selectedScreen,
                        toNavigate = { route ->
                            navController.navigate(route)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        screenDestination = Shapes,
                        selectedScreen = selectedScreen,
                        toNavigate = { route ->
                            navController.navigate(route)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        screenDestination = SaveDocuments,
                        selectedScreen = selectedScreen,
                        toNavigate = { route ->
                            navController.navigate(route)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                }
            }
        },
        content = content
    )

}

@Composable
fun DrawerItem(
    screenDestination: ScreensDestination,
    selectedScreen: String,
    toNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    NavigationDrawerItem(
        label =
        { Text(text = screenDestination.title) },
        selected = screenDestination.route == selectedScreen,
        icon = {

        },
        onClick = {
            toNavigate(screenDestination.route)
        },
//        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = MaterialTheme.colorScheme.onSurface)
    )
}

