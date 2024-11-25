package com.example.pdfcanvasdraw.implementationCore.compose.paint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract

class ComposePaint : PaintAbstract() {
    data class InnerPaint(
        val color: Color = Color.Black,
        val strokeWidth: Float = 1f,
        val style: DrawStyle = Stroke(strokeWidth),
        val alpha: Float = 1f,
    )

    private var innerPaint = InnerPaint()

    override var color: Int
        get() = innerPaint.color.toArgb()
        set(value) {
            innerPaint = innerPaint.copy(color = Color(value))
        }
    override var strokeWidth: Float
        get() = innerPaint.strokeWidth
        set(value) {
            innerPaint = innerPaint.copy(strokeWidth = value)
        }

    override var style: Style
        get() = Style.FILL
        set(value) {
            innerPaint =
                when (value) {
                    Style.STROKE -> innerPaint.copy(style = Stroke(strokeWidth))
                    Style.FILL -> innerPaint.copy(style = Fill)
                }
        }

    override var alpha: Int
        get() = innerPaint.alpha.toInt()
        set(value) {
            innerPaint = innerPaint.copy(alpha = value / 255f)
        }

    override fun getPaint() = innerPaint
}
