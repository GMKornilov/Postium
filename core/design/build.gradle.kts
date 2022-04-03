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
        consumerProguardFile("consumer-rules.pro")
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(Deps.Kotlin.coroutinesAndroid)

    implementation(Deps.AndroidX.androidXCoreKtx)
    implementation(Deps.AndroidX.appCompat)

    implementation(Deps.materialDesign)

    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.AndroidX.lifecycleRuntime)

    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.UiGraphics)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.IconsExtended)
    implementation(Deps.Compose.UiToolingPreview)
    implementation(Deps.Compose.ConstraintLayout)
    implementation(Deps.Compose.Icons.simple)
    implementation(Deps.Compose.Icons.tabler)

    implementation(Deps.Compose.Lottie.lottieCompose)

    implementation(Deps.Compose.Accompanist.placeholderMaterial)
    implementation(Deps.Compose.Accompanist.insets)

    implementation(Deps.Compose.Images.coil)

    testImplementation(Deps.TestingTooling.junit)
    androidTestImplementation(Deps.TestingTooling.androidxJunit)
    androidTestImplementation(Deps.TestingTooling.androidxEspresso)
    androidTestImplementation(Deps.TestingTooling.composeUiTest)
    debugImplementation(Deps.TestingTooling.composeUiTooling)
}