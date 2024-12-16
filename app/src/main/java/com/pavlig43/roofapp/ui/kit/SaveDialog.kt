package com.pavlig43.roofapp.ui.kit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pavlig43.roof_app.R

@Composable
fun SaveDialog(
    onDismiss: () -> Unit,
    saveFileName: String,
    saveFile: () -> Unit,
    onValueChangeName: (String) -> Unit,
    isValid: Boolean,
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.save_with_name)) },
        text = {
            Column {
                OutlinedTextField(value = saveFileName, onValueChange = onValueChangeName)
                if (!isValid) Text(text = stringResource(R.string.there_is_already_such_a_thing))
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
                Text(text = stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}
