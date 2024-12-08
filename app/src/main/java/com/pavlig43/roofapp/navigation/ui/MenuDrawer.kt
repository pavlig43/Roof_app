package com.pavlig43.roofapp.navigation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MenuDrawer(
    selectedScreen: String,
    toNavigate: (String) -> Unit,
    listMenuWrapper: List<MenuWrapper>,
    changeExpanded: (MenuWrapper) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(listMenuWrapper, key = { it.title }) { menuWrapper ->
            MenuWrapperItem(
                menuWrapper = menuWrapper,
                selectedScreen = selectedScreen,
                changeExpanded = changeExpanded,
                toNavigate = toNavigate,

            )
        }
    }
}
