
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("de.mannodermaus.android-junit5") version "1.11.0.0" apply false
}
allprojects {
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
    }
}
