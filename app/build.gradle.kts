plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs")

    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(33)
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    defaultConfig {
        applicationId = "com.cannonballapps.notechaser"
        minSdkVersion(21)
        targetSdkVersion(32)
        versionCode = 3
        versionName = "1.03-alpha"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    namespace = "com.cannonballapps.notechaser"
}

dependencies {
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.legacy.support)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.preferences)
    implementation(libs.hilt.android)
    implementation(libs.hilt.ext.lifecycle.viewmodel)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.materialdesign.compose.themeadapter)
    implementation(libs.materialdesign.material)
    implementation(libs.timber)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.androidx.arch.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.junit)
}

repositories {
    mavenCentral()
}
