package com.pavlig43.roofapp.ui.paramsDotShape

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.icons.MinusIcon
import com.pavlig43.roofapp.ui.kit.rowParam.TextFieldBigDecimal
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal

private const val TEXT_WEIGHT = 0.55f
private const val BUTTON_WEIGHT = 0.2f
private const val TEXT_FIELD_WEIGHT = 0.35f

/**
 * компоуз функция, которая показывает строку, где можно менять отклонение от крайней левой точки
 * фигуры по одной из оси
 * [axis] - текстовоое обозначение оси
 * [canChangePlus]- возможность сделать отклонение отрицательным относительно крайней левой точки-
 * для Ui это наличие/отсутствие кнопки, которое меняет знак (дабы создавать правильные многоугольники)
 * когда нибудь нужно доработать этот механизм(защита от дурака)
 * [plus], [changePlus] - кнопка и функция, которая меняет знак у отклонения
 */
@Suppress("LongParameterList")
@Composable
fun ParamsDotRow(
    modifier: Modifier = Modifier,
    value: BigDecimal,
    onValueChange: (BigDecimal) -> Unit = { _ -> },
    axis: String = "Y",
    canChangePlus: Boolean = false,
    plus: Boolean,
    canChangeAxis: Boolean,
    changePlus: () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.PointF, axis),
            modifier = Modifier.weight(TEXT_WEIGHT),
        )
        if (canChangePlus) {
            Button(
                onClick = changePlus,
                modifier = Modifier
                    .weight(BUTTON_WEIGHT)
                    .padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = if (plus) Icons.Default.Add else MinusIcon,
                    contentDescription = null,
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(BUTTON_WEIGHT))
        }
        TextFieldBigDecimal(
            value = value,
            readOnly = !canChangeAxis,
            updateParam = { newValue -> onValueChange(if (plus) newValue else -newValue) },
            modifier = Modifier.weight(TEXT_FIELD_WEIGHT),
        )
    }
}

@Suppress("UnusedPrivateMember", "MagicNumber")
@Preview(showBackground = true)
@Composable
private fun ParamsDotRowPreviewWithAxis() {
    Roof_appTheme {
        ParamsDotRow(
            value = BigDecimal(100),
            axis = "Y",
            canChangePlus = true,
            plus = true,
            canChangeAxis = true,
        ) { }
    }
}

@Suppress("UnusedPrivateMember", "MagicNumber")
@Preview(showBackground = true)
@Composable
private fun ParamsDotRowPreview() {
    Roof_appTheme {
        ParamsDotRow(
            value = BigDecimal(100),
            axis = "Y",
            canChangePlus = false,
            plus = true,
            canChangeAxis = true,
        ) { }
    }
}
