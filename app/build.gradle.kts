plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
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

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(files("libs/TarsosDSP-Android-2.0.jar"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.legacy.support)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.hilt.android)
    implementation(libs.hilt.ext.lifecycle.viewmodel)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.materialdesign)
    implementation(libs.timber)

    testImplementation(libs.junit4)






    /**
     * Refactor below
     */

    // Material Design
//    implementation("com.google.android.material:material:1.8.0-alpha03")

    // MidiDriver
    implementation("com.github.rodydavis:MidiDriver-Android-SF2:v1.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Prefs DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha01")
    implementation("androidx.preference:preference-ktx:1.2.0")

    // Testing
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.20")
    androidTestImplementation("androidx.test:runner:1.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")










    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("com.google.android.material:compose-theme-adapter:1.2.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material:material-icons-core")

    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
}

repositories {
    mavenCentral()
}
