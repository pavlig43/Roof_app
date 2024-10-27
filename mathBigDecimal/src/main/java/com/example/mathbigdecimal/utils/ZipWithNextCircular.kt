package com.example.mathbigdecimal.utils

internal inline fun <T, R> Iterable<T>.zipWithNextCircular(transform: (a: T, b: T) -> R): List<R> {
    val iterator = iterator()
    if (!iterator.hasNext()) return emptyList()
    val result = mutableListOf<R>()
    val first = iterator.next()
    var current = first
    while (iterator.hasNext()) {
        val next = iterator.next()
        result.add(transform(current, next))
        current = next
    }
    result.add(transform(current, first))
    return result
}
