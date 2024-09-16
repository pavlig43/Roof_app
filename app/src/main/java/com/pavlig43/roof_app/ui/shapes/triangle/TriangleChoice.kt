package com.pavlig43.roof_app.ui.shapes.triangle

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.model.DotNameTriangle3Side
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.ui.ButtonResultRow
import com.pavlig43.roof_app.ui.CalculateSheetParams
import com.pavlig43.roof_app.ui.ManualDialog
import com.pavlig43.roof_app.ui.params_dot_shape.ChangeParamsDots
import com.pavlig43.roof_app.ui.shapes.quadrilateral.Geometry4SideShape
import com.pavlig43.roof_app.utils.drawDot
import com.pavlig43.roof_app.utils.drawRuler
import java.io.File

@Composable
fun TriangleChoice(
    triangleViewModel: TriangleViewModel = hiltViewModel(),
    openDocument: (File) -> Unit,
    sheet: Sheet,
    updateWidthGeneral: (Float) -> Unit,
    updateOverlap: (Float) -> Unit,
    updateMultiplicity: (Float) -> Unit,
    modifier: Modifier = Modifier,


    ) {
    val geometryTriangle3SideShape by triangleViewModel.geometryTriangle3SideShape.collectAsState()
    val currentDot by triangleViewModel.currentDot.collectAsState()
    val isValid by triangleViewModel.isValid.collectAsState()


    val context = LocalContext.current
    val configuration = LocalConfiguration.current


    // Получаем ширину экрана в пикселях
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    // Получаем высоту экрана в пикселях
    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val leftBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.2).toFloat())
    val topCenter = Offset((screenWidth * 0.6).toFloat(), (screenHeight * 0.45).toFloat())
    val rightBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.7).toFloat())

    var showDotDialog by remember { mutableStateOf(false) }
    var openSheetParams by remember { mutableStateOf(false) }
    var openManual by remember { mutableStateOf(false) }




    Box(modifier = modifier.fillMaxSize()) {


        Canvas(modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset: Offset ->
                    when {
                        (topCenter - offset).getDistance() <= 45f -> {
                            showDotDialog = true
                            triangleViewModel.changeCurrentDotName(DotNameTriangle3Side.TOP)
                        }

                        (rightBottomCenter - offset).getDistance() <= 45f -> {
                            showDotDialog = true
                            triangleViewModel.changeCurrentDotName(DotNameTriangle3Side.RIGHTBOTTOM)
                        }
                    }


                }

            }) {
            drawRuler(
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
            drawDot(
                center = leftBottomCenter,
                dot = geometryTriangle3SideShape.leftBottom,
                downOffset = true,
                startDot = true
            )
            drawDot(center = topCenter, dot = geometryTriangle3SideShape.top, downOffset = true)
            drawDot(
                center = rightBottomCenter,
                dot = geometryTriangle3SideShape.rightBottom,
                downOffset = false
            )

            drawLine(
                Color.Black, leftBottomCenter, topCenter, strokeWidth = 5f
            )
            drawLine(
                Color.Black, topCenter, rightBottomCenter, strokeWidth = 5f
            )
            drawLine(
                Color.Black, rightBottomCenter, leftBottomCenter, strokeWidth = 5f
            )
        }
        FloatingActionButton(
            { openManual = !openManual },
            modifier = Modifier.align(Alignment.TopEnd).size(24.dp)) {
            Icon(Icons.Default.Info, contentDescription = null)
        }

        if (showDotDialog) {
            ChangeParamsDots(
                dot = currentDot,
                changeDot = triangleViewModel::changeParamsDot,
                onDismissRequest = { showDotDialog = false })
        }
        if (isValid) {
            ButtonResultRow(
                modifier = Modifier.align(Alignment.BottomCenter),
                getResult = { openDocument(triangleViewModel.createPDFFile(context, sheet)) },
                openSheetParams = { openSheetParams = !openSheetParams }
            )
        }
        if (openSheetParams) {
            Dialog(onDismissRequest = { openSheetParams = false }) {
                CalculateSheetParams(
                    sheet = sheet,
                    updateWidthGeneral = updateWidthGeneral,
                    updateMultiplicity = updateMultiplicity,
                    updateOverlap = updateOverlap,
                    isDialog = true,
                    closeDialog = { openSheetParams = false },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
        if (openManual) {
            ManualDialog(text = R.string.triangleManual, closeManualDialog = { openManual = false })
        }

    }


}