package com.pavlig43.roofapp.ui.calculationTile4scat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.CalculateSheetParams
import com.pavlig43.roofapp.ui.ResultImagesFromPDF
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal

@Composable
fun CalculationTile4ScatMainScreen() {
    CalculationTile4ScatMainScreenp()
}

@Composable
private fun CalculationTile4ScatMainScreenp(viewModel: CalculationTile4ScatViewModel = hiltViewModel()) {
    val stateNavigation by viewModel.stateNavigation.collectAsState()

    val paramsState by viewModel.roofState.collectAsState()
    val listBitmap by viewModel.listBitmap.collectAsState()

    val pdfReaderState by viewModel.pdfReaderState.collectAsState()
    val nameFile by viewModel.saveNameFile.collectAsState()

    val isValid by viewModel.isValid.collectAsState()

    when (stateNavigation) {
        is StateCalculationTile4Scat.ChangeCalculation ->

            CalculationTile4Scat(
                paramsState,
                changeWidth = viewModel::changeWidth,
                changeLen = viewModel::changeLen,
                updateFromHeight = viewModel::updateFromHeight,
                updateFromAngle = viewModel::updateFromAngle,
                updateFromHypotenuse = viewModel::updateFromHypotenuse,
                getResult = viewModel::getResult,
                updateSheetParam = viewModel::updateSheetParams,
                isValid = isValid,
            )


        is StateCalculationTile4Scat.GetDraw ->
            ResultImagesFromPDF(

                pdfReaderState = pdfReaderState,
                returnToCalculateScreen = viewModel::returnToCalculateScreen,
                shareFile = viewModel::shareFile,
                saveFile = viewModel::saveFile,
                nameFile = nameFile,
                checkSaveName = viewModel::checkName,
            )
    }
}

/**
 * Экран для выбора параметров 4хскатной крыши
 */
@Composable
private fun CalculationTile4Scat(
    paramsState: RoofParamsClassic4Scat,
    changeWidth: (BigDecimal) -> Unit,
    changeLen: (BigDecimal) -> Unit,
    updateFromHeight: (BigDecimal) -> Unit,
    updateFromAngle: (BigDecimal) -> Unit,
    updateFromHypotenuse: (BigDecimal) -> Unit,
    updateSheetParam: (SheetParam)->Unit,
    getResult: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CalculateItemTextField(
            nameField = stringResource(R.string.width_roof),
            value = paramsState.width,
            onValueChange = changeWidth,
        )
        CalculateItemTextField(
            nameField = stringResource(R.string.len_roof),
            value = paramsState.len,
            onValueChange = changeLen,
        )
        CalculateItemDropMenu(
            paramsState = paramsState,
            updateFromHeight = updateFromHeight,
            updateFromAngle = updateFromAngle,
            updateFromHypotenuse = updateFromHypotenuse,
        )
        if (!isValid) {
            Text(
                text = "* Проверь данные",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.error,
            )
        }
        OtherParamsColumn(paramsState = paramsState, modifier = Modifier.padding(top = 12.dp))
        Log.d("widthGeneral,", paramsState.sheet.widthGeneral.toString())

        CalculateSheetParams(
            sheet = paramsState.sheet,
            updateSheetParam = updateSheetParam
        )

        Button(
            onClick = getResult,
            enabled = isValid,
        ) {
            Text(text = "Получить результат")
        }
    }
}

/**
 * Row для установления значений крыши
 */
@Composable
private fun CalculateItemTextField(
    nameField: String,
    value: BigDecimal,
    onValueChange: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$nameField(cм)", modifier.fillMaxWidth(0.4f))
        TextField(
            value = value.takeIf { it != BigDecimal.ZERO }?.toPlainString() ?: "",
            onValueChange = { onValueChange(it.toBigDecimalOrNull() ?: BigDecimal.ZERO) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}

/**
 * Выпадающее меню в котором можно выбрать покат,угол наклона и высоту крыши дял установки значения
 */
@Composable
private fun CalculateItemDropMenu(
    paramsState: RoofParamsClassic4Scat,
    updateFromHypotenuse: (BigDecimal) -> Unit,
    updateFromHeight: (BigDecimal) -> Unit,
    updateFromAngle: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(UnitOfMeasurement.POKAT) }

    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(0.4f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val unit = if (selectedOption == UnitOfMeasurement.ANGLE) stringResource(R.string.angle) else stringResource(R.string.cm)
            Text(text = "${selectedOption.title}\n($unit)")
            IconButton(
                onClick = {
                    expanded = !expanded
                },
            ) {
                Icon(
                    imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                )
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            UnitOfMeasurement.entries.forEach {
                DropItemUnitOfMeasurement(it) { selected ->
                    selectedOption = selected
                    expanded = false
                }
            }
        }
        when (selectedOption) {
            UnitOfMeasurement.POKAT -> DropItemRoofParam(paramsState.hypotenuse, updateFromHypotenuse)

            UnitOfMeasurement.HEIGHTROOF -> DropItemRoofParam(paramsState.height, updateFromHeight)

            UnitOfMeasurement.ANGLE -> DropItemRoofParam(paramsState.angle, updateFromAngle)
        }
    }
}

@Composable
private fun DropItemUnitOfMeasurement(
    unitOfMeasurement: UnitOfMeasurement,
    onClick: (UnitOfMeasurement) -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(unitOfMeasurement.title)) },
        onClick = { onClick(unitOfMeasurement) },
    )
}

/**
 * Поле для установки значения на выбор
 */
@Composable
private fun DropItemRoofParam(
    value: BigDecimal,
    updateRoof: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value.takeIf { it != BigDecimal.ZERO }?.toPlainString() ?: "",
        onValueChange = { updateRoof(it.toBigDecimalOrNull() ?: BigDecimal.ZERO) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}

/**
 * Колонка с дополнительными расчетными параметрами для информации
 */
@Composable
private fun OtherParamsColumn(
    paramsState: RoofParamsClassic4Scat,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OtherParamsRow(title = stringResource(R.string.yandova), value = paramsState.yandova)
        OtherParamsRow(title = stringResource(R.string.pokat), value = paramsState.hypotenuse)
        OtherParamsRow(title = stringResource(R.string.angle_tilt), value = paramsState.angle)
        OtherParamsRow(title = stringResource(R.string.height_roof), value = paramsState.height)
    }
}

@Composable
private fun OtherParamsRow(
    title: String,
    value: BigDecimal,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text(value.toString())
    }
}

private enum class UnitOfMeasurement(val title: Int) {
    ANGLE(R.string.angle_tilt),
    POKAT(R.string.pokat),
    HEIGHTROOF(R.string.height_roof),
}

@Preview(showBackground = true)
@Composable
private fun CalculationTile4ScatScreenPreview() {
    Roof_appTheme {
        CalculationTile4Scat(
            paramsState = RoofParamsClassic4Scat(),
            changeWidth = { _ -> },
            changeLen = { _ -> },
            updateFromHeight = { _ -> },
            updateFromAngle = { _ -> },
            updateFromHypotenuse = { _ -> },
            getResult = {},
            updateSheetParam = { _ -> },
            isValid = true,
        )
    }
}
