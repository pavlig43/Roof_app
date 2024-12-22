package com.pavlig43.pdfcanvasdraw.implementationCore.compose

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

private const val PADDING_PERCENT_X = 0.1F
private const val PADDING_PERCENT_Y = 0.1F

@Composable
fun ComposeBuild(
    pageRendererFactory: (PageConfig) -> PageRenderer,
    paddingPercentX: Float = PADDING_PERCENT_X,
    paddingPercentY: Float = PADDING_PERCENT_Y,

) {
    var pageConfig by remember {
        mutableStateOf(
            PageConfig(
                x = 0,
                y = 0,
                paddingPercentX,
                paddingPercentY,
                getStartPointF = { x, paddingPercentX, y, paddingPercentY ->
                    PointF(x * paddingPercentX, y - (y * paddingPercentY))
                }
            ),
        )
    }
    val pageRenderer = remember(pageConfig, pageRendererFactory) { pageRendererFactory(pageConfig) }

    var modifier: Modifier by remember { mutableStateOf(Modifier) }

    LaunchedEffect(pageRenderer) {
        Log.d("ComposeBuild", pageConfig.toString())
        val canvas = ComposeCanvas()
        pageRenderer.renderPage(canvas)
        modifier = canvas.modifier
        modifier =
            modifier.then(
                Modifier.pointerInput(Unit) {
                    detectTapGestures { offset: Offset ->
                        pageRenderer.handleGetTap(PointF(offset.x, offset.y))
                    }
                },
            )
    }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .onSizeChanged { intSize ->
                pageConfig =
                    pageConfig.copy(
                        x = intSize.width,
                        y = intSize.height,
                    )
            },
    ) {
    }
}
