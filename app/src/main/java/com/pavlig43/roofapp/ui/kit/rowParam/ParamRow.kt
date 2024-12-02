package com.pavlig43.roofapp.ui.kit.rowParam

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.WIDTH_COLUMN_PERCENT
import com.pavlig43.roofapp.icons.MinusIcon
import com.pavlig43.roofapp.ui.theme.Roof_appTheme
import java.math.BigDecimal

@Suppress("LongParameterList")
@Composable
fun ParamRow(
    paramTitle: Int,
    value: BigDecimal,
    updateParam: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
    unit: Int? = null,
    canChangeOnNegative: Boolean = false
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextParam(
            paramTitle = paramTitle,
            modifier = Modifier.fillMaxWidth(WIDTH_COLUMN_PERCENT),
            unit = unit,
        )
        if (canChangeOnNegative) {
            IconButton({ updateParam(-value) }) {
                Icon(
                    if (value >= BigDecimal.ZERO) MinusIcon else Icons.Default.Add,
                    stringResource(R.string.button_to_change_the_sign_of_a_number)
                )
            }
        }
        TextFieldBigDecimal(
            value = value,
            updateParam = { newValue -> updateParam(newValue) }
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun ParamRowPreview() {
    Roof_appTheme {
        ParamRow(
            R.string.offsetInX,
            value = BigDecimal.ZERO,
            updateParam = {},
            canChangeOnNegative = true
        )
    }
}
