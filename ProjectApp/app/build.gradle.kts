import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "space.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "space.app"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.imagepicker)
    implementation(libs.glide)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.database)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.cardview)
    implementation(libs.legacy.support.v4)
    implementation(libs.firebase.auth)
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    implementation(libs.room.common)
    annotationProcessor("androidx.room:room-compiler:2.5.0") // For Java
    implementation("androidx.room:room-ktx:2.5.0") // For coroutines support
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}