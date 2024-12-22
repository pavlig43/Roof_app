package com.pavlig43.roofapp.navigation.ui.geometry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleButton(
    onClick: (String) -> Unit = {},
    geometryItemNav: GeometryItemNav,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Float = 5f,
) {
    Card(modifier = modifier.padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.Companion
                    .size(size)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onClick(geometryItemNav.route) },
            ) {
                Canvas(modifier = Modifier.Companion) {
                    val radius = size.toPx() / 2
                    val shape = geometryItemNav.getShape(radius)
                    val path = Path().apply {
                        moveTo(shape[0].x, shape[0].y)
                        shape.drop(1).forEach { lineTo(it.x, it.y) }
                        close()
                    }
                    drawPath(path, Color.Companion.Black, style = Stroke(strokeWidth))
                }
            }
            Text(stringResource(id = geometryItemNav.name))
        }
    }
}
