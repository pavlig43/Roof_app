package com.pavlig43.roof_app.ui.calculation_tile_4scat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pavlig43.roof_app.A4HEIGHT
import com.pavlig43.roof_app.A4WIDTH

import com.pavlig43.roof_app.model.RoofParamsClassic4ScatState
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.toRound2Scale
import com.pavlig43.roof_app.ui.LoadDocumentImages
import com.pavlig43.roof_app.utils.AddVerticalPaddingForLineText
import java.io.File
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.math.tan

@Composable
fun ResultTile4ScatPDF(
    listBitmap: List<Bitmap>,
    returnToCalculateScreen: () -> Unit,
    shareFile: () -> Unit,
    nameFile: SaveNameFile,
    saveFile: () -> Unit,
    checkSaveName: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var openDialog by remember {
        mutableStateOf(false)
    }
    BackHandler {
        returnToCalculateScreen
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (openDialog) {
            SaveDialog(
                saveNameFile = nameFile.name,
                saveFile = saveFile,
                checkSaveName = checkSaveName,
                onDismiss = { openDialog = false },
                isValid = nameFile.isValid
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = shareFile) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }
            IconButton(onClick = {
                openDialog = !openDialog
                run { checkSaveName(nameFile.name) }
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
        LoadDocumentImages(listBitmap = listBitmap)
    }


}



@Composable
fun SaveDialog(
    saveNameFile: String,
    saveFile: () -> Unit,
    checkSaveName: (String) -> Unit,
    onDismiss: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {


    AlertDialog(
        title = { Text(text = "Сохранить с именем") },
        text = {
            Column {
                OutlinedTextField(value = saveNameFile, onValueChange = checkSaveName)
                if (!isValid) Text(text = "Уже такое есть")
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    saveFile()
                    onDismiss()
                },
                enabled = isValid
            ) {
                Text(text = "Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Отмена")
            }
        }
    )
}


fun roofResult4Scat(
    roofParamsClassic4ScatState: RoofParamsClassic4ScatState,
    context: Context,
): File {
    val pdfDocument = PdfDocument()

    val pageInfo2 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 2).create()
    val page2 = pdfDocument.startPage(pageInfo2)
    val canvas2 = page2.canvas


    val countOfMetersForLen = ceil(roofParamsClassic4ScatState.len.toDouble()).toInt()
    val countOfMetersForHeight = ceil(roofParamsClassic4ScatState.hypotenuse.toDouble()).toInt()
    val countMetersForWidth = ceil(roofParamsClassic4ScatState.width.toDouble()).toInt()
    val shape = Shape(
        countFoolMetresHeight = countOfMetersForHeight.toFloat(),
        countFoolMetresLen = countOfMetersForLen.toFloat(),
        countSheetOnLen = roofParamsClassic4ScatState.countSheetOnLen,
        countSheetOnWidth = roofParamsClassic4ScatState.countSheetOnWidth,
        countFoolMetresWidth = countMetersForWidth.toFloat(),
        lenOfRoof = roofParamsClassic4ScatState.len.toFloat()
    )
    shape.ruler(canvas = canvas2)
    shape.ruler(canvas = canvas2, horizontal = false)
    shape.trapeze(
        canvas = canvas2,
        bigFoot = roofParamsClassic4ScatState.len.toFloat(),
        smallFoot = roofParamsClassic4ScatState.smallFoot.toFloat(),
        heightTrapeze = roofParamsClassic4ScatState.hypotenuse.toFloat(),
        yandova = roofParamsClassic4ScatState.yandova
    )
    shape.sheetOnTrapeze(
        canvas = canvas2,
        heightTrapeze = roofParamsClassic4ScatState.hypotenuse.toFloat(),
        bigFoot = roofParamsClassic4ScatState.len.toFloat(),
        smallFoot = roofParamsClassic4ScatState.smallFoot.toFloat(),
    )
    pdfDocument.finishPage(page2)
    val pageInfo1 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 1).create()
    val page1 = pdfDocument.startPage(pageInfo1)
    val canvas1 = page1.canvas

    shape.ruler(canvas = canvas1, isTrapeze = false, horizontal = false)
    shape.ruler(canvas = canvas1)
    shape.triangle(
        canvas = canvas1,
        hypotenuse = roofParamsClassic4ScatState.hypotenuse.toFloat(),
        widthRoof = roofParamsClassic4ScatState.width.toFloat(),
        yandova = roofParamsClassic4ScatState.yandova
    )
    shape.sheetOnTriangle(
        canvas = canvas1,
        widthRoof = roofParamsClassic4ScatState.width.toFloat(),
        hypotenuse = roofParamsClassic4ScatState.hypotenuse.toFloat()
    )


    pdfDocument.finishPage(page1)
    val pageInfo3 = PdfDocument.PageInfo.Builder(A4WIDTH, A4HEIGHT, 3).create()
    val page3 = pdfDocument.startPage(pageInfo3)
    val canvas3 = page3.canvas
    shape.infoPage(canvas = canvas3, roofParamsClassic4ScatState)



    pdfDocument.finishPage(page3)
    val file = File(context.getExternalFilesDir(null), "roof.pdf")


    file.outputStream().use { pdfDocument.writeTo(it) }
    pdfDocument.close()
    return file


}

class Shape(
    private val countFoolMetresLen: Float,
    private val countFoolMetresHeight: Float,
    private val countFoolMetresWidth: Float,
    private val widthPage: Float = A4WIDTH.toFloat(),
    private val heightPage: Float = A4HEIGHT.toFloat(),
    private val paddingXForVertical: Float = 0.05f,
    private val paddingYForVertical: Float = 0.05f,
    private val paddingXForHorizontal: Float = 0.05f,
    private val paddingYForHorizontal: Float = 0.05f,
    val lenOfRoof: Float,
    private val sheet: Sheet = Sheet(),
    val countSheetOnLen: Int,
    val countSheetOnWidth: Int,
) {
    private val meterInPixelLenRoof =
        (heightPage - heightPage * paddingYForVertical * 2) / countFoolMetresLen
    private val meterInPixelHeightRoof =
        (widthPage - widthPage * paddingXForHorizontal * 2) / countFoolMetresHeight
    private val meterInPixelWidthRoof =
        (heightPage - heightPage * paddingYForVertical * 2) / countFoolMetresWidth

    val allShape: MutableList<Double> = mutableListOf()


    fun ruler(
        canvas: Canvas,
        zero: Boolean = true,
        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f  // Толщина линии
            style = Paint.Style.STROKE
        },
        horizontal: Boolean = true,
        isTrapeze: Boolean = true,


        ) {


        if (!horizontal) {
            val x = widthPage * paddingXForVertical

            canvas.drawLine(
                x,
                heightPage * paddingYForVertical,
                x,
                heightPage * (1 - paddingYForVertical),
                paint
            )
            val countMetres = if (isTrapeze) countFoolMetresLen else countFoolMetresWidth
            for (i in 0..countMetres.toInt()) {
                val y =
                    i.toFloat() * (if (isTrapeze) meterInPixelLenRoof else meterInPixelWidthRoof) + (heightPage * paddingYForVertical)
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
            val y = heightPage * paddingYForHorizontal

            canvas.drawLine(
                widthPage * paddingXForHorizontal,
                y,
                widthPage * (1 - paddingXForHorizontal),
                y,
                paint
            )
            for (i in 0..countFoolMetresHeight.toInt()) {
                val x = i.toFloat() * meterInPixelHeightRoof + (widthPage * paddingXForHorizontal)
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

    fun trapeze(
        canvas: Canvas,
        paddingStartX: Float = widthPage * paddingXForVertical,
        paddingStartY: Float = heightPage * paddingYForVertical,
        bigFoot: Float,
        smallFoot: Float,
        heightTrapeze: Float,
        yandova: Double,

        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            alpha = 162// Толщина линии
            style = Paint.Style.STROKE
        },
    ) {
        val heightTrapezeInPixel = meterInPixelHeightRoof * heightTrapeze + paddingStartX
        val leftTopY = ((bigFoot - smallFoot) / 2 * meterInPixelLenRoof) + paddingStartY
        val rightTopY = (leftTopY + smallFoot * meterInPixelLenRoof)
        val rightBottomY = (bigFoot * meterInPixelLenRoof) + paddingStartY
        val path = android.graphics.Path().apply {
            moveTo(paddingStartX, paddingStartY)
            lineTo(heightTrapezeInPixel, leftTopY)
            lineTo(heightTrapezeInPixel, rightTopY)
            lineTo(paddingStartX, rightBottomY)
            close()
        }
        sqrt(heightTrapezeInPixel * heightTrapezeInPixel + leftTopY * leftTopY)
        val angleBigFoot =
            Math.toDegrees(atan((heightTrapezeInPixel - paddingStartX) / (leftTopY - paddingStartY)).toDouble())
                .toFloat()
        val angleBigFootForText = 90 - angleBigFoot


        canvas.drawPath(path, paint)


        val paintText = Paint().apply { textSize = 20f }



        canvas.rotate(90f, paddingStartX + 15, rightBottomY / 2)
        canvas.drawText("$bigFoot m", paddingStartX + 15, rightBottomY / 2, paintText)
        canvas.rotate(-90f, paddingStartX + 15, rightBottomY / 2)

        canvas.rotate(90f, heightTrapezeInPixel + 15, rightBottomY / 2)
        canvas.drawText("$smallFoot m", heightTrapezeInPixel + 15, rightBottomY / 2, paintText)
        canvas.rotate(-90f, heightTrapezeInPixel + 15, rightBottomY / 2)

        canvas.rotate(
            angleBigFootForText,
            heightTrapezeInPixel / 2 + paddingStartX,
            leftTopY / 2 + paddingStartY
        )
        canvas.drawText(
            "${yandova.toRound2Scale()} m",
            heightTrapezeInPixel / 2 + paddingStartX,
            leftTopY / 2 + paddingStartY - 20,
            paintText
        )
        canvas.rotate(
            -angleBigFootForText,
            heightTrapezeInPixel / 2 + paddingStartX,
            leftTopY / 2 + paddingStartY
        )

        canvas.rotate(
            180 - angleBigFootForText,
            heightTrapezeInPixel / 2 + paddingStartX,
            (rightBottomY - rightTopY) / 2 + rightTopY
        )
        canvas.drawText(
            "${yandova.toRound2Scale()} m",
            heightTrapezeInPixel / 2 + paddingStartX,
            (rightBottomY - rightTopY) / 2 + rightTopY,
            paintText
        )
        canvas.rotate(
            180 + angleBigFootForText,
            heightTrapezeInPixel / 2 + paddingStartX,
            (rightBottomY - rightTopY) / 2 + rightTopY
        )


    }

    fun drawSheet(
        canvas: Canvas,
        leftBottomX: Float = widthPage * paddingXForVertical,
        leftBottomY: Float = heightPage * paddingYForVertical,
        heightOfSheetInMetres: Double,
        leftTopX: Float,
        leftTopY: Float,
        rightTopX: Float = leftTopX,
        rightTopY: Float,
        rightBottomX: Float = leftBottomX,
        rightBottomY: Float = rightTopY,
        paintSheet: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 0.5f  // Толщина линии
            style = Paint.Style.STROKE
        },
        paintOverlap: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 0.5f  // Толщина линии
            style = Paint.Style.STROKE
//            alpha = (0.5 * 255).toInt()  // Прозрачность 0.5 (от 0 до 255)
            pathEffect =
                DashPathEffect(
                    floatArrayOf(10f, 20f),
                    0f
                )  // Пунктирная линия (10px линия, 20px пробел)
        }


    ) {
        val path = android.graphics.Path().apply {
            moveTo(leftBottomX, leftBottomY)
            lineTo(leftTopX, leftTopY)
            close()
        }

        canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, paintSheet)
        canvas.drawPath(path, paintSheet)
        canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, paintOverlap)

        val x = (leftTopX - leftBottomX) / 2 + leftBottomX / 2
        val y = (rightTopY - leftTopY) / 2 + leftTopY
        canvas.rotate(90f, x, y)
        canvas.drawText(String.format("%.2f", heightOfSheetInMetres), x, y, paintSheet)
        canvas.rotate(-90f, x, y)
    }

    fun sheetOnTrapeze(
        canvas: Canvas,
        heightTrapeze: Float,
        bigFoot: Float,
        smallFoot: Float,

        ) {
        val heightTrapezeInPixel =
            meterInPixelHeightRoof * heightTrapeze
        var leftBottomX = widthPage * paddingXForVertical
        var leftBottomY: Float = heightPage * paddingYForVertical
        val angleInRadians =
            atan(heightTrapezeInPixel / ((bigFoot - smallFoot) / 2 * meterInPixelLenRoof)).toDouble()
                .toFloat()
        for (numberOfSheet in 1..countSheetOnLen) {
            val leftTopY = leftBottomY

            val rightTopY = leftBottomY + (sheet.widthGeneral * meterInPixelLenRoof).toFloat()
            var leftTopX = 0f

            val dotYOfReverse =
                (bigFoot - smallFoot) / 2 * meterInPixelLenRoof + paddingYForVertical * heightPage + smallFoot * meterInPixelLenRoof

            leftTopX = if (dotYOfReverse > leftTopY) {
                ((rightTopY - paddingYForVertical * heightPage) * tan(angleInRadians)) + paddingXForVertical * widthPage
            } else {
                val left = (leftBottomY - paddingYForVertical * heightPage) / meterInPixelLenRoof
                val heigX =
                    (lenOfRoof - left) * tan(angleInRadians - PI) * meterInPixelLenRoof / meterInPixelHeightRoof
                (heigX * meterInPixelHeightRoof + paddingXForVertical * widthPage).toFloat()


            }
            val heightOfSheetInMetresFoolValue = min(
                ((leftTopX - leftBottomX) / meterInPixelHeightRoof).toDouble(),
                heightTrapeze.toDouble()
            )
            val heightOfSheetInMetres = ceil(
                String.format("%.2f", heightOfSheetInMetresFoolValue).replace(",", ".")
                    .toDouble() / sheet.multiplicity
            ) * sheet.multiplicity
            allShape.add(heightOfSheetInMetres)


            drawSheet(
                canvas = canvas,
                leftBottomX = leftBottomX,
                leftBottomY = leftBottomY,
                leftTopX = min(leftTopX, heightTrapezeInPixel + widthPage * paddingXForVertical),
                leftTopY = leftBottomY,
                rightTopY = rightTopY,
                heightOfSheetInMetres = heightOfSheetInMetres

            )

            leftBottomY =
                ((numberOfSheet) * sheet.visible * meterInPixelLenRoof + (paddingYForVertical * heightPage)).toFloat()
        }
    }

    fun triangle(
        canvas: Canvas,
        paint: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            alpha = 162// Толщина линии
            style = Paint.Style.STROKE
        },
        widthRoof: Float,
        hypotenuse: Float,
        yandova: Double,
    ) {

        val path = android.graphics.Path().apply {
            moveTo(paddingXForVertical * widthPage, paddingYForHorizontal * heightPage)
            lineTo(
                hypotenuse * meterInPixelHeightRoof + paddingXForVertical * widthPage,
                widthRoof / 2 * meterInPixelWidthRoof + paddingYForVertical * heightPage
            )
            lineTo(
                paddingXForVertical * widthPage,
                widthRoof * meterInPixelWidthRoof + paddingYForVertical * heightPage
            )
        }
        canvas.drawPath(path, paint)
        val angle = 90 - Math.toDegrees(atan(hypotenuse / (widthRoof / 2)).toDouble()).toFloat()
        val paintText = Paint().apply { textSize = 20f }



        canvas.rotate(
            90f,
            paddingXForVertical * widthPage + 15,
            widthRoof / 2 * meterInPixelWidthRoof + paddingYForVertical * heightPage
        )
        canvas.drawText(
            "$widthRoof m",
            paddingXForVertical * widthPage + 15,
            widthRoof / 2 * meterInPixelWidthRoof + paddingYForVertical * heightPage,
            paintText
        )
        canvas.rotate(
            -90f,
            paddingXForVertical * widthPage + 15,
            widthRoof / 2 * meterInPixelWidthRoof + paddingYForVertical * heightPage
        )

        canvas.rotate(
            angle,
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            (widthRoof / 4 * meterInPixelWidthRoof) + paddingYForVertical * heightPage
        )
        canvas.drawText(
            "${yandova.toRound2Scale()} m",
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            (widthRoof / 4 * meterInPixelWidthRoof) + paddingYForVertical * heightPage,
            paintText
        )
        canvas.rotate(
            -angle,
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            (widthRoof / 4 * meterInPixelWidthRoof) + paddingYForVertical * heightPage
        )

        canvas.rotate(
            180 - angle,
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            ((widthRoof * 0.75).toFloat() * meterInPixelWidthRoof) + paddingYForVertical * heightPage
        )
        canvas.drawText(
            "${yandova.toRound2Scale()} m",
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            ((widthRoof * 0.75).toFloat() * meterInPixelWidthRoof) + paddingYForVertical * heightPage,
            paintText
        )
        canvas.rotate(
            180 + angle,
            (hypotenuse * meterInPixelHeightRoof / 2) + paddingXForVertical * widthPage + 10f,
            ((widthRoof * 0.75).toFloat() * meterInPixelWidthRoof) + paddingYForVertical * heightPage
        )


    }

    fun sheetOnTriangle(
        canvas: Canvas,
        widthRoof: Float,
        hypotenuse: Float,

        ) {
        val angleInRadians = atan(hypotenuse / (widthRoof / 2))
        var leftTopX = 0f
        var leftBottomY = paddingYForVertical * heightPage
        val dotOfReverse = widthRoof / 2 * meterInPixelWidthRoof + paddingYForVertical * heightPage

        for (numberOfSheet in 1..countSheetOnWidth) {
            leftTopX =
                if (leftBottomY < dotOfReverse) {
                    ((sheet.visible * numberOfSheet + sheet.overlap) * tan(angleInRadians) * meterInPixelHeightRoof + paddingXForVertical * widthPage).toFloat()
                } else {
                    val left =
                        (leftBottomY - paddingYForVertical * heightPage) / meterInPixelWidthRoof
                    val heigX =
                        (widthRoof - left) * tan(angleInRadians - PI)
                    (heigX * meterInPixelHeightRoof + paddingXForVertical * widthPage).toFloat()
                }

            val heightOfSheetInMetresFoolValue = min(
                ((leftTopX - paddingXForVertical * widthPage) / meterInPixelHeightRoof),
                hypotenuse
            )
            val heightOfSheetInMetres = ceil(
                String.format("%.2f", heightOfSheetInMetresFoolValue).replace(",", ".")
                    .toDouble() / sheet.multiplicity
            ) * sheet.multiplicity
            allShape.add(heightOfSheetInMetres)
            drawSheet(
                canvas = canvas,
                leftBottomY = leftBottomY,
                leftTopX = min(
                    leftTopX,
                    hypotenuse * meterInPixelHeightRoof + paddingXForVertical * widthPage
                ),
                leftTopY = leftBottomY,
                rightTopY = leftBottomY + (sheet.widthGeneral * meterInPixelWidthRoof).toFloat(),
                heightOfSheetInMetres = heightOfSheetInMetres
            )
            leftBottomY += (sheet.visible * meterInPixelWidthRoof).toFloat()

        }

    }

    fun infoPage(
        canvas: Canvas,
        roofParamsClassic4ScatState: RoofParamsClassic4ScatState
    ) {

        val x = paddingXForVertical * widthPage
        val startY = paddingYForVertical * heightPage
        val textTrasfer = AddVerticalPaddingForLineText(startY)
        val paintText = TextPaint().apply {
            textSize = 20f
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        }
        val orderSheet = allShape.groupingBy { it }.eachCount().toSortedMap()

        canvas.drawText("Длина крыши ${roofParamsClassic4ScatState.len} м", x, startY, paintText)
        canvas.drawText(
            "Ширина крыши ${roofParamsClassic4ScatState.width} м",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
        canvas.drawText(
            "Покат ${roofParamsClassic4ScatState.hypotenuse} м",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
        canvas.drawText(
            "Яндовая ${roofParamsClassic4ScatState.yandova} м",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
        canvas.drawText(
            "Высота крыши ${roofParamsClassic4ScatState.height} м",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
        canvas.drawText(
            "Угол наклона поката ${roofParamsClassic4ScatState.angle} м",
            x,
            textTrasfer.addTransferText(),
            paintText
        )
        canvas.drawText("Листы", x, textTrasfer.addTransferText(40f), paintText)

        orderSheet.forEach { (k, v) ->
            canvas.drawText(
                "${k.toRound2Scale()} = ${v * 2}",
                x,
                textTrasfer.addTransferText(),
                paintText
            )
        }
    }
}


fun main() {


}