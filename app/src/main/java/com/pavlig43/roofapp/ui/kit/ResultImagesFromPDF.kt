package com.pavlig43.roofapp.ui.kit

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pavlig43.roofapp.ui.calculationTile4scat.SaveNameFile
import com.pavlig43.roofapp.ui.pdfImage.PDFView
import com.rizzi.bouquet.VerticalPdfReaderState

@Suppress("LongParameterList")
@Composable
fun ResultImagesFromPDF(
    pdfReaderState: VerticalPdfReaderState? = null,
    returnToCalculateScreen: () -> Unit,
    shareFile: () -> Unit,
    nameFile: SaveNameFile,
    saveFile: () -> Unit,
    checkSaveName: (String) -> Unit,
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
                saveNameFile = nameFile.name,
                saveFile = saveFile,
                checkSaveName = checkSaveName,
                onDismiss = { openDialog = false },
                isValid = nameFile.isValid,
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
                run { checkSaveName(nameFile.name) }
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }

        if (pdfReaderState != null) {
            PDFView(pdfReaderState)
        }
    }
}
