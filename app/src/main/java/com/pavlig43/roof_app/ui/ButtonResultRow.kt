package com.pavlig43.roof_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Получть результат - должна выводить на экран ПДФ документ
 * Параметры листа - открывать диалог для изменения параметров листа железа
 * Эта функция должна быть активна, когда выбранная фигура пользователем может существавать по законам геометрии и законам , заданным тобой
 */
@Composable
fun ButtonResultRow(
    getResult:()->Unit,
    openSheetParams:()->Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = openSheetParams) {
            Text(text = "Параметры листа")
        }
        Button(onClick = getResult) {
            Text(text = "Получить результат")
        }
    }

}