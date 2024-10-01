package com.pavlig43.roofapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SaveDialog(
    saveNameFile: String,
    saveFile: () -> Unit,
    checkSaveName: (String) -> Unit,
    onDismiss: () -> Unit,
    isValid: Boolean,
) {
    AlertDialog(
        title = { Text(text = "Сохранить с именем") },
        text = {
            Column {
                OutlinedTextField(value = saveNameFile, onValueChange = checkSaveName)
                if (!isValid) Text(text = "Уже такое есть")
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    saveFile()
                    onDismiss()
                },
                enabled = isValid,
            ) {
                Text(text = "Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Отмена")
            }
        },
    )
}
