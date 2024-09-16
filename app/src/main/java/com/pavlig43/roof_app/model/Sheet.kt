package com.pavlig43.roof_app.model

import kotlin.math.ceil
import kotlin.math.sqrt


/**
 * Лист железа
 */
data class Sheet(
    /**
     * Вид покрытия -череица или профиль
     */
    val profile: RoofMetal = RoofMetal.TILE,
    val widthGeneral: Float = 118f,
    /**
     * Перехлест при раскладке
     */
    val overlap:Float = 8f,


    /**
     *"*Кратность - округление расчетной длины листа в большую сторону. " +
     *                 "Например при длине листа 243 см и кратности 5 см результат будет 245 см"
     */
    val multiplicity:Float = 5f,
    private val len:Int = 0
){
    /**
     * оКругленная длина листа с учетом кратности [multiplicity]
     */
    val ceilLen:Int by lazy {
        if (multiplicity==0f) len else (ceil(len/multiplicity)*multiplicity).toInt()
    }
    /**
     * Видимая часть листа
     */
    val visible:Float by lazy {
        widthGeneral-overlap
    }
}


fun Sheet.updateWidthGeneral(newWidthGeneral:Float): Sheet {
    return this.copy(widthGeneral=newWidthGeneral)
}
fun Sheet.updateOverlap(newOverlap:Float): Sheet {
    return this.copy(overlap = newOverlap)
}
fun Sheet.updateMultiplicity(newMultiplicity:Float): Sheet {
    return this.copy(multiplicity = newMultiplicity)
}





enum class RoofMetal {
    TILE,
    PROFILE
}

fun main() {
    val s = Sheet(len = 243, multiplicity = 2f)
    println(s.ceilLen)
    println(0f/0f)
    println(5/0f)
}
