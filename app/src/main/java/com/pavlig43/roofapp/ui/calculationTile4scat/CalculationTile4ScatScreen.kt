package com.pavlig43.roofapp.ui.calculationTile4scat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.pavlig43.roofapp.model.RoofParam
import com.pavlig43.roofapp.model.RoofParamName
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.kit.ArrowIconButton
import com.pavlig43.roofapp.ui.kit.CalculateSheetParams
import com.pavlig43.roofapp.ui.kit.OtherParamsColumn
import com.pavlig43.roofapp.ui.kit.rowParam.ParamRow
import com.pavlig43.roofapp.ui.kit.rowParam.TextFieldBigDecimal
import com.pavlig43.roofapp.ui.kit.rowParam.TextParam
import com.pavlig43.roofapp.ui.theme.Roof_appTheme

@Composable
fun CalculationTile4ScatMainScreen(moveToPdfResult: () -> Unit) {
    CalculationTile4ScatMainScreenp(moveToPdfResult, hiltViewModel())
}

@Composable
private fun CalculationTile4ScatMainScreenp(
    moveToPdfResult: () -> Unit,
    viewModel: CalculationTile4ScatViewModel,
) {
    val paramsState by viewModel.roofState.collectAsState()
    val sheet by viewModel.sheet.collectAsState()
    val isValid by viewModel.isValid.collectAsState()
    val selectedOption by viewModel.selectedOptionDropMenu.collectAsState()

    CalculationTile4Scat(
        paramsState,
        sheet,
        getResult = {
            viewModel.getResult()
            moveToPdfResult()
        },
        updateSheetParam = viewModel::updateSheetParams,
        isValid = isValid,
        updateRoofParam = viewModel::updateRoofParams,
        selectedOption = selectedOption,
        changeSelectedOption = viewModel::changeSelectedOption,
    )
}

/**
 * Экран для выбора параметров 4хскатной крыши
 */
@Suppress("LongParameterList")
@Composable
private fun CalculationTile4Scat(
    paramsState: RoofParamsClassic4Scat,
    sheet: Sheet,
    updateRoofParam: (RoofParam) -> Unit,
    updateSheetParam: (SheetParam) -> Unit,
    getResult: () -> Unit,
    isValid: Boolean,
    selectedOption: RoofParam,
    changeSelectedOption: (RoofParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        arrayOf(paramsState.width, paramsState.len).forEach { roofParam ->
            ParamRow(
                modifier = modifier,
                paramTitle = roofParam.name.title,
                unit = roofParam.unit.title,
                value = roofParam.value,
                updateParam = { newValue -> updateRoofParam(roofParam.copy(value = newValue)) },
            )
        }
        RoofParamDropMenu(
            paramsState = paramsState,
            updateRoofParam = updateRoofParam,
            selectedOption = selectedOption,
            changeSelectedOption = changeSelectedOption,
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

/**
 * Выпадающее меню в котором можно выбрать покат,угол наклона и высоту крыши дял установки значения
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
                changeExpanded = { expanded = !expanded },
                expanded = expanded,
            )
        }
        val onDismissRequest = { expanded = false }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            arrayOf(paramsState.pokat, paramsState.angle, paramsState.height).forEach { roofParam ->
                RoofParamDropMenuRow(
                    roofParam = roofParam,
                    toSelectParam = changeSelectedOption,
                    onDismissRequest = onDismissRequest,
                )
            }
        }
        TextFieldBigDecimal(
            value = selectedOption.value,
            updateParam = { newValue ->
                Log.d("newvalue", newValue.toPlainString())
                updateRoofParam(selectedOption.copy(value = newValue))
                Log.d("newvalueparam", selectedOption.copy(value = newValue).toString())
            },
        )
    }
}

@Composable
fun TextRoofParam(
    roofParam: RoofParam,
    modifier: Modifier = Modifier,
) {
    TextParam(
        paramTitle = roofParam.name.title,
        unit = roofParam.unit.title,
        modifier = modifier,
    )
}

@Composable
fun RoofParamDropMenuRow(
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

/**
 * Колонка с дополнительными расчетными параметрами для информации
 */
@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CalculationTile4ScatScreenPreview() {
    Roof_appTheme {
        CalculationTile4Scat(
            paramsState = RoofParamsClassic4Scat(),
            sheet = Sheet(),
            updateRoofParam = { _ -> },
            getResult = {},
            updateSheetParam = { _ -> },
            isValid = true,
            changeSelectedOption = { _ -> },
            selectedOption = RoofParam(RoofParamName.POKAT),
        )
    }
}
