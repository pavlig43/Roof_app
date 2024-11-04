package com.pavlig43.roofapp.ui.kit.rowParam

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import java.math.BigDecimal

@Composable
fun TextFieldBigDecimal(
    value: BigDecimal,
    readOnly: Boolean = false,
    updateParam: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value.takeIf { it != BigDecimal.ZERO }?.toPlainString() ?: "",
        readOnly = readOnly,
        onValueChange = { updateParam(it.toBigDecimalOrNull() ?: BigDecimal.ZERO) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}
