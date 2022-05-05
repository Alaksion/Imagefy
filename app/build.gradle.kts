import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "br.com.alaksion.myapplication"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "PUBLIC_KEY",
            gradleLocalProperties(rootDir).getProperty("PUBLIC_KEY")
        )
        buildConfigField(
            "String",
            "SECRET_KEY",
            gradleLocalProperties(rootDir).getProperty("SECRET_KEY")
        )
        buildConfigField(
            "String",
            "REDIRECT_URI",
            gradleLocalProperties(rootDir).getProperty("REDIRECT_URI")
        )
        buildConfigField(
            "String",
            "API_SCOPE",
            gradleLocalProperties(rootDir).getProperty("API_SCOPE")
        )

    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
        }

        getByName("debug") {
            isTestCoverageEnabled = true
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Projects
    implementation(project(":network"))
    implementation(project(":core-ui"))

    // AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.compose.material.icons)

    // Async
    implementation(libs.coroutines.core)

    // Testing
    testImplementation(libs.bundles.test.unit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.compose.uijunit)
    debugImplementation(libs.androidx.compose.uimanifest)

    // Hilt
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.0-beta01")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Data Store
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.10.0")

}

kapt {
    correctErrorTypes = true
}