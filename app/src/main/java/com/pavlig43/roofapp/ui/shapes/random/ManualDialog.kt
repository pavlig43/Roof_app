package com.pavlig43.roofapp.ui.shapes.random

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog

/**
 * Инструкция для пользователя по устоновке координат точек
 */
@Composable
fun ManualDialog(
    closeManualDialog: () -> Unit,
    text: Int,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = closeManualDialog) {
        LazyColumn(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
        ) {
            item {
                Text(stringResource(text), color = MaterialTheme.colorScheme.onBackground)
                Button(closeManualDialog) {
                    Text("OK")
                }
            }
        }
    }
}
