package com.pavlig43.roofapp.ui.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog

/**
 * Инструкция для пользователя по устоновке координат точек
 */
@Composable
fun ManualDialog(
    modifier: Modifier = Modifier,
    closeManualDialog: () -> Unit,
    text: Int,
) {
    Dialog(onDismissRequest = closeManualDialog) {
        LazyColumn(
            modifier = modifier.background(Color.White),
        ) {
            item {
                ShapeManual(text = text)
                Button(closeManualDialog) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun ShapeManual(
    modifier: Modifier = Modifier,
    text: Int,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(stringResource(text))
    }
}
