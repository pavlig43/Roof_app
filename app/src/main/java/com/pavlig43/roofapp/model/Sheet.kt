package com.pavlig43.roofapp.model

import com.pavlig43.roof_app.R
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Лист железа
 */
data class Sheet(
    /**
     * Вид покрытия -череица или профиль
     */
    val profile: RoofMetal = RoofMetal.TILE,
    val widthGeneral: SheetParam = SheetParam(SheetParamName.WIDTH_GENERAL, BigDecimal("118")),
    /**
     * Перехлест при раскладке
     */
    val overlap: SheetParam = SheetParam(SheetParamName.OVERLAP, BigDecimal("8")),
    /**
     *"*Кратность - округление расчетной длины листа в большую сторону. " +
     *                 "Например при длине листа 243 см и кратности 5 см результат будет 245 см"
     */
    val multiplicity: SheetParam = SheetParam(SheetParamName.MULTIPLICITY, BigDecimal("5")),
    private val len: BigDecimal = BigDecimal.ZERO,
) {
    /**
     * оКругленная длина листа с учетом кратности [multiplicity]
     */
    val ceilLen: BigDecimal by lazy {

        if (multiplicity.value == BigDecimal.ZERO) {
            len
        } else {
            len
                .divide(
                    multiplicity.value,
                    0,
                    RoundingMode.CEILING,
                ).multiply(multiplicity.value)
        }
    }

    /**
     * Видимая часть листа
     */
    val visible: BigDecimal by lazy {
        widthGeneral.value - overlap.value
    }
}

enum class SheetParamName(
    val title: Int,
) {
    WIDTH_GENERAL(R.string.sheet_width),
    OVERLAP(R.string.overlap),
    MULTIPLICITY(R.string.multiplicity),
}

data class SheetParam(
    val name: SheetParamName,
    val value: BigDecimal,
    val unit: UnitOfMeasurement = UnitOfMeasurement.CM,
)

fun Sheet.updateSheetParams(sheetParam: SheetParam): Sheet =
    when (sheetParam.name) {
        SheetParamName.WIDTH_GENERAL -> this.updateWidthGeneral(sheetParam)
        SheetParamName.OVERLAP -> this.updateOverlap(sheetParam)
        SheetParamName.MULTIPLICITY -> this.updateMultiplicity(sheetParam)
    }

private fun Sheet.updateWidthGeneral(newWidthGeneral: SheetParam): Sheet =
    this.copy(
        widthGeneral = newWidthGeneral,
    )

private fun Sheet.updateOverlap(newOverlap: SheetParam): Sheet = this.copy(overlap = newOverlap)

private fun Sheet.updateMultiplicity(newMultiplicity: SheetParam): Sheet =
    this.copy(
        multiplicity = newMultiplicity,
    )

enum class RoofMetal {
    TILE,
}
