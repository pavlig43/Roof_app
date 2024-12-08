package com.pavlig43.roofapp.navigation.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pavlig43.roofapp.navigation.roofNavigation.RoofDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerNavigation(
    drawerState: DrawerState,
    navController: NavController,
    coroutineScope: CoroutineScope,
    content: @Composable () -> Unit,
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen =
        currentBackStackEntry?.destination?.route ?: RoofDestination.TileLayout.route
    val listMenuWrapper by viewModel.listMenuWrapper.collectAsState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                MenuDrawer(
                    selectedScreen = selectedScreen,
                    listMenuWrapper = listMenuWrapper,
                    changeExpanded = viewModel::changeExpanded,
                    toNavigate = {
                        navController.navigate(it)
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        },
        content = content,
    )
}
