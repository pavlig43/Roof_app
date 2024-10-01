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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.ui.CalculateSheetParams
import com.pavlig43.roofapp.ui.ResultImagesFromPDF
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal

@Composable
fun CalculationTile4ScatMainScreen(
    calculationTile4ScatViewModel: CalculationTile4ScatViewModel = hiltViewModel(),

    ) {
    val stateNavigation by calculationTile4ScatViewModel.stateNavigation.collectAsState()

    val paramsState by calculationTile4ScatViewModel.roofState.collectAsState()
    val listBitmap by calculationTile4ScatViewModel.listBitmap.collectAsState()
    val nameFile by calculationTile4ScatViewModel.saveNameFile.collectAsState()
    val context = LocalContext.current

    val isValid by calculationTile4ScatViewModel.isValid.collectAsState()
    when (stateNavigation) {
        is StateCalculationTile4Scat.ChangeCalculation ->
            CalculationTile4Scat(
                paramsState,
                changeWidth = calculationTile4ScatViewModel::changeWidth,
                changeLen = calculationTile4ScatViewModel::changeLen,
                updateFromHeight = calculationTile4ScatViewModel::updateFromHeight,
                updateFromAngle = calculationTile4ScatViewModel::updateFromAngle,
                updateFromHypotenuse = calculationTile4ScatViewModel::updateFromHypotenuse,
                changeOverlapOfSheet = calculationTile4ScatViewModel::changeOverlap,
                changeWidthOfSheet = calculationTile4ScatViewModel::changeWidthOfSheet,
                changeMultiplicity = calculationTile4ScatViewModel::changeMultiplicity,
                getResult = { calculationTile4ScatViewModel.getResult(context) },
                isValid = isValid,
            )

        is StateCalculationTile4Scat.GetDraw ->
            ResultImagesFromPDF(
                listBitmap = listBitmap,
                returnToCalculateScreen = { calculationTile4ScatViewModel.returnToCalculateScreen() },
                shareFile = { calculationTile4ScatViewModel.shareFile(context) },
                saveFile = { calculationTile4ScatViewModel.saveFile(context) },
                nameFile = nameFile,
                checkSaveName = { newName ->
                    calculationTile4ScatViewModel.checkName(
                        newName,
                        context,
                    )
                },
            )
    }
}

/**
 * Экран для выбора параметров 4хскатной крыши
 */
@Composable
fun CalculationTile4Scat(
    paramsState: RoofParamsClassic4Scat,
    changeWidth: (BigDecimal) -> Unit,
    changeLen: (BigDecimal) -> Unit,
    updateFromHeight: (BigDecimal) -> Unit,
    updateFromAngle: (BigDecimal) -> Unit,
    updateFromHypotenuse: (BigDecimal) -> Unit,
    changeWidthOfSheet: (Float) -> Unit,
    changeOverlapOfSheet: (Float) -> Unit,
    changeMultiplicity: (Float) -> Unit,
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
            nameField = "Ширина крыши",
            value = paramsState.width,
            onValueChange = changeWidth,
        )
        CalculateItemTextField(
            nameField = "Длина крыши",
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
        Log.d("widthGeneralM,", (paramsState.sheet.widthGeneral * 100).toInt().toString())
        CalculateSheetParams(
            sheet = paramsState.sheet,
            updateOverlap = changeOverlapOfSheet,
            updateMultiplicity = changeMultiplicity,
            updateWidthGeneral = changeWidthOfSheet,
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
fun CalculateItemTextField(
    nameField: String,
    value: BigDecimal,
    onValueChange: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
//    value = if (value == 0f) "" else value.toInt().toString(),
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$nameField(cм)", modifier.fillMaxWidth(0.4f))
        TextField(
            value =  value.toString(),
            onValueChange = { onValueChange(it.toBigDecimal()) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}

/**
 * Выпадающее меню в котором можно выбрать покат,угол наклона и высоту крыши дял установки значения
 */
@Composable
fun CalculateItemDropMenu(
    paramsState: RoofParamsClassic4Scat,
    updateFromHypotenuse: (BigDecimal) -> Unit,
    updateFromHeight: (BigDecimal) -> Unit,
    updateFromAngle: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Покат") }

    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(0.4f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val unit = if (selectedOption == "Угол наклона") "градус" else "cm"
            Text(text = "$selectedOption\n($unit)")
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
            DropdownMenuItem(
                text = { Text(text = "Покат") },
                onClick = {
                    selectedOption = "Покат"
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(text = "Высота крыши") },
                onClick = {
                    selectedOption = "Высота крыши"

                    expanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(text = "Угол наклона") },
                onClick = {
                    selectedOption = "Угол наклона"
                    expanded = false
                },
            )
        }
        when (selectedOption) {
            "Покат" -> DropItemRoofParam(paramsState.hypotenuse, updateFromHypotenuse)

            "Высота крыши" -> DropItemRoofParam(paramsState.height, updateFromHeight)

            "Угол наклона" -> DropItemRoofParam(paramsState.angle, updateFromAngle)
        }
    }
}

/**
 * Поле для установки значения на выбор
 */
@Composable
fun DropItemRoofParam(
    value: BigDecimal,
    updateRoof: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
//    value = if (value == 0f)"" else value.toInt().toString(),
    TextField(
        value = value.toString(),
        onValueChange = { updateRoof(it.toBigDecimalOrNull() ?: BigDecimal.ZERO) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}

/**
 * Колонка с дополнительными расчетными параметрами для информации
 */
@Composable
fun OtherParamsColumn(
    paramsState: RoofParamsClassic4Scat,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OtherParamsRow(title = "Яндова", value = paramsState.yandova)
        OtherParamsRow(title = "Покат", value = paramsState.hypotenuse)
        OtherParamsRow(title = "Угол наклона", value = paramsState.angle)
        OtherParamsRow(title = "Высота", value = paramsState.height)
    }
}

@Composable
fun OtherParamsRow(
    title: String,
    value: BigDecimal,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text( value.toString())
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculationTile4ScatScreenPreview() {
    Roof_appTheme {
        CalculationTile4Scat(
            paramsState = RoofParamsClassic4Scat(),
            changeWidth = { _ -> },
            changeLen = { _ ->  },
            updateFromHeight = { _ ->  },
            updateFromAngle = { _ ->  },
            updateFromHypotenuse = { _ ->  },
            changeOverlapOfSheet = { _ ->  },
            changeWidthOfSheet = { _ ->  },
            changeMultiplicity = { _ ->  },
            getResult = {},
            isValid = true,
        )
    }
}
