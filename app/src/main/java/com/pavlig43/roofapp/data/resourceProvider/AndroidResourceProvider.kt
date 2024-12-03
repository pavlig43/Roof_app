package com.pavlig43.roofapp.data.resourceProvider

import android.content.Context

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(id: Int): String = context.getString(id)
}
