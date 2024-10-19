plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mentalhealthtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mentalhealthtracker"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    // Required to use `ListenableFuture` from Guava Android for one-shot generation
    implementation("com.google.guava:guava:31.0.1-android")

    // Required to use `Publisher` from Reactive Streams for streaming operations
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.espresso.web)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}