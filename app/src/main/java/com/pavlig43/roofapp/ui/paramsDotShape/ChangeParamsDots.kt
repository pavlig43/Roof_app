package com.pavlig43.roofapp.ui.paramsDotShape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.Dot
import java.math.BigDecimal

/**
 * компоуз функция, предствляющая собой диалог в котором можно изменить отклонение от левой нижней точки по обеим осям
 */
@Composable
fun ChangeParamsDots(
    dot: Dot,
    changeDot: (Dot) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.offset_from_left_bottom))
            ParamsDotRow(
                value = dot.offset.x,
                onValueChange = {
                        newValue ->
                    changeDot(dot.copy(offset = dot.offset.copy(x = newValue)))
                },
                axis = "X",
                canChangePlus = dot.canMinusX,
                plus = dot.offset.x >= BigDecimal.ZERO,
                canChangeAxis = dot.canChangeX,
                changePlus = {
                    changeDot(dot.copy(offset = dot.offset.copy(x = -dot.offset.x)))
                },
            )
            ParamsDotRow(
                value = dot.offset.y,
                onValueChange = {
                        newValue ->
                    changeDot(dot.copy(offset = dot.offset.copy(y = newValue)))
                },
                canChangePlus = dot.canMinusY,
                plus = dot.offset.y >= BigDecimal.ZERO,
                canChangeAxis = dot.canChangeY,
                changePlus = {
                    changeDot(dot.copy(offset = dot.offset.copy(y = -dot.offset.y)))
                },
            )
            Button(
                onClick = onDismissRequest,
            ) {
                Text(text = "OK")
            }
        }
    }
}
