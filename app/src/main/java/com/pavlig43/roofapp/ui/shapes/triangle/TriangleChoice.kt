package com.pavlig43.roofapp.ui.shapes.triangle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import java.io.File

@Composable
fun TriangleChoice(
    modifier: Modifier = Modifier,
    viewModel: TriangleViewModel = hiltViewModel(),
    openDocument: (suspend (Sheet) -> File?) -> Unit,
    sheet: Sheet,
    updateSheetParams: (SheetParam) -> Unit,
) {
//    val geometryTriangle3SideShape by viewModel.geometryTriangle3SideShape.collectAsState()
//    val currentDot by viewModel.currentDot.collectAsState()
//    val isValid by viewModel.isValid.collectAsState()
//
//    val configuration = LocalConfiguration.current
//
//    // Получаем ширину экрана в пикселях
//    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
//
//    // Получаем высоту экрана в пикселях
//    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
//    val leftBottomCenter = PointF((screenWidth * 0.2).toFloat(), (screenHeight * 0.2).toFloat())
//    val topCenter = PointF((screenWidth * 0.6).toFloat(), (screenHeight * 0.45).toFloat())
//    val rightBottomCenter = PointF((screenWidth * 0.2).toFloat(), (screenHeight * 0.7).toFloat())
//
//    var showDotDialog by remember { mutableStateOf(false) }
//    var openSheetParams by remember { mutableStateOf(false) }
//    var openManual by remember { mutableStateOf(false) }
//
//    Box(modifier = modifier.fillMaxSize()) {
//        Canvas(
//            modifier =
//            modifier
//                .fillMaxSize()
//                .pointerInput(Unit) {
//                    detectTapGestures { PointF: PointF ->
//                        when {
//                            (topCenter - PointF).getDistance() <= 45f -> {
//                                showDotDialog = true
//                                viewModel.changeCurrentDotName(DotNameTriangle3Side.TOP)
//                            }
//
//                            (rightBottomCenter - PointF).getDistance() <= 45f -> {
//                                showDotDialog = true
//                                viewModel.changeCurrentDotName(DotNameTriangle3Side.RIGHTBOTTOM)
//                            }
//                        }
//                    }
//                },
//        ) {
//            drawRuler(
//                screenWidth = screenWidth,
//                screenHeight = screenHeight,
//            )
//            drawDot(
//                center = leftBottomCenter,
//                dot = geometryTriangle3SideShape.leftBottom,
//                downPointF = true,
//                startDot = true,
//            )
//            drawDot(center = topCenter, dot = geometryTriangle3SideShape.top, downPointF = true)
//            drawDot(
//                center = rightBottomCenter,
//                dot = geometryTriangle3SideShape.rightBottom,
//                downPointF = false,
//            )
//
//            drawLine(
//                Color.Black,
//                leftBottomCenter,
//                topCenter,
//                strokeWidth = 5f,
//            )
//            drawLine(
//                Color.Black,
//                topCenter,
//                rightBottomCenter,
//                strokeWidth = 5f,
//            )
//            drawLine(
//                Color.Black,
//                rightBottomCenter,
//                leftBottomCenter,
//                strokeWidth = 5f,
//            )
//        }
//        FloatingActionButton(
//            { openManual = !openManual },
//            modifier =
//            Modifier
//                .align(Alignment.TopEnd)
//                .size(24.dp),
//        ) {
//            Icon(Icons.Default.Info, contentDescription = null)
//        }
//
//        if (showDotDialog) {
//            ChangeParamsDots(
//                dot = currentDot,
//                changeDot = viewModel::changeParamsDot,
//                onDismissRequest = { showDotDialog = false },
//            )
//        }
//        if (isValid) {
//            ButtonResultRow(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                getResult = {
////                    triangleViewModel.createPDFFile(sheet)
//                    openDocument(viewModel::createPDFFile)
//                },
//                openSheetParams = { openSheetParams = !openSheetParams },
//            )
//        }
//        if (openSheetParams) {
//            Dialog(onDismissRequest = { openSheetParams = false }) {
//                CalculateSheetParams(
//                    sheet = sheet,
//                    updateSheetParam = updateSheetParams,
//                    isDialog = true,
//                    closeDialog = { openSheetParams = false },
//                    modifier = Modifier.background(Color.White),
//                )
//            }
//        }
//        if (openManual) {
//            ManualDialog(text = R.string.triangleManual, closeManualDialog = { openManual = false })
//        }
//    }
}
