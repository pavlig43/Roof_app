package com.pavlig43.roofapp.ui.kit.rowParam

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal

@Composable
fun TextFieldBigDecimal(
    value: BigDecimal,
    updateParam: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,

) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val placeholder by remember(isFocused) { mutableStateOf(if (isFocused) "" else "0") }

    Column {
        TextField(
            value = value.takeIf { it != BigDecimal.ZERO }?.toPlainString() ?: "",

            onValueChange = { input ->
                if (input.toIntOrNull() != null) {
                    updateParam(input.toBigDecimalOrNull() ?: BigDecimal.ZERO)
                }
            },
            placeholder = { Text(placeholder) },
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = modifier

        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun TextFieldBigDecimalPreview() {
    Roof_appTheme {
        TextFieldBigDecimal(
            value = BigDecimal.ZERO,
            updateParam = {}
        )
    }
}
