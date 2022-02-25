plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
    id(Deps.Google.googlePlayServicesPlugin)
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
    implementation(project(":core:common"))
    implementation(project(":core:secrets"))
    implementation(project(":core:activity-utils"))

    implementation(Deps.Kotlin.coroutinesAndroid)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.kapt)

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.authorization)
    implementation(Deps.Firebase.coroutinesPlayServices)
    implementation(Deps.Google.googlePlayServicesAuth)

    implementation(Deps.Facebook.login)
}