package com.pavlig43.roofapp.ui.shapes.random

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.theme.Roof_appTheme

/**
 * Инструкция для пользователя по устоновке координат точек
 */
@Composable
fun ManualDialog(
    closeManualDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = closeManualDialog) {
        LazyColumn(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
        ) {
            item {
                ManualText()
                Button(closeManualDialog) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
private fun ManualText() {
    val textAndImage = mapOf(
        R.string.dot_manual_1 to R.drawable.dot_manual_1,
        R.string.dot_manual_2 to R.drawable.dot_manual_2,
        R.string.dot_manual_3 to R.drawable.dot_manual_3

    )
    Column(modifier = Modifier.fillMaxSize()) {
        textAndImage.forEach { (text, image) ->
            Text(text = stringResource(id = text), color = MaterialTheme.colorScheme.onBackground)
            Image(painterResource(image), "dot_manual")
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ManualDialogPreview() {
    Roof_appTheme {
        ManualDialog({})
    }
}
