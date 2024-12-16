package com.pavlig43.roofapp.data.resourceProvider

import android.content.Context
import java.io.InputStream

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(id: Int): String = context.getString(id)
    override fun getFontInputStream(fontPath: String): InputStream {
        return context.assets.open(fontPath)
    }
}
