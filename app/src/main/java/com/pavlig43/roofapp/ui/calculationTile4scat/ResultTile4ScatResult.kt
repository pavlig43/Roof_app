package com.pavlig43.roofapp.ui.calculationTile4scat

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.pavlig43.roofapp.model.Dot
import com.pavlig43.roofapp.model.DotName4Side
import com.pavlig43.roofapp.model.DotNameTriangle3Side
import com.pavlig43.roofapp.model.RoofParamsClassic4Scat
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.ui.shapes.quadrilateral.Geometry4SideShape
import com.pavlig43.roofapp.ui.shapes.triangle.GeometryTriangle3SideShape
import com.pavlig43.roofapp.utils.addInfo
import com.pavlig43.roofapp.utils.createFile
import com.pavlig43.roofapp.utils.pdfResult3SideTriangle
import com.pavlig43.roofapp.utils.pdfResult4Side
import java.io.File
import java.math.BigDecimal

fun pdfResultRoof4Scat(
    roofParamsClassic4Scat: RoofParamsClassic4Scat,
    pdfDocument: PdfDocument,
    context: Context,
): File {
    val geometryTriangle3SideShape =
        GeometryTriangle3SideShape(
            leftBottom = Dot(DotNameTriangle3Side.LEFTBOTTOM, distanceX = 0f, distanceY = 0f),
            top =
                Dot(
                    DotNameTriangle3Side.TOP,
                    distanceX = roofParamsClassic4Scat.hypotenuse.toFloat(),
                    distanceY = (roofParamsClassic4Scat.width.divide(BigDecimal(2))).toFloat(),
                ),
            rightBottom =
                Dot(
                    DotNameTriangle3Side.RIGHTBOTTOM,
                    distanceX = 0f,
                    distanceY = roofParamsClassic4Scat.width.toFloat(),
                ),
        )
    val geometry4SideShape =
        Geometry4SideShape(
            leftBottom = Dot(DotName4Side.LEFTBOTTOM, distanceX = 0f, distanceY = 0f),
            leftTop =
                Dot(
                    DotName4Side.LEFTTOP,
                    distanceX = roofParamsClassic4Scat.hypotenuse.toFloat(),
                    distanceY = (roofParamsClassic4Scat.len.toFloat() - roofParamsClassic4Scat.smallFoot.toFloat()) / 2,
                ),
            rightTop =
                Dot(
                    DotName4Side.RIGHTBOTTOM,
                    distanceX = roofParamsClassic4Scat.hypotenuse.toFloat(),
                    distanceY = (roofParamsClassic4Scat.len.toFloat() - roofParamsClassic4Scat.smallFoot.toFloat()) / 2 + roofParamsClassic4Scat.smallFoot.toFloat(),
                ),
            rightBottom =
                Dot(
                    DotName4Side.RIGHTBOTTOM,
                    distanceX = 0f,
                    distanceY = roofParamsClassic4Scat.len.toFloat(),
                ),
        )

    fun getOtherParams(): List<Pair<String, String>> {
        return listOf(
            Pair("Ширина крыши", "${roofParamsClassic4Scat.width.toInt()} cm"),
            Pair("Длина крыши", "${roofParamsClassic4Scat.len.toInt()} cm"),
            Pair("Яндова", "${roofParamsClassic4Scat.yandova.toInt()} cm"),
            Pair("Высота крыши", "${roofParamsClassic4Scat.height.toInt()} cm"),
            Pair("Угол наклона", "${roofParamsClassic4Scat.angle.toInt()}°"),
        )
    }

    val countSheet4SideShape: MutableList<Sheet> =
        pdfDocument.pdfResult4Side(
            geometry4SideShape,
            pageNumber = 1,
            sheet = roofParamsClassic4Scat.sheet,
        )
    val countSheet3SideTriangle: MutableList<Sheet> =
        pdfDocument.pdfResult3SideTriangle(
            geometryTriangle3SideShape,
            pageNumber = 2,
            sheet = roofParamsClassic4Scat.sheet,
        )
    pdfDocument.addInfo(
        listOfSheet = countSheet3SideTriangle + countSheet4SideShape,
        fullRoof = true,
        pageNumber = 3,
        otherParams = getOtherParams(),
    )
    val file = pdfDocument.createFile(context)
    return file
}

