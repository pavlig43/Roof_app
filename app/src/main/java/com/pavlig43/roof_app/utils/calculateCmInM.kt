package com.pavlig43.roof_app.utils

fun calculateCmInM(newValue:String): Double {
    return newValue.toDoubleOrNull()
        ?.div(100)
        ?.let {it1-> String.format("%.3f", it1)
            .replace(",",".").toDouble() }
        ?:0.0
}