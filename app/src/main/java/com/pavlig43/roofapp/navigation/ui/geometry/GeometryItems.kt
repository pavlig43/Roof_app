package com.pavlig43.roofapp.navigation.ui.geometry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GeometryItems(
    listGeometryItemNav: List<GeometryItemNav>,
    onMove: (String) -> Unit = {},
    size: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            listGeometryItemNav,
            key = { it.route }
        ) {
            CircleButton(
                onClick = onMove,
                geometryItemNav = it,
                size = size,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
