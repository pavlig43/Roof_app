package com.pavlig43.roofapp.ui.kit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.ui.kit.rowParam.ParamRow

@Composable
fun CalculateSheetParamsDialog(
    onDismissRequest: () -> Unit,
    sheet: Sheet,
    updateSheetParam: (SheetParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            CalculateSheetParams(
                sheet = sheet,
                updateSheetParam = updateSheetParam,
            )
            Button(
                onClick = onDismissRequest,
            ) {
                Text("OK")
            }
        }
    }
}

@Composable
fun CalculateSheetParams(
    sheet: Sheet,
    updateSheetParam: (SheetParam) -> Unit,
    modifier: Modifier = Modifier,
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
    }
}

@Composable
fun CalculateSheetParamsRow(
    sheetParam: SheetParam,
    updateSheetParam: (SheetParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    ParamRow(
        paramTitle = sheetParam.name.title,
        value = sheetParam.value,
        updateParam = { newValue -> updateSheetParam(sheetParam.copy(value = newValue)) },
        modifier = modifier,
        unit = sheetParam.unit.title,
    )
}
