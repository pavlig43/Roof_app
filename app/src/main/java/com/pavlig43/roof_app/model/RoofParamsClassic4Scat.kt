package com.pavlig43.roof_app.model


import java.lang.Math.toRadians
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * класс для орределения 4хскатной крыши
 */
data class RoofParamsClassic4ScatState(
    val width: Float = 0f,
    val len: Float = 0f,
    val angle: Float = 0f,// угол наклона листа
    val height: Float = 0f,
    val hypotenuse: Float = 0f, //Покат
    val sheet: Sheet = Sheet()
    )
{
    val yandova:Float by lazy {
        try {
            val foot = width /2
            sqrt(hypotenuse * hypotenuse + foot*foot )
        }
        catch (e:Exception){
            0f
        }

    }

    /**
     * Длина конька - верхнего основания трапеции
     */
    val smallFoot:Float by lazy {
        try {
        round((len - 2* sqrt(yandova*yandova - hypotenuse * hypotenuse))*100)/100
        }
        catch (e:Exception){
            0f
        }
    }

}

/**
 * Пересчитывается высота,покат исходя из угла наклона
 */
fun RoofParamsClassic4ScatState.calculateFromAngle(angle: Float?): RoofParamsClassic4ScatState {
    return when{
        angle == 0f->this.copy(height = 0f, angle = 0f, hypotenuse = 0f)
        angle !=null ->{
            val adjacent = width.toDouble() / 2 // длина прилежащего катета
            val angleInRadians = toRadians(angle.toDouble())
            val height = (adjacent * tan(angleInRadians)).toFloat()
            val hypotenuse = (adjacent / cos(angleInRadians)).toFloat()
            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
        else-> this
    }


}

/**
 * Пересчитывается угол и покат исходя из высоты крыши
 */
fun RoofParamsClassic4ScatState.calculateFromHeight(newHeight: Float?): RoofParamsClassic4ScatState {
    return when {
        newHeight == 0f -> this.copy(height = 0f, angle = 0f, hypotenuse = 0f)
        newHeight != null -> {
            val adjacent = width.toDouble() / 2
            val hypotenuse =
                (sqrt(adjacent * adjacent + height * height)).toFloat()


            val angleInRadians = atan(height/ adjacent)

            val angle = Math.toDegrees(angleInRadians).toFloat()
            this.copy(height = height, angle = angle, hypotenuse = hypotenuse)
        }

        else -> this
    }

}

/**
 * Пересчитывает угол наклона и высоту исходя из поката
 */
fun RoofParamsClassic4ScatState.calculateFromHypotenuse(hypotenuse: Float): RoofParamsClassic4ScatState {

    return  when{
        hypotenuse == 0f->this.copy(height = 0f, angle = 0f, hypotenuse = 0f)
        hypotenuse!= null->{
            val adjacent = width.toDouble() / 2 // прилежащий катет
            val angleInRadians = acos(adjacent / hypotenuse)
            val angle = Math.toDegrees(angleInRadians).toFloat()
            val height =
                (sqrt(hypotenuse * hypotenuse - adjacent * adjacent)).toFloat()

            this.copy(angle = angle, height = height, hypotenuse = hypotenuse)
        }
        else->this
    }


}




fun main() {
    println(ceil(3.25))
}




