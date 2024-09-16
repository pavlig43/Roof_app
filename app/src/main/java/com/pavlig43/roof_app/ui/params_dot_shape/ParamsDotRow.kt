package com.pavlig43.roof_app.ui.params_dot_shape

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pavlig43.roof_app.ui.MinusIcon

/**
 * компоуз функция, которая показывает строку, где можно менять отклонение от крайней левой точки фигуры по одной из оси
 * [axis] - текстовоое обозначение оси
 * [canChangePlus]- возможность сделать отклонение отрицательным относительно крайней левой точки- для Ui это наличие/отсутствие кнопки, которое меняет знак (дабы создавать правильные многоугольники)
 * когда нибудь нужно доработать этот механизм(защита от дурака)
 * [plus],[changePlus] - кнопка и функция, которая меняет знак у отклонения
 */
@Composable
fun ParamsDotRow(
    value: Float,
    onValueChange: (Float) -> Unit,
    axis: String = "Y",
    canChangePlus: Boolean = false,
    plus:Boolean,
    canChangeAxis:Boolean,
    changePlus:()->Unit,

    modifier: Modifier = Modifier
) {



    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Отклонение по $axis (см)", modifier = Modifier
            .padding(end = 4.dp)
            .weight(0.55f))
        if (canChangePlus) {
            Button(onClick = changePlus, modifier = Modifier.weight(0.2f)) {
                Icon(
                    imageVector = if (plus) Icons.Default.Add else MinusIcon,
                    contentDescription = null
                )
            }
        }
        else{
            Spacer(modifier = Modifier.weight(0.2f))
        }
        TextField(
            value = if (value==0f)"" else value.toInt().toString(),
            readOnly = !canChangeAxis,
            onValueChange = { it: String ->
                val newValue =  it.toIntOrNull()?:0
                onValueChange(if (plus) newValue.toFloat() else - newValue.toFloat()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(0.35f)
        )


    }
}