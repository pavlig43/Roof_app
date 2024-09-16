package com.pavlig43.roof_app.ui.params_dot_shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.pavlig43.roof_app.model.Dot


/**
 * компоуз функция, предствляющая собой диалог в котором можно изменить отклонение от левой нижней точки по обеим осям
 */
@Composable
fun ChangeParamsDots(
    dot: Dot,
    changeDot: (Dot) -> Unit,
    onDismissRequest: () -> Unit,

    modifier: Modifier = Modifier
) {


    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier.background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Отклонение от левой нижней точки")
            ParamsDotRow(
                value = dot.distanceX,
                onValueChange = { newValue -> changeDot(dot.copy(distanceX = newValue)) },
                axis = "X",
                canChangePlus = dot.canMinusX,
                plus = dot.distanceX>=0,
                canChangeAxis = dot.canChangeX,
                changePlus = {
                    changeDot(dot.copy(distanceX = -dot.distanceX))
                }
            )
            ParamsDotRow(
                value = dot.distanceY,
                onValueChange = { newValue -> changeDot(dot.copy(distanceY = newValue)) },
                canChangePlus = dot.canMinusY,
                plus = dot.distanceY>=0,
                canChangeAxis = dot.canChangeY,
                changePlus = {
                    changeDot(dot.copy(distanceY = -dot.distanceY))
                }
            )
            Button(
                onClick = onDismissRequest,

                ) {
                Text(text = "OK")
            }
        }

    }

}