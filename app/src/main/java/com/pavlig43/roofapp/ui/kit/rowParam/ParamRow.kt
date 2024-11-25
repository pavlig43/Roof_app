package com.pavlig43.roofapp.ui.kit.rowParam

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pavlig43.roofapp.WIDTH_COLUMN_PERCENT
import java.math.BigDecimal

@Suppress("LongParameterList")
@Composable
fun ParamRow(
    paramTitle: Int,
    unit: Int? = null,
    value: BigDecimal,
    updateParam: (BigDecimal) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextParam(
            paramTitle = paramTitle,
            unit = unit,
            modifier = Modifier.fillMaxWidth(WIDTH_COLUMN_PERCENT),
        )
        TextFieldBigDecimal(
            value = value,
            updateParam = { newValue -> updateParam(newValue) },
        )
    }
}
