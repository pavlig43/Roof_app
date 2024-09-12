package com.pavlig43.roof_app.utils


/**
 * класс для написания текста в ПДФ файле, основная цель добавлять перенос строки на выбранное количество пикселей
 * - получение  координат для новой строки
 * TODO() нужно заменить этот класс на что-то другое
 */
class AddVerticalPaddingForLineText(private val startPaddingY:Float){
    var y = startPaddingY
        private set

    fun addTransferText(spaceLine:Float = 25f): Float {
        y+=spaceLine
        return y
    }
}

