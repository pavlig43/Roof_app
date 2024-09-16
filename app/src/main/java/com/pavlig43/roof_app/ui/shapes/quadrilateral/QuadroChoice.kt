package com.pavlig43.roof_app.ui.shapes.quadrilateral

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.ui.ButtonResultRow
import com.pavlig43.roof_app.ui.CalculateSheetParams
import com.pavlig43.roof_app.ui.ManualDialog
import com.pavlig43.roof_app.ui.params_dot_shape.ChangeParamsDots
import com.pavlig43.roof_app.ui.theme.Roof_appTheme
import com.pavlig43.roof_app.utils.drawDot
import com.pavlig43.roof_app.utils.drawRuler
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuadroChoice(
    quadrilateralViewModel: QuadrilateralViewModel = hiltViewModel(),
    openDocument: (File)->Unit,
    sheet: Sheet,
    updateWidthGeneral:(Float)->Unit,
    updateOverlap:(Float)->Unit,
    updateMultiplicity:(Float)->Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val geometryShape by quadrilateralViewModel.geometryShape.collectAsState()
    val currentDot by quadrilateralViewModel.currentDot.collectAsState()
    val isValid by quadrilateralViewModel.isValid.collectAsState()
    var openManual by remember { mutableStateOf(false) }



    // Получаем ширину экрана в пикселях
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    // Получаем высоту экрана в пикселях
    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val leftBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.2).toFloat())
    val leftTopCenter = Offset((screenWidth * 0.7).toFloat(), (screenHeight * 0.2).toFloat())
    val rightBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.5).toFloat())
    val rightTopCenter = Offset((screenWidth * 0.7).toFloat(), (screenHeight * 0.5).toFloat())



    var showDotDialog by remember { mutableStateOf(false) }
    var openSheetParams by remember{ mutableStateOf(false) }




    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset: Offset ->
                        when {
                            (leftTopCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.LEFTTOP)
                            }

                            (rightTopCenter - offset).getDistance() <= 45f

                            -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.RIGHTTOP)
                            }

                            (rightBottomCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.RIGHTBOTTOM)
                            }
                        }


                    }

                }
        ) {
            drawRuler(
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
            drawDot(
                center = leftBottomCenter,
                dot = geometryShape.leftBottom,
                downOffset = true,
                startDot = true
            )
            drawDot(center = leftTopCenter, dot = geometryShape.leftTop, downOffset = true)
            drawDot(
                center = rightBottomCenter,
                dot = geometryShape.rightBottom,
                downOffset = false
            )
            drawDot(center = rightTopCenter, dot = geometryShape.rightTop, downOffset = false)



            drawLine(
                Color.Black,
                leftBottomCenter,
                leftTopCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                leftBottomCenter,
                rightBottomCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                leftTopCenter,
                rightTopCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                rightBottomCenter,
                rightTopCenter,
                strokeWidth = 5f
            )
        }
        FloatingActionButton(
            { openManual = !openManual },
            modifier = Modifier.align(Alignment.TopEnd).size(24.dp)) {
            Icon(Icons.Default.Info, contentDescription = null)
        }

        if (showDotDialog){
            ChangeParamsDots(
                dot = currentDot,
                changeDot = quadrilateralViewModel::changeParamsDot ,
                onDismissRequest = { showDotDialog=false })
        }
        if (isValid){
        ButtonResultRow(
            modifier = Modifier.align(Alignment.BottomCenter),
            getResult = {openDocument(quadrilateralViewModel.createPDFFile(context, sheet))},
            openSheetParams = {openSheetParams != openSheetParams}
            )}
        if (openSheetParams){
            Dialog(onDismissRequest = {openSheetParams=false}){
                CalculateSheetParams(
                    sheet = sheet,
                    updateWidthGeneral = updateWidthGeneral,
                    updateMultiplicity = updateMultiplicity,
                    updateOverlap = updateOverlap,
                    isDialog = true,
                    closeDialog = {openSheetParams=false},
                    modifier = Modifier.background(Color.White)
                )
            }
        }
        if (openManual) {
            ManualDialog(text = R.string.manual_quadro, closeManualDialog = { openManual = false })
        }
    }


}









@Preview(showBackground = true)
@Composable
private fun ParamsDotRowPreview() {
    Roof_appTheme {
        ChangeParamsDots(
            dot = Dot(DotName4Side.LEFTTOP,canMinusY = true),
            onDismissRequest = {},
            changeDot = {_->Unit}

            )
    }

}




