package com.pavlig43.roofapp.ui.shapes.quadrilateral

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.Dot
import com.pavlig43.roofapp.model.DotName4Side
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.kit.ButtonResultRow
import com.pavlig43.roofapp.ui.kit.CalculateSheetParams
import com.pavlig43.roofapp.ui.kit.ManualDialog
import com.pavlig43.roofapp.ui.paramsDotShape.ChangeParamsDots
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import com.pavlig43.roofapp.utils.drawDot
import com.pavlig43.roofapp.utils.drawRuler
import java.io.File

@Composable
fun QuadroChoice(
    modifier: Modifier = Modifier,
    viewModel: QuadrilateralViewModel = hiltViewModel(),
    openDocument: (suspend (Sheet) -> File?) -> Unit,
    updateSheetParams: (SheetParam) -> Unit,
    sheet: Sheet,
) {
    val configuration = LocalConfiguration.current

    val geometryShape by viewModel.geometry4SideShape.collectAsState()
    val currentDot by viewModel.currentDot.collectAsState()
    val isValid by viewModel.isValid.collectAsState()
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
    var openSheetParams by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { pointF: Offset ->

                        when {
                            (leftTopCenter - pointF).getDistance() <= 45f -> {
                                showDotDialog = true
                                viewModel.changeCurrentDotName(DotName4Side.LEFTTOP)
                            }

                            (rightTopCenter - pointF).getDistance() <= 45f

                                -> {
                                showDotDialog = true
                                viewModel.changeCurrentDotName(DotName4Side.RIGHTTOP)
                            }

                            (rightBottomCenter - pointF).getDistance() <= 45f -> {
                                showDotDialog = true
                                viewModel.changeCurrentDotName(DotName4Side.RIGHTBOTTOM)
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
                dot = geometryShape.leftBottom,
                downPointF = true,
                startDot = true,
            )
            drawDot(center = leftTopCenter, dot = geometryShape.leftTop, downPointF = true)
            drawDot(
                center = rightBottomCenter,
                dot = geometryShape.rightBottom,
                downPointF = false,
            )
            drawDot(center = rightTopCenter, dot = geometryShape.rightTop, downPointF = false)

            drawLine(
                Color.Black,
                leftBottomCenter,
                leftTopCenter,
                strokeWidth = 5f,
            )
            drawLine(
                Color.Black,
                leftBottomCenter,
                rightBottomCenter,
                strokeWidth = 5f,
            )
            drawLine(
                Color.Black,
                leftTopCenter,
                rightTopCenter,
                strokeWidth = 5f,
            )
            drawLine(
                Color.Black,
                rightBottomCenter,
                rightTopCenter,
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
                    openDocument(viewModel::createPDFFile)
                },
                openSheetParams = { openSheetParams != openSheetParams },
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
            ManualDialog(text = R.string.manual_quadro, closeManualDialog = { openManual = false })
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun ParamsDotRowPreview() {
    Roof_appTheme {
        ChangeParamsDots(
            dot = Dot(DotName4Side.LEFTTOP, canMinusY = true),
            onDismissRequest = {},
            changeDot = { _ -> },
        )
    }
}
