package com.pavlig43.roofapp.ui.calculationTile4scat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.WIDTH_COLUMN_PERCENT
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParam
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamName
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofType
import com.pavlig43.roofapp.ui.kit.ArrowIconButton
import com.pavlig43.roofapp.ui.kit.CalculateSheetParams
import com.pavlig43.roofapp.ui.kit.rowParam.ParamRow
import com.pavlig43.roofapp.ui.kit.rowParam.TextFieldBigDecimal
import com.pavlig43.roofapp.ui.kit.rowParam.TextParam
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CalculationTile4ScatMainScreen(moveToPdfResult: (String) -> Unit) {
    CalculationTile4ScatMainScreenp(moveToPdfResult, hiltViewModel())
}

@Composable
private fun CalculationTile4ScatMainScreenp(
    moveToPdfResult: (String) -> Unit,
    viewModel: CalculationTile4ScatViewModel,
) {
    val paramsState by viewModel.roofState.collectAsState()
    val sheet by viewModel.sheet.collectAsState()
    val isValid by viewModel.isValid.collectAsState()
    val selectedOption by viewModel.selectedOptionDropMenu.collectAsState()

    CalculationTile4Scat(
        paramsState = paramsState,
        updateRoofParam = viewModel::updateRoofParams,
        sheet = sheet,
        updateSheetParam = viewModel::updateSheetParams,
        selectedOption = selectedOption,
        changeSelectedOption = viewModel::changeSelectedOption,
        calculateFromRoofType = viewModel::calculateFromRoofType,
        isValid = isValid,
        getResult = {
            viewModel.getResult { filePath ->
                moveToPdfResult(filePath)
            }
        },
    )
}

/**
 * Экран для выбора параметров 4хскатной крыши
 */
@Suppress("LongParameterList")
@Composable
private fun CalculationTile4Scat(
    paramsState: RoofParamsClassic4Scat,
    updateRoofParam: (RoofParam) -> Unit,
    sheet: Sheet,
    updateSheetParam: (SheetParam) -> Unit,
    selectedOption: RoofParam,
    changeSelectedOption: (RoofParam) -> Unit,
    calculateFromRoofType: (RoofType) -> Unit,
    isValid: Boolean,
    getResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(verticalScrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        arrayOf(paramsState.width, paramsState.len).forEach { roofParam ->
            ParamRow(
                paramTitle = roofParam.name.title,
                value = roofParam.value,
                updateParam = { newValue -> updateRoofParam(roofParam.copy(value = newValue)) },
                modifier = modifier,
                unit = roofParam.unit.title,
            )
        }
        RoofTypeMenu(
            roofType = paramsState.roofType,
            calculateFromRoofType = calculateFromRoofType
        )
        if (paramsState.roofType == RoofType.None) {
            RoofParamDropMenu(
                paramsState = paramsState,
                updateRoofParam = updateRoofParam,
                selectedOption = selectedOption,
                changeSelectedOption = changeSelectedOption,
            )
        }
        UserRidgeParamRow(
            userRidge = paramsState.userRidge,
            updateUserRidge = { newValue -> updateRoofParam(newValue) },
        )

        if (!isValid) {
            Text(
                text = "* Проверь данные",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.error,
            )
        }
        OtherParamsColumn(paramsState = paramsState, modifier = Modifier.padding(top = 12.dp))

        CalculateSheetParams(
            sheet = sheet,
            updateSheetParam = updateSheetParam,
        )

        Button(
            onClick = getResult,
            enabled = isValid,
        ) {
            Text(stringResource(R.string.get_result))
        }
    }
}

@Composable
private fun UserRidgeParamRow(
    userRidge: RoofParam?,
    updateUserRidge: (RoofParam) -> Unit,
    modifier: Modifier = Modifier
) {
    ParamRow(
        paramTitle = RoofParamName.USER_RIDGE.title,
        value = userRidge?.value ?: BigDecimal.ZERO,
        updateParam = { newValue: BigDecimal ->
            val userRidge = RoofParam(
                RoofParamName.USER_RIDGE,
                newValue
            )
            updateUserRidge(userRidge)

        },
        modifier = modifier,
        unit = userRidge?.unit?.title,
    )
}

@Composable
private fun RoofTypeMenu(
    roofType: RoofType,
    calculateFromRoofType: (RoofType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.roof_type) +
                    " (${stringResource(R.string.angle_tilt)})",
            modifier = Modifier.fillMaxWidth(
                WIDTH_COLUMN_PERCENT
            )
        )
        Text(stringResource(roofType.title))
        ArrowIconButton(
            expanded = expanded,
            changeExpanded = { expanded = !expanded }
        )
        Column {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                RoofType.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(it.title) +
                                        " (${it.angle ?: ""})"
                            )
                        },
                        onClick = {
                            calculateFromRoofType(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Выпадающее меню в котором можно выбрать покат,угол наклона,тип и высоту крыши дял установки значения
 */
@Composable
private fun RoofParamDropMenu(
    paramsState: RoofParamsClassic4Scat,
    updateRoofParam: (RoofParam) -> Unit,
    selectedOption: RoofParam,
    changeSelectedOption: (RoofParam) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(WIDTH_COLUMN_PERCENT),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextRoofParam(
                selectedOption,
            )
            ArrowIconButton(
                expanded = expanded,
                changeExpanded = { expanded = !expanded },
            )
        }
        val onDismissRequest = { expanded = false }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            arrayOf(
                paramsState.pokatTrapezoid,
                paramsState.angle,
                paramsState.height
            ).forEach { roofParam ->
                RoofParamDropMenuItem(
                    roofParam = roofParam,
                    toSelectParam = changeSelectedOption,
                    onDismissRequest = onDismissRequest,
                )
            }
        }
        TextFieldBigDecimal(
            value = selectedOption.value.setScale(0, RoundingMode.HALF_UP),
            updateParam = { newValue ->
                updateRoofParam(selectedOption.copy(value = newValue))
            },
        )
    }
}

@Composable
private fun TextRoofParam(
    roofParam: RoofParam,
    modifier: Modifier = Modifier,
) {
    TextParam(
        paramTitle = roofParam.name.title,
        modifier = modifier,
        unit = roofParam.unit.title,
    )
}

@Composable
fun RoofParamDropMenuItem(
    roofParam: RoofParam,
    onDismissRequest: () -> Unit,
    toSelectParam: (RoofParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Row(modifier = modifier.fillMaxWidth()) {
                TextRoofParam(roofParam)
            }
        },
        onClick = {
            toSelectParam(roofParam)
            onDismissRequest()
        },
    )
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CalculationTile4ScatScreenPreview() {
    Roof_appTheme {
        CalculationTile4Scat(
            paramsState = RoofParamsClassic4Scat(),
            updateRoofParam = { _ -> },
            sheet = Sheet(),
            updateSheetParam = { _ -> },
            selectedOption = RoofParam(RoofParamName.POKAT_TRAPEZOID),
            changeSelectedOption = { _ -> },
            calculateFromRoofType = {},
            isValid = true,
            getResult = {},
        )
    }
}
