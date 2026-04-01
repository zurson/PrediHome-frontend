@file:Suppress("PropertyName")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.betoniarze.predihome"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.betoniarze.predihome"
        minSdk = 32
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        compileSdkPreview = "UpsideDownCake"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    /** Core **/
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(kotlin("script-runtime"))

    /** Coroutines **/
    implementation(libs.kotlinx.coroutines.android)

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)

    /** Google **/
    implementation(libs.play.services.auth.vx)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials.v130)
    implementation(libs.androidx.credentials.play.services.auth.v130)
    implementation(libs.googleid.v111)

    /** Facebook **/
    implementation(libs.facebook.login.vlatestrelease)

    /** Communication **/
    implementation (libs.gson.v2110)

    /** Maps **/
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widgets)

    /** Reflect **/
    implementation(libs.kotlin.reflect)

    /** Jetpack Foundation **/
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    /** Compose **/
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material)
    implementation(libs.androidx.emoji2)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)


    /** Accompanist **/
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.material3.android)
    implementation(libs.google.accompanist.systemuicontroller)

    /** Navigation **/
    implementation(libs.androidx.navigation.compose)

    /** Test **/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
}