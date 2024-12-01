import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.pavlig43.roof_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pavlig43.roof_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations += setOf("ru")
        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }
    }
    signingConfigs {
        create("release") {
            val keyStoreProperties = Properties()
            val keyStorePropertiesFile = File("keystore/keystore_config")

            if (keyStorePropertiesFile.exists()) {
                keyStoreProperties.load(FileInputStream(keyStorePropertiesFile))
                keyStorePropertiesFile.inputStream().use { keyStoreProperties.load(it) }
                storeFile = File(rootDir, keyStoreProperties["storeFile"] as String)
                storePassword = keyStoreProperties["storePassword"] as String
                keyAlias = keyStoreProperties["keyAlias"] as String
                keyPassword = keyStoreProperties["keyPassword"] as String
            } else {
                storeFile = File("$rootDir/keystore/roofapp.keystore")
                storePassword = System.getenv("KEYSTORE_PASSWORD") as String
                keyAlias = System.getenv("RELEASE_SIGN_KEY_ALIAS") as String
                keyPassword = System.getenv("RELEASE_SIGN_KEY_PASSWORD") as String
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/kotlin/**"
            excludes += "META-INF/androidx.*.version"
            excludes += "META-INF/com.google.*.version"
            excludes += "META-INF/kotlinx_*.version"
            excludes += "kotlin-tooling-metadata.json"
            excludes += "DebugProbesKt.bin"
            excludes += "META-INF/com/android/build/gradle/*"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(project(":mathBigDecimal"))
    implementation(project(":canvasDraw"))
    implementation(libs.bouquet)

    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.work.runtime)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
