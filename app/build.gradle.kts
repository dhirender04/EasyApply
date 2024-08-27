plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.application.hilt)
     kotlin("kapt") // Add this line for Kotlin Annotation Processing Tool
}

android {
    namespace = "com.example.easyapply"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.easyapply"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    dataBinding{
        enable = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.com.google.devtools.ksp.gradle.plugin)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    implementation(libs.support.annotations)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Room dependencies
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
//    annotationProcessor (libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.hilt.android)
//    annotationProcessor(libs.hilt.android.compiler)
    kapt(libs.hilt.android.compiler)


}
