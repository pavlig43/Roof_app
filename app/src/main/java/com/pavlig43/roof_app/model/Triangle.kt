package com.pavlig43.roof_app.model

data class Triangle(
    val a: ShapeSide = ShapeSide("a",500),
    val b: ShapeSide = ShapeSide("b",400),
    val c: ShapeSide = ShapeSide("c",300) ,

){
    fun isValid(): Boolean= a.value + b.value > c.value && a.value + c.value > b.value && c.value + b.value > a.value
}
