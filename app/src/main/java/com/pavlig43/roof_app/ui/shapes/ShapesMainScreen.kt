package com.pavlig43.roof_app.ui.shapes

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
import com.pavlig43.roof_app.R
import com.pavlig43.roof_app.ui.ResultImagesFromPDF
import com.pavlig43.roof_app.ui.shapes.quadrilateral.QuadroChoice
import com.pavlig43.roof_app.ui.shapes.triangle.TriangleChoice
import com.pavlig43.roof_app.ui.theme.Roof_appTheme

@Composable
fun ShapesMainUi(
    shapesViewModel: ShapesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shapesScreenState by shapesViewModel.shapesScreenState.collectAsState()
    val sheet by shapesViewModel.sheet.collectAsState()
    val listBitmap by shapesViewModel.listBitmap.collectAsState()
    val nameFile by shapesViewModel.saveNameFile.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        when (shapesScreenState) {
            is ShapesScreenState.ShapesMain -> ChoiceShape(moveToShape = shapesViewModel::moveToShape)
            is ShapesScreenState.Triangle -> TriangleChoice (
                openDocument = shapesViewModel::openDocument,
                sheet = sheet,
                updateMultiplicity = shapesViewModel::changeMultiplicity,
                updateOverlap = shapesViewModel::changeOverlap,
                updateWidthGeneral = shapesViewModel::changeWidthOfSheet

            )
            is ShapesScreenState.Quadrilateral -> QuadroChoice(
                openDocument = shapesViewModel::openDocument,
                sheet = sheet,
                updateMultiplicity = shapesViewModel::changeMultiplicity,
                updateOverlap = shapesViewModel::changeOverlap,
                updateWidthGeneral = shapesViewModel::changeWidthOfSheet
                )

            is ShapesScreenState.LoadDocumentImage -> ResultImagesFromPDF(
                listBitmap = listBitmap,
                returnToCalculateScreen = shapesViewModel::returnCalculateTriangleScreen,
                shareFile = {shapesViewModel.shareFile(context)},
                nameFile = nameFile,
                saveFile = {shapesViewModel.saveFile(context)},
                checkSaveName = {newName->shapesViewModel.checkName(newName, context)}
            )


        }
    }

}

@Composable
fun ChoiceShape(
    modifier: Modifier = Modifier,
    moveToShape: (String) -> Unit,


    ) {
    Column(
        modifier = modifier

            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        CardShape(
            imageShape = R.drawable.triangle,
            shapeName = "Треугольник",
            moveToShape = moveToShape
        )
        CardShape(
            imageShape = R.drawable.quadro,
            shapeName = "4-угольник",
            moveToShape = moveToShape
        )
    }
}

@Composable
fun CardShape(
    imageShape: Int,
    shapeName: String,
    moveToShape: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .clickable { moveToShape(shapeName)
            }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = imageShape),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp)
            )
            Text(text = shapeName)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChoiceShapePreview() {
    Roof_appTheme {
        ChoiceShape(moveToShape = { _ -> Unit })
    }
}

