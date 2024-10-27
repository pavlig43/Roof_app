package com.pavlig43.roofapp.ui.shapes.triangle

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.DotNameTriangle3Side
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.general.ButtonResultRow
import com.pavlig43.roofapp.ui.general.CalculateSheetParams
import com.pavlig43.roofapp.ui.general.ManualDialog
import com.pavlig43.roofapp.ui.paramsDotShape.ChangeParamsDots
import com.pavlig43.roofapp.utils.drawDot
import com.pavlig43.roofapp.utils.drawRuler
import java.io.File

@Composable
fun TriangleChoice(
    modifier: Modifier = Modifier,
    viewModel: TriangleViewModel = hiltViewModel(),
    openDocument: (suspend (Sheet) -> File?) -> Unit,
    sheet: Sheet,
    updateSheetParams: (SheetParam) -> Unit,
) {
    val geometryTriangle3SideShape by viewModel.geometryTriangle3SideShape.collectAsState()
    val currentDot by viewModel.currentDot.collectAsState()
    val isValid by viewModel.isValid.collectAsState()

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
        Canvas(
            modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset: Offset ->
                        when {
                            (topCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                viewModel.changeCurrentDotName(DotNameTriangle3Side.TOP)
                            }

                            (rightBottomCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                viewModel.changeCurrentDotName(DotNameTriangle3Side.RIGHTBOTTOM)
                            }
                        }
                    }
                },
        ) {
            drawRuler(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
            )
            drawDot(
                center = leftBottomCenter,
                dot = geometryTriangle3SideShape.leftBottom,
                downOffset = true,
                startDot = true,
            )
            drawDot(center = topCenter, dot = geometryTriangle3SideShape.top, downOffset = true)
            drawDot(
                center = rightBottomCenter,
                dot = geometryTriangle3SideShape.rightBottom,
                downOffset = false,
            )

            drawLine(
                Color.Black,
                leftBottomCenter,
                topCenter,
                strokeWidth = 5f,
            )
            drawLine(
                Color.Black,
                topCenter,
                rightBottomCenter,
                strokeWidth = 5f,
            )
            drawLine(
                Color.Black,
                rightBottomCenter,
                leftBottomCenter,
                strokeWidth = 5f,
            )
        }
        FloatingActionButton(
            { openManual = !openManual },
            modifier =
            Modifier
                .align(Alignment.TopEnd)
                .size(24.dp),
        ) {
            Icon(Icons.Default.Info, contentDescription = null)
        }

        if (showDotDialog) {
            ChangeParamsDots(
                dot = currentDot,
                changeDot = viewModel::changeParamsDot,
                onDismissRequest = { showDotDialog = false },
            )
        }
        if (isValid) {
            ButtonResultRow(
                modifier = Modifier.align(Alignment.BottomCenter),
                getResult = {
//                    triangleViewModel.createPDFFile(sheet)
                    openDocument(viewModel::createPDFFile)
                },
                openSheetParams = { openSheetParams = !openSheetParams },
            )
        }
        if (openSheetParams) {
            Dialog(onDismissRequest = { openSheetParams = false }) {
                CalculateSheetParams(
                    sheet = sheet,
                    updateSheetParam = updateSheetParams,
                    isDialog = true,
                    closeDialog = { openSheetParams = false },
                    modifier = Modifier.background(Color.White),
                )
            }
        }
        if (openManual) {
            ManualDialog(text = R.string.triangleManual, closeManualDialog = { openManual = false })
        }
    }
}
