package com.pavlig43.roof_app.ui.shapes.triangle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import android.util.Log
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.Triangle
import com.pavlig43.roof_app.model.toRound2Scale
import com.pavlig43.roof_app.utils.AddVerticalPaddingForLineText
import java.io.File
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.math.tan

fun triangleResult(
    context: Context,
    triangle: Triangle,
    sheet: Sheet
): File {
    val a = triangle.a.value.toFloat()
    val b = triangle.b.value.toFloat()
    val c = triangle.c.value.toFloat()
    val triangleShape = TriangleShape(a, b, c,sheet)
    val pdfDocument = PdfDocument()

    val pageInfo1 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 1).create()
    val page1 = pdfDocument.startPage(pageInfo1)
    val canvas1 = page1.canvas
    triangleShape.ruler(canvas1, horizontal = false)
    triangleShape.ruler(canvas1, zero = false)
    triangleShape.drawTriangle(canvas1)
    triangleShape.sheetOnTriangle(canvas1)
    pdfDocument.finishPage(page1)
    val pageInfo2 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 2).create()
    val page2 = pdfDocument.startPage(pageInfo2)
    val canvas2 = page2.canvas
    triangleShape.infoPage(canvas2)
    pdfDocument.finishPage(page2)

    val file = File(context.getExternalFilesDir(null), "roof.pdf")


    file.outputStream().use { pdfDocument.writeTo(it) }
    pdfDocument.close()
    return file
}

class TriangleShape(
    private val a: Float,
    private val b: Float,
    private val c: Float,
    private val sheet: Sheet = Sheet()
) {
    private val widthPage = A4WIDTH.toFloat()
    private val heightPage = A4HEIGHT.toFloat()
    private val paddingWidth = (widthPage * 0.05).toFloat()
    private val paddingHeight = (heightPage * 0.05).toFloat()
    private val cosGamma = (a * a + b * b - c * c) / (2 * a * b)
    private val cosB = (a * a + c * c - b * b) / (2 * a * c)
    private val sinGamma = sqrt(1 - cosGamma * cosGamma)
    private val oneMeterInPxA = (heightPage - 2 * paddingHeight) / (ceil(a / 100).toInt())
    private val heightTriangle = b * sinGamma
    private val oneMeterInPxHeightTriangle =
        (widthPage - 2 * paddingWidth) / (ceil(heightTriangle / 100).toInt())
    private val dotOfHeightY = b * cosGamma * oneMeterInPxA

    val listOfSheets:MutableList<Double> = mutableListOf()
    fun ruler(
        canvas: Canvas,
        zero: Boolean = true,
        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f  // Толщина линии
            style = Paint.Style.STROKE
        },
        horizontal: Boolean = true,

        ) {


        if (!horizontal) {
            val x = paddingWidth

            canvas.drawLine(
                x,
                paddingHeight,
                x,
                heightPage - paddingHeight,
                paint
            )
            val countMetres = ceil(a / 100).toInt()
            for (i in 0..countMetres) {
                val y =
                    i.toFloat() * oneMeterInPxA + paddingHeight
                canvas.drawLine(x - 10, y, x + 10, y, Paint().apply { strokeWidth = 3f })
                canvas.rotate(90f, x - 25, y - 10)
                when {
                    i == 0 && !zero -> continue
                    else -> canvas.drawText(
                        "$i м",
                        x - 25,
                        y - 10,
                        Paint().apply { textSize = 20f })
                }

                canvas.rotate(-90f, x - 25, y - 10)
            }
        } else {
            val y = paddingHeight

            canvas.drawLine(
                paddingWidth,
                y,
                widthPage - paddingWidth,
                y,
                paint
            )
            val countMetres = ceil(heightTriangle / 100).toInt()
            for (i in 0..countMetres) {
                val x = i.toFloat() * oneMeterInPxHeightTriangle + paddingWidth
                canvas.drawLine(x, y - 10, x, y + 10, Paint().apply { strokeWidth = 3f })
                when {
                    i == 0 && !zero -> continue
                    else -> canvas.drawText(
                        "$i м",
                        x - 10,
                        y - 25,
                        Paint().apply { textSize = 20f })
                }

            }
        }

    }

    fun drawTriangle(
        canvas: Canvas
    ) {
        val paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            alpha = 162// Толщина линии
            style = Paint.Style.STROKE
        }
        val paintText = Paint().apply {
            textSize = 20f
        }
        val path = android.graphics.Path().apply {
            moveTo(paddingWidth, paddingHeight)
            lineTo(
                heightTriangle / 100 * oneMeterInPxHeightTriangle + paddingWidth,
                (b / 100) * cosGamma * oneMeterInPxA + paddingHeight
            )
            lineTo(paddingWidth, (a / 100) * oneMeterInPxA + paddingHeight)
            close()
        }

        canvas.drawPath(
            path, paint
        )
        canvas.rotate(90f, paddingWidth + 20, (a / 2 / 100) * oneMeterInPxA + paddingWidth)
        canvas.drawText(
            "${a.toInt()} cm",
            paddingWidth + 20,
            (a / 2 / 100) * oneMeterInPxA + paddingWidth,
            paintText
        )
        canvas.rotate(-90f, paddingWidth + 20, (a / 2 / 100) * oneMeterInPxA + paddingWidth)

        val xLeft = (heightTriangle / 100 * oneMeterInPxHeightTriangle) / 2 + paddingWidth + 5f
        val yLeft = (b * cosGamma) / 200 * oneMeterInPxA + paddingHeight + 0f
        val dotOfHeightX = heightTriangle * oneMeterInPxHeightTriangle

        val angleC = 90 - Math.toDegrees(atan(dotOfHeightX / dotOfHeightY).toDouble())
        canvas.rotate(angleC.toFloat(), xLeft, yLeft)
        canvas.drawText("${b.toInt()} cm", xLeft, yLeft, paintText)
        canvas.rotate(-angleC.toFloat(), xLeft, yLeft)


        val xRight = heightTriangle / 100 * oneMeterInPxHeightTriangle / 2 + paddingWidth + 10
        val yRight = (b * cosGamma + (c * cosB) / 2) * oneMeterInPxA / 100 + paddingHeight
        val angleB =
            90 + Math.toDegrees(atan(dotOfHeightX / (a * oneMeterInPxA - dotOfHeightY)).toDouble())
        canvas.rotate(angleB.toFloat(), xRight, yRight)
        canvas.drawText("${c.toInt()} cm", xRight, yRight, paintText)
        canvas.rotate(-angleB.toFloat(), xRight, yRight)
    }

    private fun drawSheet(
        canvas: Canvas,
        bottomX: Float,
        topX: Float,
        bottomY: Float,
        topY: Float,
        lenOfSheet:Int,
        paintSheet: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 0.5f  // Толщина линии
            style = Paint.Style.STROKE
        }
    ) {

        val paintOverlap: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 0.3f  // Толщина линии
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(
                floatArrayOf(10f, 20f), 0f
            )
        }
        val path = Path().apply {
            moveTo(bottomX, bottomY)
            lineTo(topX, bottomY)
            moveTo(topX, bottomY)
            lineTo(topX, topY)
            Log.d("color", paintSheet.color.toString())
            close()
        }
        canvas.drawPath(path, paintSheet)
        canvas.drawLine(topX, topY, bottomX, topY, paintOverlap)
        val x = (topX- bottomX) / 2 + bottomX / 2
        val y = (topY - bottomY) / 2 + bottomY
        canvas.rotate(90f, x, y)
        canvas.drawText("$lenOfSheet cm", x, y, paintSheet)
        canvas.rotate(-90f, x, y)
    }

    fun sheetOnTriangle(
        canvas: Canvas,

        ) {
        val countOfSheet = ceil((a / sheet.visible + sheet.overlap * 100) / 100).toInt()
        var bottomY = paddingHeight
        val hT = heightTriangle / 100 * oneMeterInPxHeightTriangle + paddingWidth
        for (numberOfSheet in 1..countOfSheet) {

            val topY = bottomY + (sheet.widthGeneral * oneMeterInPxA).toFloat()
            val lenOfSheet =  if (bottomY > dotOfHeightY/100 + paddingHeight) {
                val adjacent = a / 100 - (numberOfSheet-1) * sheet.visible
                tan(acos(cosB)) * adjacent
            }
            else{
                val adjacent = numberOfSheet * sheet.visible + sheet.overlap
                 tan(acos(cosGamma)) * adjacent
            }
            listOfSheets.add(ceil(lenOfSheet/sheet.multiplicity)*sheet.multiplicity)

            val topX =(lenOfSheet * oneMeterInPxHeightTriangle + paddingWidth).toFloat()



            drawSheet(
                canvas = canvas,
                bottomX = paddingWidth,
                bottomY = bottomY,
                topY = topY,
                topX = minOf(topX, hT),
                lenOfSheet = ((ceil(lenOfSheet/sheet.multiplicity)*sheet.multiplicity)*100 ).toInt()
            )


            bottomY = ((numberOfSheet * sheet.visible) * oneMeterInPxA).toFloat() + paddingHeight
        }
    }
    fun infoPage(
        canvas: Canvas,

    ) {

        val x = paddingWidth
        val startY = paddingHeight
        val textTrasfer = AddVerticalPaddingForLineText(startY)
        val paintText = TextPaint().apply {
            textSize = 20f
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        }
        val orderSheet = listOfSheets.groupingBy { it }.eachCount().toSortedMap()
        orderSheet.forEach { (k, v) ->
            canvas.drawText(
                "${k.toRound2Scale()}m = $v",
                x,
                textTrasfer.addTransferText(),
                paintText
            )
        }

    }
}
