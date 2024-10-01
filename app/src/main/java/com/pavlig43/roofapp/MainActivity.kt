package com.pavlig43.roofapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pavlig43.roofapp.navigation.DrawerNavigation
import com.pavlig43.roofapp.navigation.NavigationGraph
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Roof_appTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val navHostController = rememberNavController()
    val scrollState = rememberScrollState()
    DrawerNavigation(
        drawerState = drawerState,
        navController = navHostController,
        coroutineScope = coroutineScope,
        scrollState = scrollState,
        content = {
            Scaffold(topBar = {
                RoofAppBar(
                    openNavigationDrawer = { coroutineScope.launch { drawerState.open() } },
                )
            }) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigationGraph(
                        navController = navHostController,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoofAppBar(openNavigationDrawer: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = openNavigationDrawer) {
                Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = null)
            }
        },
    )
}
