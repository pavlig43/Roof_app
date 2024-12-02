package com.pavlig43.roofapp.ui.kit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ArrowIconButton(
    expanded: Boolean,
    changeExpanded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = changeExpanded,
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            modifier = modifier,
        )
    }
}
