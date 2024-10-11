package com.pavlig43.roofapp.icons

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val MinusIcon: ImageVector
    get() {
        return ImageVector
            .Builder(
                name = "Minus",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                materialPath {
                    moveTo(6f, 13f)
                    horizontalLineTo(18f)
                    verticalLineTo(11f)
                    horizontalLineTo(6f)
                    close()
                }
            }.build()
    }
