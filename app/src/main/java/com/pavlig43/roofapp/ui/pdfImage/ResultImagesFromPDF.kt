package com.pavlig43.roofapp.ui.pdfImage

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roofapp.ui.kit.SaveDialog
import com.rizzi.bouquet.VerticalPdfReaderState

@Composable
fun ResultImagesFromPDF(
    returnToCalculateScreen: () -> Unit = {},
    viewModel: PDFImageViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val isValidName by viewModel.isValidName.collectAsState()
    val fileName by viewModel.saveFileName.collectAsState()
    val pdfReaderState by viewModel.verticalPdfReaderState.collectAsState()
    Log.d("fileState", pdfReaderState?.file.toString())
    ResultImagesFromPDFp(
        returnToCalculateScreen = returnToCalculateScreen,
        pdfReaderState = pdfReaderState,
        shareFile = viewModel::shareFile,
        saveFile = viewModel::saveFile,
        fileName = fileName,
        onValueChangeName = viewModel::changeName,
        isValidName = isValidName,
        modifier = modifier

    )
}

@Suppress("LongParameterList")
@Composable
private fun ResultImagesFromPDFp(
    returnToCalculateScreen: () -> Unit,
    pdfReaderState: VerticalPdfReaderState?,
    shareFile: () -> Unit,
    saveFile: () -> Unit,
    fileName: String,
    onValueChangeName: (String) -> Unit,
    isValidName: Boolean,
    modifier: Modifier = Modifier,
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    BackHandler {
        returnToCalculateScreen()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (openDialog) {
            SaveDialog(
                onDismiss = { openDialog = false },
                saveFileName = fileName,
                saveFile = saveFile,
                onValueChangeName = onValueChangeName,
                isValid = isValidName,
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = shareFile) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }
            IconButton(onClick = {
                openDialog = !openDialog
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }

        pdfReaderState?.let { PDFView(it) }
    }
}
