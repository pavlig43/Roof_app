package com.pavlig43.roofapp.ui.saveDocuments

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.LoadDocumentImages
import com.pavlig43.roofapp.ui.pdfImage.PDFView

@Composable
fun ScreensSaveDocuments() {
    ScreensSaveDocumentsp()
}

@Composable
private fun ScreensSaveDocumentsp(viewModel: ListSaveDocumentsViewModel = hiltViewModel()) {
    BackHandler {
        viewModel.returnScreenListDocument()
    }

    val screensSaveDocumentsState by viewModel.screensSaveDocumentsState.collectAsState()
    val listOfDocument by viewModel.listSaveDocument.collectAsState()
    val pdfReaderState by viewModel.pdfReaderState.collectAsState()
    when (screensSaveDocumentsState) {
        is ScreensSaveDocumentsState.ListSaveDocumentsState ->
            ListSaveDocuments(
                listOfDocument = listOfDocument,
                openDocument = { document -> viewModel.openDocument(document) },
                shareFile = viewModel::shareFile,
                deleteFile = viewModel::deleteFile,
            )

        is ScreensSaveDocumentsState.DrawDocumentState -> {
            pdfReaderState?.let { PDFView(it) }
        }
    }
}

@Composable
private fun ListSaveDocuments(
    modifier: Modifier = Modifier,
    listOfDocument: List<Document>,
    openDocument: (Document) -> Unit = {},
    shareFile: (Document) -> Unit = {},
    deleteFile: (Document) -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        items(listOfDocument, key = { doc -> doc.name }) { document ->
            DocumentItemCard(
                document = document,
                openDocument = openDocument,
                shareFile = shareFile,
                deleteFile = deleteFile,
            )
        }
    }
}

@Composable
private fun DocumentItemCard(
    document: Document,
    openDocument: (Document) -> Unit,
    shareFile: (Document) -> Unit,
    deleteFile: (Document) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    if (isShowDeleteDialog) {
        ConfirmDeleteDocDialog(
            deleteFile,
            onDialogDismissed = { isShowDeleteDialog = false },
            document,
        )
    }

    Box(
        modifier =
            modifier.pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > 15) {
                        isShowDeleteDialog = true
                    }
                }
            },
    ) {
        Card(modifier = Modifier.clickable { openDocument(document) }) {
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
                    Text(text = document.name, fontSize = 30.sp)
                }
                Text(text = stringResource(R.string.delete_document_manual))
            }
        }
        IconButton(
            onClick = { shareFile(document) },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
        }
    }
}

@Composable
private fun ConfirmDeleteDocDialog(
    deleteFile: (Document) -> Unit,
    onDialogDismissed: () -> Unit,
    document: Document,
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.delete_this_doc_question)) },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(onClick = {
                deleteFile(document)
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
