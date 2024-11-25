package com.example.pdfcanvasdraw.implementationCore.pdf

import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageRenderer

interface DocBuilder {
    suspend fun createAndGetFileName(listOfPageRenderer: List<PageRenderer>): String
}
