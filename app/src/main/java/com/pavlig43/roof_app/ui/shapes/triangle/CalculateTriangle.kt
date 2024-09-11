package com.pavlig43.roof_app.ui.shapes.triangle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavlig43.roof_app.R
import com.pavlig43.roof_app.model.ShapeSide
import com.pavlig43.roof_app.model.Sheet
import com.pavlig43.roof_app.model.Triangle
import com.pavlig43.roof_app.ui.calculation_tile_4scat.CalculateSheetParams
import com.pavlig43.roof_app.ui.shapes.ChoiceCalculateItem
import com.pavlig43.roof_app.ui.theme.Roof_appTheme
import kotlin.math.ceil

@Composable
fun CalculateTriangle(
    triangle: Triangle,
    changeTriangle: (ShapeSide) -> Unit,
    sheet: Sheet,
    changeWidthOfSheet: (String) -> Unit,
    changeOverlap: (String) -> Unit,
    isValidate:Boolean,
    getResult:()->Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(id = R.drawable.triangle_image), contentDescription = null)
        ChoiceCalculateItem(shapeSide = triangle.a, changeSideValue = changeTriangle)
        ChoiceCalculateItem(shapeSide = triangle.b, changeSideValue = changeTriangle)
        ChoiceCalculateItem(shapeSide = triangle.c, changeSideValue = changeTriangle)
        Spacer(modifier = Modifier.height(20.dp))
        CalculateSheetParams(
            nameField = "Ширина листа",
            value = (sheet.widthGeneral * 100 ).toInt().toString(),
            onValueChange = changeWidthOfSheet
        )
        CalculateSheetParams(
            nameField = "Перехлест",
            value = (sheet.overlap * 100) .toInt().toString(),
            onValueChange = changeOverlap
        )

        Button(onClick = getResult, enabled = isValidate) {
            Text(text = "Получить результат")
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CalculateTrianglePreview() {
    Roof_appTheme {
        CalculateTriangle(
            changeTriangle = { _ -> Unit },
            triangle = Triangle(),
            changeWidthOfSheet = {_->Unit},
            changeOverlap =  {_->Unit},
            sheet = Sheet(),
            getResult = {},
            isValidate = true)
    }
}