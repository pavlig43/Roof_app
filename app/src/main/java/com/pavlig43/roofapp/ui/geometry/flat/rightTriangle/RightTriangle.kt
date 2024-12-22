package com.pavlig43.roofapp.ui.geometry.flat.rightTriangle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.WIDTH_COLUMN_PERCENT

@Composable
fun RightTriangle(
    modifier: Modifier = Modifier,
    viewModel: RightTriangleViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isValidCountParams by viewModel.isValidCountParams.collectAsState()
    val isValidParams by viewModel.isValidParams.collectAsState()
    Column(modifier = modifier) {
        Text(stringResource(R.string.select_2_params))
        RightTriangleParamRow(state.A, viewModel::onChange)
        RightTriangleParamRow(state.B, viewModel::onChange)
        RightTriangleParamRow(state.C, viewModel::onChange)
        RightTriangleParamRow(state.alpha, viewModel::onChange)
        RightTriangleParamRow(state.beta, viewModel::onChange)
        RightTriangleParamRow(state.gamma, viewModel::onChange)
        if (!isValidCountParams) {
            Text(stringResource(R.string.select_2_params), color = MaterialTheme.colorScheme.error)
        }
        if (!isValidParams) {
            Text(stringResource(R.string.not_numeric_data), color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun RightTriangleParamRow(
    param: RightTriangleParam,
    onValueChange: (RightTriangleParam) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = param.name.name, modifier = Modifier.fillMaxWidth(WIDTH_COLUMN_PERCENT))
        RightTriangleTextField(param, onValueChange)
    }
}

@Composable
fun RightTriangleTextField(
    param: RightTriangleParam,
    onValueChange: (RightTriangleParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = param.value,
        onValueChange = { str ->
            param.copy(value = str.replace(",", ".")).let(onValueChange)
        },
        modifier = modifier
    )
}
