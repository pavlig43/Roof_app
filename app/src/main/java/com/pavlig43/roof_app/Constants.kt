package com.pavlig43.roof_app

import java.io.File

const val A4WIDTH = 595 // Ширина A4 в пикселях
const val A4HEIGHT = 841  // Высота A4 в пикселях



fun listFiles(startPath: String, outputFilePath: String) {
    val startDir = File(startPath)
    val outputFile = File(outputFilePath)

    outputFile.printWriter().use { writer ->
        startDir.walkTopDown().forEach { file ->
            val relativePath = startDir.toPath().relativize(file.toPath()).toString()
            val depth = relativePath.count { it == File.separatorChar }
            val indent = " ".repeat(depth * 4)
            if (file.isDirectory) {
                writer.println("$indent${file.name}/")
            } else {
                writer.println("$indent${file.name}")
            }
        }
    }
}

fun main() {
    val projectPath = "C:\\Users\\user\\AndroidStudioProjects\\Roof_app" // Замените на путь к вашему проекту
    val outputPath = "структура_проекта.txt" // Имя выходного файла
    listFiles(projectPath, outputPath)
}
