package com.pavlig43.roofapp.ui.saveDocuments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import java.io.File

private const val DRAG_AMOUNT_FOR_DELETE = 15

@Composable
fun ScreensSaveDocuments(moveToPdfResult: (String) -> Unit) {
    ScreensSaveDocumentsp(moveToPdfResult)
}

@Composable
private fun ScreensSaveDocumentsp(
    moveToPdfResult: (String) -> Unit,
    viewModel: ListSaveDocumentsViewModel = hiltViewModel()
) {
    val listSaveDocuments by viewModel.listSaveDocuments.collectAsState()

    ListSaveDocuments(
        listOfFile = listSaveDocuments,
        moveToPdfResult = moveToPdfResult,
        shareFile = viewModel::shareFile,
        deleteFile = viewModel::deleteFile,
    )
}

@Composable
private fun ListSaveDocuments(
    listOfFile: List<File>,
    moveToPdfResult: (String) -> Unit,
    shareFile: (File) -> Unit,
    deleteFile: (File) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        items(listOfFile, key = { file ->
            file.name
        }) { file ->
            FileItemCard(
                file = file,
                openFile = moveToPdfResult,
                shareFile = shareFile,
                deleteFile = deleteFile,
            )
        }
    }
}

@Composable
private fun FileItemCard(
    file: File,
    openFile: (String) -> Unit,
    shareFile: (File) -> Unit,
    deleteFile: (File) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    if (isShowDeleteDialog) {
        ConfirmDeleteDocDialog(
            onDialogDismissed = { isShowDeleteDialog = false },
            file,
            deleteFile,
        )
    }

    Box(
        modifier =
        modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                change.consume()
                if (dragAmount > DRAG_AMOUNT_FOR_DELETE) {
                    isShowDeleteDialog = true
                }
            }
        },
    ) {
        Card(modifier = Modifier.clickable { openFile(file.name) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_pdf_96),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(text = file.nameWithoutExtension, fontSize = 30.sp)
                }
                Text(text = stringResource(R.string.delete_document_manual))
            }
        }
        IconButton(
            onClick = { shareFile(file) },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
        }
    }
}

@Composable
private fun ConfirmDeleteDocDialog(
    onDialogDismissed: () -> Unit,
    file: File,
    deleteFile: (File) -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.delete_this_doc_question)) },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(onClick = {
                deleteFile(file)
                onDialogDismissed()
            }) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = { onDialogDismissed() }) {
                Text(text = stringResource(R.string.Cancel))
            }
        },
    )
}
