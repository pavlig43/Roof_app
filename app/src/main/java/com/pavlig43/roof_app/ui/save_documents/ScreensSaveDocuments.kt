package com.pavlig43.roof_app.ui.save_documents

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roof_app.ui.LoadDocumentImages

@Composable
fun ScreensSaveDocuments(
    listSaveDocumentsViewModel: ListSaveDocumentsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    BackHandler {
        listSaveDocumentsViewModel.returnScreenListDocument()
    }
    val context = LocalContext.current
    val screensSaveDocumentsState by listSaveDocumentsViewModel.screensSaveDocumentsState.collectAsState()
    val listOfDocument by listSaveDocumentsViewModel.listSaveDocument.collectAsState()
    val listBitmap by listSaveDocumentsViewModel.listBitmap.collectAsState()
    when (screensSaveDocumentsState) {
        is ScreensSaveDocumentsState.ListSaveDocumentsState -> ListSaveDocuments(
            listOfDocument = listOfDocument,
            openDocument = { document -> listSaveDocumentsViewModel.openDocument(document) },
            shareFile = { document -> listSaveDocumentsViewModel.shareFile(context, document) },
            deleteFile = { document -> listSaveDocumentsViewModel.deleteFile(context,document) }
        )

        is ScreensSaveDocumentsState.DrawDocumentState -> {
            LoadDocumentImages(listBitmap = listBitmap)
        }
    }


}

@Composable
fun ListSaveDocuments(
    listOfDocument: List<Document>,
    openDocument: (Document) -> Unit,
    shareFile: (Document) -> Unit,
    deleteFile: (Document) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(listOfDocument, key = { doc -> doc.name }) { document ->
            DocumentItemCard(
                document = document,
                openDocument = openDocument,
                shareFile = shareFile,
                deleteFile = deleteFile

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentItemCard(
    document: Document,
    openDocument: (Document) -> Unit,
    shareFile: (Document) -> Unit,
    deleteFile: (Document) -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }
    if (isShowDeleteDialog) {
        AlertDialog(
            title = { Text(text = "Удалить этот документ???") },
            onDismissRequest = { isShowDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    deleteFile(document)
                    isShowDeleteDialog = false
                }) {
                    Text(text = "Удалить")
                }
            },
            dismissButton = {
                Button(onClick = { isShowDeleteDialog = false }) {
                    Text(text = "Отмена")
                }
            })
    }

    Box(modifier = modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            change.consume()
            if (dragAmount > 15) {
                isShowDeleteDialog = true
            }
        }
    }) {
        Card(modifier = Modifier.clickable { openDocument(document) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_pdf_96),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(text = document.name, fontSize = 30.sp)
                }
                Text(text = "Для удаления смахни направо документ")
            }
        }
        IconButton(
            onClick = { shareFile(document) },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
        }

    }
}