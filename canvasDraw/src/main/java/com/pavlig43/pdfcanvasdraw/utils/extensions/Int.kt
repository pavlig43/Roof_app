package com.pavlig43.pdfcanvasdraw.utils.extensions

import com.pavlig43.pdfcanvasdraw.CM_IN_ONE_METER
import kotlin.math.ceil

/**

 *  655.roundUpToNearestWithScale(100) -> 700
 */
fun Int.roundUpToNearestWithScale(scale: Int = CM_IN_ONE_METER): Int {
    return (ceil(this.toDouble() / scale) * scale).toInt()
}

/**
 * 655.roundUpToNearestToFullScale(100) -> 7m
 */
fun Int.roundUpToNearestToFullScale(scale: Int = CM_IN_ONE_METER): Int {
    return ceil(this.toDouble() / scale).toInt()
}
