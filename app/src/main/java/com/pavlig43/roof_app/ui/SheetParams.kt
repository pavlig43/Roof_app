package com.pavlig43.roof_app.ui

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
import androidx.compose.ui.text.input.KeyboardType
import com.pavlig43.roof_app.model.Sheet


@Composable
fun CalculateSheetParams(
    sheet: Sheet,
    updateWidthGeneral: (Float) -> Unit,
    updateOverlap: (Float) -> Unit,
    updateMultiplicity:(Float) ->Unit,
    isDialog:Boolean = false,
    closeDialog:()->Unit ={},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CalculateSheetParamsRow(
            nameField = "Ширина листа (см)",
            sheet.widthGeneral,
            onValueChange = updateWidthGeneral
        )
        CalculateSheetParamsRow(
            nameField = "Нахлёст (см)",
            sheet.overlap,
            onValueChange = updateOverlap
        )
        CalculateSheetParamsRow(
            nameField = "Кратность (см)",
            sheet.multiplicity,
            onValueChange = updateMultiplicity
        )
        Text("*Кратность - округление расчетной длины листа в большую сторону. " +
                "Например при длине листа 243 см и кратности 5 см результат будет 245 см")
        if (isDialog){
            Button(onClick = closeDialog) {
                Text("OK")
            }
        }
    }

}

@Composable
fun CalculateSheetParamsRow(
    nameField: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$nameField", modifier.fillMaxWidth(0.4f))
        TextField(
            value = if (value == 0f) "" else value.toInt().toString(),
            onValueChange = { onValueChange(it.toFloatOrNull() ?: 0f) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }

}