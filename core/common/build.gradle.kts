plugins {
    id("com.android.library")
    id("kotlin-android")
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
            isMinifyEnabled =  false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), 
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(Deps.Navigation.composeNavigation)
    implementation(Deps.Navigation.brickNavigation)
    implementation(Deps.Compose.Material)
    implementation(Deps.MVI.Orbit.core)
    implementation(Deps.MVI.Orbit.viewModel)
}