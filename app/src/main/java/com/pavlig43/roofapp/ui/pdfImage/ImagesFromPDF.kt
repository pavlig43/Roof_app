package com.pavlig43.roofapp.ui.pdfImage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.kit.SaveDialog
import com.rizzi.bouquet.VerticalPdfReaderState

@Suppress("LongParameterList")
@Composable
fun ImagesFromPDF(
    onBackNavigation: () -> Unit,
    onAdd: () -> Unit,
    openChangeRoofInfo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PDFImageViewModel = hiltViewModel(),
) {
    val isValidName by viewModel.isValidName.collectAsState()
    val fileName by viewModel.saveFileName.collectAsState()
    val pdfReaderState by viewModel.verticalPdfReaderState.collectAsState()

    ImagesFromPDFp(
        pdfReaderState = pdfReaderState,
        onBack = {
            viewModel.removeLastShape()
            onBackNavigation()
        },
        onAdd = onAdd,
        shareFile = viewModel::shareFile,
        saveFile = viewModel::saveFile,
        fileName = fileName,
        onValueChangeName = viewModel::changeName,
        isValidName = isValidName,
        openChangeRoofInfo = {
            viewModel.moveToChangePdfScreen { filePath ->
                openChangeRoofInfo(filePath)
            }
        },
        isConstructor = viewModel.isConstructor,
        modifier = modifier

    )
}

@Suppress("LongParameterList")
@Composable
private fun ImagesFromPDFp(
    pdfReaderState: VerticalPdfReaderState?,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    shareFile: () -> Unit,
    saveFile: () -> Unit,
    fileName: String,
    onValueChangeName: (String) -> Unit,
    isValidName: Boolean,
    openChangeRoofInfo: () -> Unit,
    modifier: Modifier = Modifier,
    isConstructor: Boolean = false,
) {
    var isOpenDialog by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = modifier.fillMaxWidth()) {
            ActionIcons(
                shareFile = shareFile,

                openSaveDialog = { isOpenDialog = true },
                onBack = onBack,
                onAdd = onAdd,
                isConstructor = isConstructor
            )
            if (isOpenDialog) {
                SaveDialog(
                    onDismiss = { isOpenDialog = false },
                    saveFileName = fileName,
                    saveFile = saveFile,
                    onValueChangeName = onValueChangeName,
                    isValid = isValidName,
                )
            }

            pdfReaderState?.let { PDFView(it) }
        }
        FloatButtonForRemoveSheet(
            openChangeRoofInfo = openChangeRoofInfo,
            currentPage = pdfReaderState?.currentPage ?: 0,
            countPage = pdfReaderState?.pdfPageCount ?: 0,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ActionIcons(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    shareFile: () -> Unit,
    openSaveDialog: () -> Unit,
    modifier: Modifier = Modifier,
    isConstructor: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isConstructor) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            IconButton(onClick = onAdd) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }

        IconButton(onClick = shareFile) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.share)
            )
        }
        IconButton(onClick = openSaveDialog) {
            Icon(
                painter = painterResource(R.drawable.ic_save),
                contentDescription = stringResource(R.string.save)
            )
        }
    }
}

@Composable
private fun FloatButtonForRemoveSheet(
    openChangeRoofInfo: () -> Unit,
    currentPage: Int,
    countPage: Int,
    modifier: Modifier = Modifier,

) {
    if (currentPage == countPage) {
        FloatingActionButton(
            onClick = openChangeRoofInfo,
            modifier = modifier
        ) {
            Icon(Icons.Filled.Edit, stringResource(R.string.remove_sheets))
        }
    }
}
