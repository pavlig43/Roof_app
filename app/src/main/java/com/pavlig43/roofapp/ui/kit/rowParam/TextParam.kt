package com.pavlig43.roofapp.ui.kit.rowParam

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun TextParam(
    paramTitle: Int,
    modifier: Modifier = Modifier,
    unit: Int? = null,
) {
    Text(
        "${stringResource(paramTitle)} ${unit?.let { "(${stringResource(it)})" } ?: ""}",
        modifier = modifier,
    )
}
