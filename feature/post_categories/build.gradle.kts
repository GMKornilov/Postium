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
    implementation(project(":core:authorization"))
    implementation(project(":core:post"))
    implementation(project(":core:post_list"))

    implementation(project(":data:post"))
    implementation(project(":data:categories"))

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.firestore)
    implementation(Deps.Firebase.coroutinesPlayServices)

    implementation(Deps.AndroidX.androidXCoreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.AndroidX.lifecycleRuntime)

    implementation(Deps.materialDesign)

    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.UiToolingPreview)
    implementation(Deps.Compose.IconsExtended)

    implementation(Deps.Compose.Accompanist.swipeRefresh)
    implementation(Deps.Compose.Accompanist.flowLayouts)

    implementation(Deps.Compose.Icons.simple)

    implementation(Deps.Compose.Lottie.lottieCompose)

    implementation(Deps.Navigation.brickNavigation)

    implementation(Deps.MVI.Orbit.core)
    implementation(Deps.MVI.Orbit.viewModel)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.kapt)

    implementation(Deps.Logging.timber)

    implementation(Deps.Kotlin.coroutinesAndroid)
    implementation(Deps.Kotlin.coroutinesViewModel)

    testImplementation(Deps.TestingTooling.junit)
    testImplementation(Deps.MVI.Orbit.testing)
    androidTestImplementation(Deps.TestingTooling.androidxJunit)
    androidTestImplementation(Deps.TestingTooling.androidxEspresso)
    androidTestImplementation(Deps.TestingTooling.composeUiTest)
    debugImplementation(Deps.TestingTooling.composeUiTooling)
}