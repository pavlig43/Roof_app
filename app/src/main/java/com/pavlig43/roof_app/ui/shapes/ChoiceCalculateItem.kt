package com.pavlig43.roof_app.ui.shapes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.pavlig43.roof_app.model.ShapeSide

@Composable
fun ChoiceCalculateItem(
    shapeSide: ShapeSide,
    changeSideValue: (ShapeSide) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${shapeSide.name}(cm)", modifier = Modifier.fillMaxWidth(0.5f))
        TextField(
            value = shapeSide.value.toString(),
            onValueChange = { sideValue: String ->
                changeSideValue(
                    shapeSide.copy(
                        value = sideValue.replace(
                            Regex("[^0-9]"),
                            ""
                        ).toIntOrNull() ?: 0
                    )

                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
    }
}

