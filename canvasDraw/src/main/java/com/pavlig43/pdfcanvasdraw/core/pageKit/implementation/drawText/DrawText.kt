package com.pavlig43.pdfcanvasdraw.core.pageKit.implementation.drawText

import android.graphics.PointF
import com.pavlig43.pdfcanvasdraw.TEXT_SIZE_MEDIUM_PLUS
import com.pavlig43.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

fun List<String>.drawTextOnSeveralPages(
    markInfo: String = "",
    pageConfig: PageConfig = PageConfig(),
    testSizeFloat: Float = TEXT_SIZE_MEDIUM_PLUS,
    spaceLine: Float = DEFAULT_SPACE_LINE,
): List<PageRenderer> {
    val countPages = getCountStringRowOnOnePage(
        heightPage = pageConfig.y.toFloat(),
        startPaddingY = pageConfig.startPointF.y,
        textSize = testSizeFloat,
        spaceLine = spaceLine,
    )
    return this.chunked(countPages).map {
        DrawText(
            it,
            markInfo,
            pageConfig,
            AddVerticalPaddingForLineText(pageConfig.startPointF.y, spaceLine),
            testSizeFloat
        )
    }
}

private class DrawText(
    private val info: List<String>,
    private val markInfo: String = "",
    override val pageConfig: PageConfig,
    private val rowSpacing: AddVerticalPaddingForLineText,
    private val testSizeFloat: Float = TEXT_SIZE_MEDIUM_PLUS
) : PageRenderer() {
    override suspend fun CanvasInterface.drawContent() {
        drawAndRotateText(
            markInfo,
            pivotX = markOffset.x,
            pivotY = markOffset.y,
            paintText = createPaintText().apply { }
        )
        info.forEach {
            drawAndRotateText(
                it,
                pivotX = pageConfig.startPointF.x,
                pivotY = pageConfig.startPointF.y + rowSpacing.y,
                paintText = createPaintText().apply { textSize = testSizeFloat }
            )

            rowSpacing.addTransferText()
        }
    }

    override fun handleGetTap(tapPointF: PointF) {
        TODO("Not yet implemented")
    }
}

/**
 * Подсчёт количества строк текста, которые помещаются на одной странице
 * число 1,8 методом подбора полученно, работает только для этого размера текса
 */
@Suppress("MagicNumber")
private fun getCountStringRowOnOnePage(
    heightPage: Float,
    textSize: Float,
    startPaddingY: Float,
    spaceLine: Float
): Int {
    val usableHeight = heightPage - 2 * startPaddingY
    val lineText = (textSize + spaceLine) / 1.8
    return (usableHeight / lineText).toInt()
}

@Suppress("MagicNumber")
private val markOffset = PointF(10f, 10f)
private const val DEFAULT_SPACE_LINE = 25f
