package com.pavlig43.roofapp.ui.general

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.general.rowParam.ParamRow

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
                updateSheetParam = updateSheetParam,
            )
        }

        Text(
            stringResource(R.string.description_multiplicity),
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
    updateSheetParam: (SheetParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    ParamRow(
        modifier = modifier,
        paramTitle = sheetParam.name.title,
        unit = sheetParam.unit.title,
        value = sheetParam.value,
        updateParam = { newValue -> updateSheetParam(sheetParam.copy(value = newValue)) }
    )
}
