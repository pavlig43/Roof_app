package com.pavlig43.roof_app.ui.shapes.quadrilateral

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.ui.calculation_tile_4scat.ResultTile4ScatPDF

@Composable
fun QuadrilateralUi(
    quadrilateralViewModel: QuadrilateralViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val screenState by quadrilateralViewModel.screenState.collectAsState()
    val listBitmap by quadrilateralViewModel.listBitmap.collectAsState()
    val saveName by quadrilateralViewModel.saveNameFile.collectAsState()
    val context = LocalContext.current
    when (screenState) {
        QuadroScreenState.ChoiceState -> QuadroChoice(
            quadrilateralViewModel,
            modifier = modifier
        )

        QuadroScreenState.PdfResult -> ResultTile4ScatPDF(
            listBitmap = listBitmap,
            returnToCalculateScreen = { quadrilateralViewModel.returnCalculateScreen() },
            shareFile = { quadrilateralViewModel.shareFile(context) },
            nameFile = saveName,
            saveFile = { quadrilateralViewModel.saveFile(context) },
            checkSaveName = {newName:String ->quadrilateralViewModel.checkName(newName, context)}
        )
    }

}