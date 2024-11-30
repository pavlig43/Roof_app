package com.pavlig43.pdfcanvasdraw.implementationCore.pdf

import com.pavlig43.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

interface DocBuilder {
    suspend fun createAndGetFileName(listOfPageRenderer: List<PageRenderer>): String
}
