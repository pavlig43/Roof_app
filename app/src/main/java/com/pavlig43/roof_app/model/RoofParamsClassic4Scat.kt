package com.pavlig43.roof_app.model


import java.lang.Math.toRadians
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.math.tan

data class RoofParamsClassic4ScatState(
    val width: String = "",
    val len: String = "",
    val angle: String = "",
    val height: String = "",
    val hypotenuse: String = "",
    val sheet: Sheet = Sheet()
    )
{
    val yandova:Double by lazy {
        try {
            val foot = width.toDouble()/2
            sqrt(hypotenuse.toDouble()*hypotenuse.toDouble() + foot*foot ).toRound2Scale()
        }
        catch (e:Exception){
            0.0
        }

    }

    val smallFoot:Double by lazy {
        try {
        round((len.toDouble() - 2* sqrt(yandova*yandova - hypotenuse.toDouble()*hypotenuse.toDouble()))*100)/100
        }
        catch (e:Exception){
            0.0
        }
    }

    val countSheetOnLen:Int by lazy {
        try {
            ceil((len.toDouble() - sheet.overlap)/(sheet.visible)).toInt()
        }
        catch (e:Exception){
            0
        }

    }

    val countSheetOnWidth:Int by lazy {
        try {
            ceil((width.toDouble() - sheet.overlap)/(sheet.visible)).toInt()
        }
        catch (e:Exception){
            0
        }

    }
}
fun RoofParamsClassic4ScatState.calculateFromAngle(angle: String): RoofParamsClassic4ScatState {
    return when{
        angle.isBlank()->this.copy(height = "", angle = "", hypotenuse = "")
        angle.toDoubleOrNull() !=null ->{
            val adjacent = width.toDouble() / 2 // длина прилежащего катета
            val angleInRadians = toRadians(angle.toDouble())
            val height = (adjacent * tan(angleInRadians)).toRound2Scale().toString()
            val hypotenuse = (adjacent / cos(angleInRadians)).toRound2Scale().toString()
            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
        else-> this
    }


}

fun RoofParamsClassic4ScatState.calculateFromHeight(newHeight: String): RoofParamsClassic4ScatState {
    return when {
        newHeight.isBlank() -> this.copy(height = "", angle = "", hypotenuse = "")
        newHeight.toDoubleOrNull() != null -> {
            val adjacent = width.toDouble() / 2
            val hypotenuse =
                (sqrt(adjacent * adjacent + height.toDouble() * height.toDouble())).toRound2Scale()
                    .toString()

            val angleInRadians = atan(height.toDouble() / adjacent)

            val angle = Math.toDegrees(angleInRadians).toRound2Scale().toString()
            this.copy(height = height, angle = angle, hypotenuse = hypotenuse)
        }

        else -> this
    }

}

fun RoofParamsClassic4ScatState.calculateFromHypotenuse(hypotenuse: String): RoofParamsClassic4ScatState {

    return  when{
        hypotenuse.isBlank()->this.copy(height = "", angle = "", hypotenuse = "")
        hypotenuse.toDoubleOrNull() != null->{
            val adjacent = width.toDouble() / 2 // прилежащий катет
            val angleInRadians = acos(adjacent / hypotenuse.toDouble())
            val angle = Math.toDegrees(angleInRadians).toRound2Scale().toString()
            val height =
                (sqrt(hypotenuse.toDouble() * hypotenuse.toDouble() - adjacent * adjacent)).toRound2Scale()
                    .toString()
            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
        else->this
    }


}


fun Double.toRound2Scale(): Double {
    return try {
        String.format("%.2f", this).replace(',', '.').toDouble()
    } catch (e: Exception) {
        this
    }
}
fun main() {
    println(ceil(3.25))
}




