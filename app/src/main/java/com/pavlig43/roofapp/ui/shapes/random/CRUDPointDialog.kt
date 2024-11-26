package com.pavlig43.roofapp.ui.shapes.random

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.mathbigdecimal.OffsetBD
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.kit.rowParam.ParamRow
import com.pavlig43.roofapp.ui.theme.Roof_appTheme

@Suppress("LongParameterList")
@Composable
fun CRUDPointDialog(
    offsetBD: OffsetBD = OffsetBD.Zero,
    onDismissRequest: () -> Unit,
    updateOrAddPoint: (OffsetBD) -> Unit,
    deletePoint: () -> Unit = {},
    checkOnProximity: (OffsetBD) -> Boolean,
    modifier: Modifier = Modifier,
) {
    var offsetBD1 by remember { mutableStateOf(offsetBD) }
    var isValid by remember { mutableStateOf(true) }
    Dialog(onDismissRequest) {
        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            ParamRow(
                paramTitle = R.string.offsetInX,
                value = offsetBD1.x,
                updateParam = { newX ->
                    offsetBD1 = offsetBD1.copy(x = newX)
                    isValid = checkOnProximity(offsetBD1)
                },
                canChangeOnNegative = true
            )
            ParamRow(
                paramTitle = R.string.offsetInY,
                value = offsetBD1.y,
                updateParam = { newY ->
                    offsetBD1 = offsetBD1.copy(y = newY)
                    isValid = checkOnProximity(offsetBD1)
                },
                canChangeOnNegative = true
            )
            if (!isValid) {
                Text(
                    stringResource(R.string.points_are_too_close),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            ButtonRowCRUD(
                offsetBD = offsetBD1,
                updateOrAddPoint = { updateOrAddPoint(offsetBD1) },
                deletePoint = {
                    deletePoint()
                    onDismissRequest()
                },
                onDismissRequest = onDismissRequest,
                isValid = isValid,
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun ButtonRowCRUD(
    offsetBD: OffsetBD,
    updateOrAddPoint: (OffsetBD) -> Unit,
    deletePoint: () -> Unit,
    onDismissRequest: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = {
                updateOrAddPoint(offsetBD)
                onDismissRequest()
            },
            enabled = isValid,
        ) {
            Text("OK")
        }
        val canDelete = offsetBD != OffsetBD.Zero
        if (canDelete) {
            Button(deletePoint) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun AddDialogPrev() {
    Roof_appTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            CRUDPointDialog(
                onDismissRequest = {},
                offsetBD = OffsetBD.Zero,
                updateOrAddPoint = { _ -> },
                checkOnProximity = { _ -> true },
                modifier =
                Modifier
                    .fillMaxSize()
                    .rotate(com.example.pdfcanvasdraw.RIGHT_DEGREE),
                deletePoint = TODO(),
            )
        }
    }
}
