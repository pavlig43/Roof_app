package com.pavlig43.roofapp.ui.roof.calculationTile4scat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.model.roofParamsClassic4Scat.RoofParamsClassic4Scat
import java.math.BigDecimal

/**
 * Колонка с дополнительными расчетными параметрами для информации
 */
@Composable
fun OtherParamsRow(
    title: String,
    value: BigDecimal,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = title)
            Spacer(modifier = Modifier.weight(1f))
            Text(value.toString())
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
fun OtherParamsColumn(
    paramsState: RoofParamsClassic4Scat,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OtherParamsRow(
            title = stringResource(R.string.yandova),
            value = paramsState.yandova.value
        )
        OtherParamsRow(
            title = stringResource(R.string.pokat_trapezoid),
            value = paramsState.pokatTrapezoid.value
        )
        OtherParamsRow(
            title = stringResource(R.string.pokat_triangle),
            value = paramsState.pokatTriangle.value
        )
        OtherParamsRow(
            title = stringResource(R.string.ridge),
            value = paramsState.userRidge?.value ?: paramsState.calculateStandardRidge.value
        )

        OtherParamsRow(
            title = stringResource(R.string.angle_tilt),
            value = paramsState.angle.value
        )
        OtherParamsRow(
            title = stringResource(R.string.height_roof),
            value = paramsState.height.value,
        )
    }
}
