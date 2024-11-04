package com.pavlig43.roofapp.ui.kit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import java.math.BigDecimal

@Composable
fun OtherParamsRow(
    title: String,
    value: BigDecimal,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text(value.toString())
    }
}

@Composable
fun OtherParamsColumn(
    paramsState: RoofParamsClassic4Scat,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OtherParamsRow(title = stringResource(R.string.yandova), value = paramsState.yandova)
        OtherParamsRow(title = stringResource(R.string.pokat), value = paramsState.pokat.value)
        OtherParamsRow(title = stringResource(R.string.angle_tilt), value = paramsState.angle.value)
        OtherParamsRow(
            title = stringResource(R.string.height_roof),
            value = paramsState.height.value,
        )
    }
}
