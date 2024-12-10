package com.pavlig43.roofapp.data.shapeMulti

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.pavlig43.mathbigdecimal.shapes.CoordinateShape
import kotlinx.coroutines.flow.Flow

class ShapeMultiProvider {

    private val _listCoordinateShape: SnapshotStateList<CoordinateShape> =
        mutableStateListOf<CoordinateShape>()
    val listCoordinateShape: Flow<List<CoordinateShape>> =
        snapshotFlow { _listCoordinateShape.toList() }

    fun addShape(coordinateShape: CoordinateShape) {
        _listCoordinateShape.add(coordinateShape)
        Log.d("ShapeMultiProvider", " $_listCoordinateShape")
    }

    fun clear() {
        _listCoordinateShape.clear()
    }
}
