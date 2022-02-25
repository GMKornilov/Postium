plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdk = BuildSettings.compileSdk

    defaultConfig {
        minSdk = BuildSettings.minSdk
        targetSdk = BuildSettings.targetSdk
        version = 1

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(Deps.AndroidX.androidXCoreKtx)
    implementation(Deps.AndroidX.appCompat)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.kapt)
}