package com.pavlig43.roofapp.data.shapeMulti

import android.util.Log
import com.pavlig43.mathbigdecimal.shapes.shapeForDraw.CoordinateShape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShapeMultiProvider {

    private val _listCoordinateShape: MutableStateFlow<List<CoordinateShape>> =
        MutableStateFlow(listOf())
    val listCoordinateShape = _listCoordinateShape.asStateFlow()

    fun addShape(coordinateShape: CoordinateShape) {
        val updatedList = _listCoordinateShape.value.toMutableList() + (coordinateShape)
        _listCoordinateShape.update { updatedList }
        Log.d("ShapeMultiProvider", " $_listCoordinateShape")
    }

    fun clear() {
        _listCoordinateShape.update { listOf() }
    }

    fun removeLast() {
        val updatedList = _listCoordinateShape.value.dropLast(1)
        _listCoordinateShape.update { updatedList }
    }
}
