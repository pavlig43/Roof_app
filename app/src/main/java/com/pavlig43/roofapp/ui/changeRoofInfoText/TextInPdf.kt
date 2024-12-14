package com.pavlig43.roofapp.ui.changeRoofInfoText

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TextInPdf(
    modifier: Modifier = Modifier,
    viewModel: TextInPdfViewModel = hiltViewModel(),
) {
    PageText(
        viewModel.text.toString(),
        modifier = modifier
    )
}

@Composable
fun PageText(
    pageText: String,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = pageText,
        onValueChange = {},
        modifier = modifier
    )
}
