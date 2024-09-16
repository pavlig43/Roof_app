package com.pavlig43.roof_app.ui.calculation_tile_4scat

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.model.DotNameTriangle3Side

import com.pavlig43.roof_app.model.RoofParamsClassic4ScatState
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.ui.shapes.quadrilateral.Geometry4SideShape
import com.pavlig43.roof_app.ui.shapes.triangle.GeometryTriangle3SideShape
import com.pavlig43.roof_app.utils.addInfo
import com.pavlig43.roof_app.utils.createFile
import com.pavlig43.roof_app.utils.pdfResult3SideTriangle
import com.pavlig43.roof_app.utils.pdfResult4Side
import java.io.File


fun  pdfResultRoof4Scat(
    roofParamsClassic4ScatState: RoofParamsClassic4ScatState,
    pdfDocument: PdfDocument,
    context: Context,
): File {
    val geometryTriangle3SideShape = GeometryTriangle3SideShape(
        leftBottom = Dot(DotNameTriangle3Side.LEFTBOTTOM, distanceX = 0f, distanceY = 0f),
        top = Dot(
            DotNameTriangle3Side.TOP,
            distanceX = roofParamsClassic4ScatState.hypotenuse,
            distanceY = roofParamsClassic4ScatState.width/2
        ),
        rightBottom = Dot(
            DotNameTriangle3Side.RIGHTBOTTOM,
            distanceX = 0f,
            distanceY = roofParamsClassic4ScatState.width
        )
    )
    val geometry4SideShape = Geometry4SideShape(
        leftBottom = Dot(DotName4Side.LEFTBOTTOM, distanceX = 0f, distanceY = 0f),
        leftTop = Dot(
            DotName4Side.LEFTTOP,
            distanceX = roofParamsClassic4ScatState.hypotenuse,
            distanceY = (roofParamsClassic4ScatState.len - roofParamsClassic4ScatState.smallFoot) / 2
        ),
        rightTop = Dot(
            DotName4Side.RIGHTBOTTOM,
            distanceX = roofParamsClassic4ScatState.hypotenuse,
            distanceY = (roofParamsClassic4ScatState.len - roofParamsClassic4ScatState.smallFoot) / 2 + roofParamsClassic4ScatState.smallFoot
        ),
        rightBottom = Dot(
            DotName4Side.RIGHTBOTTOM,
            distanceX = 0f,
            distanceY = roofParamsClassic4ScatState.len,
        )
    )
    fun getOtherParams(): List<Pair<String, String>> {
        return listOf(
            Pair("Ширина крыши","${roofParamsClassic4ScatState.width.toInt()} cm"),
            Pair("Длина крыши","${roofParamsClassic4ScatState.len.toInt()} cm"),
            Pair("Яндова","${roofParamsClassic4ScatState.yandova.toInt()} cm"),
            Pair("Высота крыши","${roofParamsClassic4ScatState.height.toInt()} cm"),
            Pair("Угол наклона","${roofParamsClassic4ScatState.angle.toInt()}°"),
            )
    }

    val countSheet4SideShape: MutableList<Sheet> = pdfDocument.pdfResult4Side(geometry4SideShape, pageNumber = 1, sheet = roofParamsClassic4ScatState.sheet)
    val countSheet3SideTriangle: MutableList<Sheet> = pdfDocument.pdfResult3SideTriangle(geometryTriangle3SideShape, pageNumber = 2, sheet = roofParamsClassic4ScatState.sheet)
    pdfDocument.addInfo(listOfSheet =  countSheet3SideTriangle+countSheet4SideShape , fullRoof = true, pageNumber = 3, otherParams = getOtherParams())
    val file = pdfDocument.createFile(context)
    return file


}


fun main() {


}