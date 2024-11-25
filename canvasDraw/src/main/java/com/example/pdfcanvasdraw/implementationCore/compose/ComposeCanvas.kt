package com.example.pdfcanvasdraw.implementationCore.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.CanvasInterface
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.PathAbstract
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.PaintAbstract
import com.example.pdfcanvasdraw.core.abstractCanvas.drawKit.paint.TextPainAbstract
import com.example.pdfcanvasdraw.implementationCore.compose.paint.ComposePaint
import com.example.pdfcanvasdraw.implementationCore.compose.paint.ComposeTextPain

class ComposeCanvas : CanvasInterface {
    var modifier: Modifier = Modifier
        private set

    override fun createPaint(): PaintAbstract {
        return ComposePaint()
    }

    override fun createPaintText(): TextPainAbstract {
        return ComposeTextPain()
    }

    override fun createPath(): PathAbstract {
        return ComposePath()
    }

    override fun drawLine(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paint: PaintAbstract,
    ) {
        if (paint is ComposePaint) {
            val paint1 = paint.getPaint()

            modifier =
                modifier.then(
                    Modifier.drawBehind {
                        drawLine(
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            color = paint1.color,
                            strokeWidth = paint1.strokeWidth,
                            alpha = paint1.alpha,
                        )
                    },
                )
        }
    }

    override fun drawCircle(
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: PaintAbstract,
    ) {
        if (paint is ComposePaint) {
            val paint1 = paint.getPaint()

            modifier =
                modifier.then(
                    Modifier.drawBehind {
                        drawCircle(
                            center = Offset(centerX, centerY),
                            radius = radius,
                            color = paint1.color,
                            style = paint1.style,
                        )
                    },
                )
        }
    }

    override fun drawAndRotateText(
        text: String,
        paintText: TextPainAbstract,
        degree: Float,
        pivotX: Float,
        pivotY: Float,
    ) {
        if (paintText is ComposeTextPain) {
            modifier =
                modifier.then(
                    Modifier.drawBehind {
                        rotate(degree, Offset(pivotX, pivotY)) {
                            drawContext.canvas.nativeCanvas.drawText(
                                text,
                                pivotX,
                                pivotY,
                                paintText.getTextPaint(),
                            )
                        }
                    },
                )
        }
    }

    override fun drawPath(
        path: PathAbstract,
        paint: PaintAbstract,
    ) {
        if (path is ComposePath && paint is ComposePaint) {
            val innerPaint = paint.getPaint()
            val innerPath = path.getPath()
            modifier =
                modifier.then(
                    Modifier.drawBehind {
                        drawPath(
                            path = innerPath,
                            color = innerPaint.color,
                            style = innerPaint.style,
                        )
                    },
                )
        }
    }
}
