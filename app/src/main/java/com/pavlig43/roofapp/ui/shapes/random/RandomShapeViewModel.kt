package com.pavlig43.roofapp.ui.shapes.random

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathbigdecimal.OffsetBD
import com.example.mathbigdecimal.shapes.CoordinateShape
import com.pavlig43.roofapp.di.DocType
import com.pavlig43.roofapp.di.DocTypeBuilder
import com.pavlig43.roofapp.domain.TileReportUseCase
import com.pavlig43.roofapp.model.Sheet
import com.pavlig43.roofapp.model.SheetParam
import com.pavlig43.roofapp.model.updateSheetParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class RandomShapeViewModel
@Inject constructor(
    @DocTypeBuilder(DocType.AndroidPdf) private val tileReportUseCase: TileReportUseCase
) : ViewModel() {
    private val _randomShapeState =
        MutableStateFlow<RandomShapeState>(RandomShapeState.ConstructorShape)
    val randomShapeState = _randomShapeState.asStateFlow()

    private val listOffset = SnapshotStateList<OffsetBD>().apply {
        add(OffsetBD.Zero)
    }

    val coordinateShape = snapshotFlow { listOffset.toList() }.map {
        CoordinateShape(it, true)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CoordinateShape(listOffset))

    fun addDot(offsetBD: OffsetBD) {
        if (!listOffset.contains(offsetBD)) {
            listOffset.add(offsetBD)
        }
    }

    val isConvex = snapshotFlow { listOffset.toList() }.map {
        CoordinateShape(it, true).isConvex
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun checkOnProximity(offsetBD: OffsetBD): Boolean {
        return true
    }

    private val _sheet = MutableStateFlow(Sheet())
    val sheet = _sheet.asStateFlow()

    fun moveToManualDialog() {
        _randomShapeState.update { RandomShapeState.ManualDialog }
    }

    fun moveToSheetDialog() {
        _randomShapeState.update { RandomShapeState.SheetDialog }
    }

    fun moveToAddPointDialog() {
        _randomShapeState.update { RandomShapeState.AddPointDialog }
    }

    fun moveTOUpdateDialog(index: Int) {
        val dot = listOffset[index]
        _randomShapeState.update { RandomShapeState.UpdatePointDialog(index, dot) }
    }

    fun moveToConstructor() {
        _randomShapeState.update { RandomShapeState.ConstructorShape }
    }

    fun updateSheetParams(sheetParam: SheetParam) {
        _sheet.update { _sheet.value.updateSheetParams(sheetParam) }
    }

    fun updateDot(
        index: Int,
        offsetBD: OffsetBD,
    ) {
        listOffset[index] = offsetBD
    }

    fun deleteDot(index: Int) {
        listOffset.removeAt(index)
    }

    fun getResult() {
        viewModelScope.launch {
            tileReportUseCase.invoke(
                listOfCoordinateShape = listOf(coordinateShape.value),
                sheet = _sheet.value,
            )
        }
    }
}

sealed interface RandomShapeState {
    data object ConstructorShape : RandomShapeState

    data object ManualDialog : RandomShapeState

    data object AddPointDialog : RandomShapeState

    data class UpdatePointDialog(val index: Int, val offsetBD: OffsetBD) : RandomShapeState

    data object SheetDialog : RandomShapeState
}
