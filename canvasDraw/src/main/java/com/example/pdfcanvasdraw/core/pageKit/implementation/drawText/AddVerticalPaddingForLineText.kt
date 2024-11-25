package com.example.pdfcanvasdraw.core.pageKit.implementation.drawText

/**
 * класс для написания текста в ПДФ файле, основная цель добавлять перенос строки на выбранное количество пикселей
 * - получение  координат для новой строки
 * TODO() нужно заменить этот класс на что-то другое
 */
class AddVerticalPaddingForLineText(startPaddingY: Float) {
    var y = startPaddingY
        private set

    fun addTransferText(spaceLine: Float = 25f): Float {
        y += spaceLine
        return y
    }
}
