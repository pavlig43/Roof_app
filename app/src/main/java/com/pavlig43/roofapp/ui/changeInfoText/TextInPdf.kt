package com.pavlig43.roofapp.ui.changeInfoText

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R

@Composable
fun TextInPdf(
    showResult: (String) -> Unit,
    onBackNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TextInPdfViewModel = hiltViewModel(),
) {
    val allText by viewModel.allText.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TextField(
                value = allText,
                onValueChange = viewModel::changePageText,
            )
        }
        item {
            ButtonRow(
                cancel = onBackNavigation,
                saveAndShowResult = {
                    viewModel.showResult { filePath -> showResult(filePath) }
                }
            )
        }
    }
}

@Composable
private fun ButtonRow(
    cancel: () -> Unit,
    saveAndShowResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = cancel) {
            Text(stringResource(R.string.cancel))
        }
        Button(onClick = saveAndShowResult) {
            Text(stringResource(R.string.save))
        }
    }
}
