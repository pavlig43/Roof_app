package com.pavlig43.roofapp.navigation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pavlig43.roof_app.R
import com.pavlig43.roofapp.navigation.destination.ScreenDestination
import com.pavlig43.roofapp.navigation.geometryNavigation.GeometryDestination
import com.pavlig43.roofapp.navigation.roofNavigation.RoofDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    private val _listMenuWrapper =
        MutableStateFlow(listOf(forRoofMenuWrapper, forGeometryMenuWrapper))
    val listMenuWrapper = _listMenuWrapper.asStateFlow()

    fun changeExpanded(selectedMenuWrapper: MenuWrapper) {
        val updatedList = _listMenuWrapper.value.map { menuWrapper ->
            if (menuWrapper.title == selectedMenuWrapper.title) {
                menuWrapper.copy(isShowNested = !selectedMenuWrapper.isShowNested)
            } else {
                menuWrapper.copy(isShowNested = false)
            }
        }
        Log.d("updatedList", _listMenuWrapper.toString())
        _listMenuWrapper.update { updatedList }
    }
}

data class MenuWrapper(
    val title: Int,
    val nestedMenuItems: Array<ScreenDestination>,
    val isShowNested: Boolean = false
)

private val forRoofMenuWrapper = MenuWrapper(
    title = R.string.for_roofer,
    nestedMenuItems = RoofDestination.getAllDestination(),
)
private val forGeometryMenuWrapper = MenuWrapper(
    title = R.string.geometry,
    nestedMenuItems = GeometryDestination.getAllDestination(),
)
