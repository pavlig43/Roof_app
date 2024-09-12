package com.pavlig43.roof_app.utils


/**
 * Переводит строку(подразумевая сантиметры) , полученную в TextField в метры,
 * поставил 3 знака после запятой (Излишество, но может быть влияет на точность расчетов)
 * TODO() Нужно убрать эту функцию и все расчеты и взаимодействия с пользователем вести в см
 */
fun calculateCmInM(newValue:String): Double {
    return newValue.toDoubleOrNull()
        ?.div(100)
        ?.let {it1-> String.format("%.3f", it1)
            .replace(",",".").toDouble() }
        ?:0.0
}