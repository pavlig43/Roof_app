import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.junit5) apply false
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
