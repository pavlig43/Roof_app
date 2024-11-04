package com.example.pdfcanvasdraw.pdf.model

import android.graphics.Canvas

data class PageContentBuilder(
    val pageConfig: PageConfig = PageConfig(),
    val generateDraw: Canvas.() -> Unit,

    )
