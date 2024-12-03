package com.pavlig43.roofapp.data.resourceProvider

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
}
