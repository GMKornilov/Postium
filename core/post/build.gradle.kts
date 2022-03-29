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
    implementation(project(":core:common"))
    implementation(project(":core:authorization"))

    implementation(project(":data:post"))
    implementation(project(":data:post_likes"))
    implementation(project(":data:post_bookmarks"))
    implementation(project(":data:user"))

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.firestore)
    implementation(Deps.Firebase.coroutinesPlayServices)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.kapt)

    implementation(Deps.Logging.timber)

    implementation(Deps.Kotlin.coroutinesAndroid)
    implementation(Deps.Kotlin.coroutinesViewModel)

    testImplementation(Deps.TestingTooling.junit)
    androidTestImplementation(Deps.TestingTooling.androidxJunit)
    androidTestImplementation(Deps.TestingTooling.androidxEspresso)
}