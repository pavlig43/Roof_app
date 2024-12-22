package com.pavlig43.roofapp.ui.geometry.flat.rightTriangle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlig43.roofapp.WHILE_SUBSCRIBED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RightTriangleViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(RightTriangleState())
    val state = _state.asStateFlow()

    fun onChange(param: RightTriangleParam) {
        when (param.name) {
            RightTriangleParamName.A -> _state.update { it.copy(A = param) }
            RightTriangleParamName.B -> _state.update { it.copy(B = param) }
            RightTriangleParamName.C -> _state.update { it.copy(C = param) }
            RightTriangleParamName.Alpha -> _state.update { it.copy(alpha = param) }
            RightTriangleParamName.Beta -> _state.update { it.copy(beta = param) }
            RightTriangleParamName.Gamma -> {}
        }
    }


    private val notEmptyParams = _state.map {
        val params = listOf(it.A, it.B, it.C, it.alpha, it.beta)
        params.filter { it.value.isNotBlank() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED), listOf())

    val isValidParams = notEmptyParams.map { notEmptyParams ->
        notEmptyParams.count { it.value.toBigDecimalOrNull() != null } == notEmptyParams.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED), false)


    val isValidCountParams = notEmptyParams.map {
        it.size == 2
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED), false)

}


@Suppress("ConstructorParameterNaming")
data class RightTriangleState(
    val A: RightTriangleParam = RightTriangleParam(RightTriangleParamName.A),
    val B: RightTriangleParam = RightTriangleParam(RightTriangleParamName.B),
    val C: RightTriangleParam = RightTriangleParam(RightTriangleParamName.C),
    val alpha: RightTriangleParam = RightTriangleParam(RightTriangleParamName.Alpha),
    val beta: RightTriangleParam = RightTriangleParam(RightTriangleParamName.Beta),
    val gamma: RightTriangleParam = RightTriangleParam(RightTriangleParamName.Gamma, "90"),
)

data class RightTriangleParam(
    val name: RightTriangleParamName,
    val value: String = "",
)

enum class RightTriangleParamName {
    A, B, C, Alpha, Beta, Gamma
}
