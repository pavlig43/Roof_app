import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.android.library) apply false

    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("de.mannodermaus.android-junit5") version "1.11.0.0" apply false

}
allprojects.onEach { project ->
    project.afterEvaluate {
        with(project.plugins) {
            if (hasPlugin(
                    libs.plugins.jetbrains.kotlin.android
                        .get()
                        .pluginId,
                ) ||
                hasPlugin(
                    libs.plugins.jetbrains.kotlin.jvm
                        .get()
                        .pluginId,
                )
            ) {
                apply(
                    libs.plugins.detekt
                        .get()
                        .pluginId,
                )
                apply(
                    libs.plugins.ktlint
                        .get()
                        .pluginId,
                )
                project.extensions.configure<DetektExtension> {
                    config.setFrom(rootProject.files("default-detekt-config.yml"))
                    autoCorrect = true
                }
                project.dependencies.add(
                    "detektPlugins",
                    libs.detekt.formatting
                        .get()
                        .toString(),
                )
            }
        }
    }
}
