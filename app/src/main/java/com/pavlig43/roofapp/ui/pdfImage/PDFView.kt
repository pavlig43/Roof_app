package com.pavlig43.roofapp.ui.pdfImage

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState

@Composable
fun PDFView(
    pdfState: VerticalPdfReaderState,
    modifier: Modifier = Modifier,
    orientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
) {
    Box {
        LockOrientation(orientation)
        VerticalPDFReader(
            state = pdfState,
            modifier =
            modifier
                .fillMaxSize(),
        )
    }
}

@Composable
private fun LockOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose { }
        val originOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originOrientation
        }
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
