package com.example.pdfcanvasdraw.core.metrics

import com.example.pdfcanvasdraw.CM_IN_ONE_METER
import com.example.pdfcanvasdraw.core.pageKit.abstractPage.PageConfig
import com.example.pdfcanvasdraw.utils.extensions.roundUpToNearestWithScale

private fun getCountPxInOneCMSide(
    pageSideSize: Int,
    pagePaddingPercent: Float,
    maxDistance: Int = 1,
): Float =
    (pageSideSize * (1 - 2 * pagePaddingPercent)) / (
        maxDistance.roundUpToNearestWithScale()
            .takeIf { it != 0 } ?: CM_IN_ONE_METER
        )

fun PageConfig.getCountPxInOneCM(
    maxDistanceX: Int,
    maxDistanceY: Int,
): CountPxInOneCM {
    return CountPxInOneCM(
        x = getCountPxInOneCMSide(this.x, this.paddingPercentX, maxDistanceX),
        y = getCountPxInOneCMSide(this.y, this.paddingPercentY, maxDistanceY),
    )
}
