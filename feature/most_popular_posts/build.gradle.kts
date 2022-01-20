plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:design"))

    implementation(project(":data:post"))

    implementation(Deps.AndroidX.androidXCoreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.AndroidX.lifecycleRuntime)

    implementation(Deps.materialDesign)

    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.UiToolingPreview)

    implementation(Deps.Navigation.composeNavigation)
    implementation(Deps.Navigation.brickNavigation)

    implementation(Deps.MVI.Orbit.core)
    implementation(Deps.MVI.Orbit.viewModel)

    implementation(Deps.Hilt.android)
    kapt(Deps.Hilt.androidCompiler)

    testImplementation(Deps.TestingTooling.junit)
    testImplementation(Deps.MVI.Orbit.testing)
    androidTestImplementation(Deps.TestingTooling.androidxJunit)
    androidTestImplementation(Deps.TestingTooling.androidxEspresso)
    androidTestImplementation(Deps.TestingTooling.composeUiTest)
    debugImplementation(Deps.TestingTooling.composeUiTooling)
}

hilt {
    enableAggregatingTask = true
}