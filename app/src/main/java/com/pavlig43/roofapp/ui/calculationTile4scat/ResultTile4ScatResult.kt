package com.pavlig43.roofapp.ui.calculationTile4scat

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.mathbigdecimal.OffsetBD
import com.pavlig43.roof_app.R
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
import java.math.RoundingMode

suspend fun pdfResultRoof4Scat(
    roofParamsClassic4Scat: RoofParamsClassic4Scat,
    pdfDocument: PdfDocument,
    context: Context,
): File {
    val geometryTriangle3SideShape =
        GeometryTriangle3SideShape(
            leftBottom = Dot(DotNameTriangle3Side.LEFTBOTTOM),
            top =
            Dot(
                DotNameTriangle3Side.TOP,
                offset =
                OffsetBD(
                    roofParamsClassic4Scat.pokat.value,
                    roofParamsClassic4Scat.width.value.divide(
                        BigDecimal(2),

                        RoundingMode.HALF_UP,
                    ),
                ),
            ),
            rightBottom =
            Dot(
                DotNameTriangle3Side.RIGHTBOTTOM,
                OffsetBD(BigDecimal.ZERO, roofParamsClassic4Scat.width.value),
            ),
        )
    val geometry4SideShape =
        Geometry4SideShape(
            leftBottom = Dot(DotName4Side.LEFTBOTTOM),
            leftTop =
            Dot(
                DotName4Side.LEFTTOP,
                OffsetBD(
                    roofParamsClassic4Scat.pokat.value,
                    (roofParamsClassic4Scat.len.value - roofParamsClassic4Scat.smallFoot).divide(
                        BigDecimal(2),

                        RoundingMode.HALF_UP,
                    ),
                ),
            ),
            rightTop =

            Dot(
                DotName4Side.RIGHTBOTTOM,
                OffsetBD(
                    roofParamsClassic4Scat.pokat.value,
                    (roofParamsClassic4Scat.len.value - roofParamsClassic4Scat.smallFoot).divide(
                        BigDecimal(2),

                        RoundingMode.HALF_UP,
                    ) + roofParamsClassic4Scat.smallFoot,
                ),
            ),
            rightBottom =
            Dot(
                DotName4Side.RIGHTBOTTOM,
                offset =
                OffsetBD(
                    BigDecimal.ZERO,
                    roofParamsClassic4Scat.len.value,
                ),
            ),
        )

    fun getOtherParams(): List<Pair<String, String>> =
        listOf(
            Pair(
                context.getString(R.string.len_roof),
                "${roofParamsClassic4Scat.width.value} ${context.getString(R.string.cm)}",
            ),
            Pair(
                context.getString(R.string.width_roof),
                "${roofParamsClassic4Scat.len.value} ${context.getString(R.string.cm)}",
            ),
            Pair(
                context.getString(R.string.yandova),
                "${roofParamsClassic4Scat.yandova.toInt()} ${context.getString(R.string.cm)}",
            ),
            Pair(
                context.getString(R.string.height_roof),
                "${roofParamsClassic4Scat.height.value} ${context.getString(R.string.cm)}",
            ),
            Pair(
                context.getString(R.string.angle_tilt),
                "${roofParamsClassic4Scat.angle.value} °",
            ),
        )

    val countSheet4SideShape: MutableList<Sheet> =
        pdfDocument.pdfResult4Side(
            context,
            geometry4SideShape,
            pageNumber = 1,
            sheet = roofParamsClassic4Scat.sheet,
        )
    val countSheet3SideTriangle: MutableList<Sheet> =
        pdfDocument.pdfResult3SideTriangle(
            context,
            geometryTriangle3SideShape,
            pageNumber = 2,
            sheet = roofParamsClassic4Scat.sheet,
        )
    pdfDocument.addInfo(
        context,
        listOfSheet = countSheet3SideTriangle + countSheet4SideShape,
        fullRoof = true,
        pageNumber = 3,
        otherParams = getOtherParams(),
    )
    val file = pdfDocument.createFile(context)
    return file
}
