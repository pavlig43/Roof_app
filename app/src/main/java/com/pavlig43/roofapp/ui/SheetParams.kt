package com.pavlig43.roofapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import java.math.BigDecimal

@Composable
fun CalculateSheetParams(
    modifier: Modifier = Modifier,
    sheet: Sheet,
    updateSheetParam: (SheetParam) -> Unit,
    isDialog: Boolean = false,
    closeDialog: () -> Unit = {},
) {
    Column(modifier = modifier) {
        arrayOf(sheet.widthGeneral, sheet.overlap, sheet.multiplicity).forEach { sheetParam ->
            CalculateSheetParamsRow(
                sheetParam = sheetParam,
                onValueChange = updateSheetParam,
            )
        }
//        CalculateSheetParamsRow(
//            nameField = stringResource(R.string.params_with_unit, stringResource(sheet.widthGeneral.name.title), stringResource(R.string.cm)),
//            sheet.widthGeneral,
//            onValueChange = updateWidthGeneral,
//        )
//        CalculateSheetParamsRow(
//            nameField = "Нахлёст (см)",
//            sheet.overlap,
//            onValueChange = updateOverlap,
//        )
//        CalculateSheetParamsRow(
//            nameField = "Кратность (см)",
//            sheet.multiplicity,
//            onValueChange = updateMultiplicity,
//        )
        Text(
            "*Кратность - округление расчетной длины листа в большую сторону. " +
                "Например при длине листа 243 см и кратности 5 см результат будет 245 см",
        )
        if (isDialog) {
            Button(onClick = closeDialog) {
                Text("OK")
            }
        }
    }
}

@Composable
fun CalculateSheetParamsRow(
    sheetParam: SheetParam,
    onValueChange: (SheetParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.params_with_unit, stringResource(sheetParam.name.title), stringResource(R.string.cm)),
            modifier.fillMaxWidth(0.4f),
        )
        TextField(
            value = sheetParam.value.takeIf { it != BigDecimal.ZERO }?.toPlainString() ?: "",
            onValueChange = { onValueChange(sheetParam.copy(value = it.toBigDecimalOrNull() ?: BigDecimal.ZERO)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}
