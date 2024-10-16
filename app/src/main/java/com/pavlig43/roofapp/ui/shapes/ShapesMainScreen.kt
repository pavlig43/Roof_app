package com.pavlig43.roofapp.ui.shapes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roofapp.ui.ResultImagesFromPDF
import com.pavlig43.roofapp.ui.shapes.quadrilateral.QuadroChoice
import com.pavlig43.roofapp.ui.shapes.triangle.TriangleChoice
import com.pavlig43.roofapp.ui.theme.Roof_appTheme

@Composable
fun ShapesMainUi(viewModel: ShapesViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val shapesScreenState by viewModel.shapesScreenState.collectAsState()
    val sheet by viewModel.sheet.collectAsState()
    val nameFile by viewModel.saveNameFile.collectAsState()
    val pdfReaderState by viewModel.pdfReaderState.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        when (shapesScreenState) {
            is ShapesScreenState.ShapesMain -> ChoiceShape(moveToShape = viewModel::moveToShape)
            is ShapesScreenState.Triangle ->
                TriangleChoice(
                    openDocument = viewModel::openDocument,
                    sheet = sheet,
                    updateSheetParams = viewModel::updateSheetParams,
                )

            is ShapesScreenState.Quadrilateral ->
                QuadroChoice(
                    openDocument = viewModel::openDocument,
                    sheet = sheet,
                    updateSheetParams = viewModel::updateSheetParams,
                )

            is ShapesScreenState.LoadDocumentImage ->
                ResultImagesFromPDF(
                    pdfReaderState = pdfReaderState,
                    returnToCalculateScreen = viewModel::returnCalculateTriangleScreen,
                    shareFile = { viewModel.shareFile() },
                    nameFile = nameFile,
                    saveFile = { viewModel.saveFile(context) },
                    checkSaveName = { newName -> viewModel.checkName(newName, context) },
                )
        }
    }
}

@Composable
fun ChoiceShape(
    modifier: Modifier = Modifier,
    moveToShape: (Shapes) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
    ) {
        Shapes.entries.forEach { shape ->
            CardShape(
                imageShape = shape.imageRes,
                shape = shape,
                moveToShape = moveToShape,
            )
        }
    }
}

@Composable
fun CardShape(
    imageShape: Int,
    shape: Shapes,
    moveToShape: (Shapes) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { moveToShape(shape) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = imageShape),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp),
            )
            Text(text = shape.readableName)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChoiceShapePreview() {
    Roof_appTheme {
        ChoiceShape(moveToShape = { _ -> })
    }
}
