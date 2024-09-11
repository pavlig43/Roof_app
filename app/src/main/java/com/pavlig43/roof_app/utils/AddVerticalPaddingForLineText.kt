package com.pavlig43.roof_app.utils

class AddVerticalPaddingForLineText(private val startPaddingY:Float){
    var y = startPaddingY
        private set

    fun addTransferText(spaceLine:Float = 25f): Float {
        y+=spaceLine
        return y
    }
}

fun main() {
    val list = listOf("apple", "banana", "apple", "orange", "banana", "banana")
    println(list.groupingBy { it }.eachCount())
}