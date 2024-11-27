package com.pavlig43.roofapp.ui.shapes.random

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.example.pdfcanvasdraw.core.pageKit.implementation.shape.ConstructorShape
import com.example.pdfcanvasdraw.implementationCore.compose.ComposeBuild
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.mappers.coordinateShape.toShapeCanvas
import com.pavlig43.roofapp.ui.kit.CalculateSheetParamsDialog

@Composable
fun RandomShape(
    moveToPdfResult: () -> Unit,
) {
    RandomShapep(moveToPdfResult, hiltViewModel())
}

@Composable
fun RandomShapep(
    moveToPdfResult: () -> Unit,
    viewModel: RandomShapeViewModel,
) {
    val randomShapeState by viewModel.randomShapeState.collectAsState()

    val coordinateShape by viewModel.coordinateShape.collectAsState()
    val sheet by viewModel.sheet.collectAsState()
    val isConvex by viewModel.isConvex.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        ButtonsRow(
            isValidResult = isConvex,
            openManualDialog = viewModel::moveToManualDialog,
            openSheetDialog = viewModel::moveToSheetDialog,
            openAddDialog = viewModel::moveToAddPointDialog,
            getResult = {
                viewModel.getResult()
                moveToPdfResult()
            }
        )
        when (val state = randomShapeState) {
            is RandomShapeState.ConstructorShape ->
                ConstructorRandomShape(
                    coordinateShape,
                    openUpdateDialog = viewModel::moveTOUpdateDialog,
                    provideDotRadius = viewModel::getDotRadius
                )

            is RandomShapeState.ManualDialog ->
                ManualDialog(
                    closeManualDialog = viewModel::moveToConstructor,
                )

            is RandomShapeState.SheetDialog ->
                CalculateSheetParamsDialog(
                    sheet = sheet,
                    updateSheetParam = viewModel::updateSheetParams,
                    onDismissRequest = viewModel::moveToConstructor,
                    modifier = Modifier.rotate(RIGHT_DEGREE),
                )

            is RandomShapeState.AddPointDialog ->
                CRUDPointDialog(
                    onDismissRequest = viewModel::moveToConstructor,
                    updateOrAddPoint = viewModel::addDot,
                    checkOnProximity = viewModel::checkOnProximity,
                    modifier = Modifier.rotate(RIGHT_DEGREE),
                )

            is RandomShapeState.UpdatePointDialog -> {
                val offsetBD = state.offsetBD
                val index = state.index
                CRUDPointDialog(
                    onDismissRequest = viewModel::moveToConstructor,
                    offsetBD = offsetBD,
                    updateOrAddPoint = { offset -> viewModel.updateDot(index, offset) },
                    checkOnProximity = viewModel::checkOnProximity,
                    deletePoint = {
                        viewModel.moveToConstructor()
                        viewModel.deleteDot(index)
                    },
                    modifier = Modifier.rotate(RIGHT_DEGREE),
                )
            }
        }
    }
}

@Composable
private fun ConstructorRandomShape(
    coordinateShape: CoordinateShape,
    openUpdateDialog: (Int) -> Unit,
    provideDotRadius: (Float) -> Unit,
) {
    val factory =
        remember(coordinateShape) {
            {
                    pageConfig: PageConfig ->
                ConstructorShape(
                    coordinateShape.toShapeCanvas(),
                    pageConfig,
                    onClickDot = { (ind, _) -> openUpdateDialog(ind) },
                    provideRadiusDot = provideDotRadius
                )
            }
        }

    ComposeBuild(factory)
}

@Suppress("LongParameterList")
@Composable
private fun ButtonsRow(
    isValidResult: Boolean,
    openManualDialog: () -> Unit = {},
    openSheetDialog: () -> Unit = {},
    openAddDialog: () -> Unit = {},
    getResult: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listOfIconButtons =
        listOf(
            Triple(Icons.Default.CheckCircle, R.string.show_result, getResult),
            Triple(Icons.Default.AddCircle, R.string.add_dot, openAddDialog),
            Triple(Icons.Default.Menu, R.string.open_sheet_params, openSheetDialog),
            Triple(Icons.Default.Info, R.string.info, openManualDialog),
        )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        listOfIconButtons.forEach { (icon, contentDescription, openDialog) ->
            IconButton(
                openDialog,
                enabled = if (icon == Icons.Default.CheckCircle) isValidResult else true,
            ) {
                Icon(
                    icon,
                    contentDescription = stringResource(contentDescription),
                    modifier =
                    Modifier
                        .size(128.dp)
                        .rotate(RIGHT_DEGREE),
                )
            }
        }
    }
}

private const val RIGHT_DEGREE = 90F
