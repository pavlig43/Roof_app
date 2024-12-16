package com.pavlig43.roofapp.data.resourceProvider

import androidx.annotation.StringRes
import java.io.InputStream

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
    fun getFontInputStream(fontPath: String): InputStream
}
