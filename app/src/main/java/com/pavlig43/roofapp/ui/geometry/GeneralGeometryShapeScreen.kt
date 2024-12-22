package com.pavlig43.roofapp.ui.geometry

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.ui.theme.Roof_appTheme

@Composable
fun GeneralGeometryShapeScreen(
    @DrawableRes shapeImage: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { ShapeImage(shapeImage) }
        item { content() }
    }
}

@Composable
private fun ShapeImage(
    @DrawableRes shapeImage: Int,
    modifier: Modifier = Modifier,
) {
    Image(
        painterResource(shapeImage),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(R.string.shape_image),
        modifier = modifier
    )
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun GeneralGeometryShapeScreenPrev() {
    Roof_appTheme {
        GeneralGeometryShapeScreen(R.drawable.right_triangle) { }
    }
}
