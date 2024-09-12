package com.pavlig43.roof_app.ui.shapes.quadrilateral

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pavlig43.roof_app.model.Dot
import com.pavlig43.roof_app.model.DotName4Side
import com.pavlig43.roof_app.ui.MinusIcon
import com.pavlig43.roof_app.ui.theme.Roof_appTheme

@Composable
fun QuadroChoice(
    quadrilateralViewModel: QuadrilateralViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current


    // Получаем ширину экрана в пикселях
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    // Получаем высоту экрана в пикселях
    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val leftBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.2).toFloat())
    val leftTopCenter = Offset((screenWidth * 0.7).toFloat(), (screenHeight * 0.2).toFloat())
    val rightBottomCenter = Offset((screenWidth * 0.2).toFloat(), (screenHeight * 0.5).toFloat())
    val rightTopCenter = Offset((screenWidth * 0.7).toFloat(), (screenHeight * 0.5).toFloat())

    val geometricShape by quadrilateralViewModel.geometryShape.collectAsState()

    var showDotDialog by remember { mutableStateOf(false) }

    val currentDot by quadrilateralViewModel.currentDot.collectAsState()
    val isValid by quadrilateralViewModel.isValid.collectAsState()


    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset: Offset ->
                        when {
                            (leftTopCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.LEFTTOP)
                            }

                            (rightTopCenter - offset).getDistance() <= 45f

                            -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.RIGHTTOP)
                            }

                            (rightBottomCenter - offset).getDistance() <= 45f -> {
                                showDotDialog = true
                                quadrilateralViewModel.changeCurrentDotName(DotName4Side.RIGHTBOTTOM)
                            }
                        }


                    }

                }
        ) {
            drawLine(
                Color.Black,
                Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
                Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.8).toFloat()),
                strokeWidth = 10f
            )
            drawLine(
                Color.Black,
                Offset((screenWidth * 0.05).toFloat(), (screenHeight * 0.05).toFloat()),
                Offset((screenWidth * 0.8).toFloat(), (screenHeight * 0.05).toFloat()),
                strokeWidth = 10f
            )
            drawDot(
                center = leftBottomCenter,
                dot = geometricShape.leftBottom,
                downOffset = true,
                startDot = true
            )
            drawDot(center = leftTopCenter, dot = geometricShape.leftTop, downOffset = true)
            drawDot(
                center = rightBottomCenter,
                dot = geometricShape.rightBottom,
                downOffset = false
            )
            drawDot(center = rightTopCenter, dot = geometricShape.rightTop, downOffset = false)



            drawLine(
                Color.Black,
                leftBottomCenter,
                leftTopCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                leftBottomCenter,
                rightBottomCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                leftTopCenter,
                rightTopCenter,
                strokeWidth = 5f
            )
            drawLine(
                Color.Black,
                rightBottomCenter,
                rightTopCenter,
                strokeWidth = 5f
            )
        }

        if (showDotDialog){
            ChangeParamsDots(
                dot = currentDot,
                changeDot = quadrilateralViewModel::changeParamsDot ,
                onDismissRequest = { showDotDialog=false })
        }
        if (isValid)
        ButtonRow(
            modifier = Modifier.align(Alignment.BottomCenter),
            getResult = {quadrilateralViewModel.openDocument(context)}
            )
    }


}

fun DrawScope.drawDot(
    downOffset: Boolean,
    offsetY: Float = 35f,
    startDot: Boolean = false,
    center: Offset,
    dot: Dot,
    textPaint: Paint = Paint().apply {
        textSize = 20f //
        color = Color.Black.toArgb()
        textAlign = Paint.Align.CENTER
    }
) {
    drawCircle(if (startDot) Color.Green else Color.Black, center = center, radius = 15f)
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "(${dot.distanceX.toInt()}cm, ${dot.distanceY.toInt()}cm)",
            center.x,
            center.y + if (!downOffset) offsetY else -offsetY,
            textPaint
        )
    }
}

@Composable
fun ChangeParamsDots(
    dot: Dot,
    changeDot: (Dot) -> Unit,
    onDismissRequest: () -> Unit,

    modifier: Modifier = Modifier
) {


    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier.background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Отклонение от левой нижней точки")
            ParamsDotRow(
                value = dot.distanceX,
                onValueChange = { newValue -> changeDot(dot.copy(distanceX = newValue)) },
                axis = "X",
                canChangePlus = dot.canMinusX,
                plus = dot.distanceX>=0,
                changePlus = {
                    changeDot(dot.copy(distanceX = -dot.distanceX))
                }
            )
            ParamsDotRow(
                value = dot.distanceY,
                onValueChange = { newValue -> changeDot(dot.copy(distanceY = newValue)) },
                canChangePlus = dot.canMinusY,
                plus = dot.distanceY>=0,
                changePlus = {
                    changeDot(dot.copy(distanceY = -dot.distanceY))
                }
            )
            Button(
                onClick = onDismissRequest,

                ) {
                Text(text = "OK")
            }
        }

    }

}

@Composable
fun ParamsDotRow(
    value: Float,
    onValueChange: (Float) -> Unit,
    axis: String = "Y",
    canChangePlus: Boolean = false,
    plus:Boolean,
    changePlus:()->Unit,

    modifier: Modifier = Modifier
) {



    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Отклонение по $axis (см)", modifier = Modifier
            .padding(end = 4.dp)
            .weight(0.55f))
        if (canChangePlus) {
            Button(onClick = changePlus, modifier = Modifier.weight(0.2f)) {
                Icon(
                    imageVector = if (plus) Icons.Default.Add else MinusIcon,
                    contentDescription = null
                )
            }
        }
        else{
            Spacer(modifier = Modifier.weight(0.2f))
        }
        TextField(
            value = if (value==0f)"" else value.toInt().toString(),
            onValueChange = { it: String ->
                val newValue =  it.toIntOrNull()?:0
                onValueChange(if (plus) newValue.toFloat() else - newValue.toFloat()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(0.35f)
            )


    }
}

@Composable
fun ButtonRow(
    getResult:()->Unit,
    modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Параметры листа")
        }
        Button(onClick = getResult) {
            Text(text = "Получить результат")
        }
    }
    
}

@Preview(showBackground = true)
@Composable
private fun ParamsDotRowPreview() {
    Roof_appTheme {
        ChangeParamsDots(
            dot = Dot(DotName4Side.LEFTTOP,canMinusY = true),
            onDismissRequest = {},
            changeDot = {_->Unit}

            )
    }

}




