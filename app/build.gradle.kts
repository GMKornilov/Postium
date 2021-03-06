plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id(Deps.Google.secretsPlugin)
    id(Deps.Google.googlePlayServicesPlugin)
}

android {
    compileSdk = BuildSettings.compileSdk

    defaultConfig {
        applicationId = "com.gmkornilov.postium"
        minSdk = BuildSettings.minSdk
        targetSdk = 31
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(project(":core:design"))
    implementation(project(":core:authorization"))
    implementation(project(":core:secrets"))
    implementation(project(":core:common"))
    implementation(project(":core:activity-utils"))
    implementation(project(":core:post"))
    implementation(project(":core:post_comments"))
    implementation(project(":core:post_list"))

    implementation(project(":data:post"))
    implementation(project(":data:post_likes"))
    implementation(project(":data:post_bookmarks"))
    implementation(project(":data:post_contents"))
    implementation(project(":data:user"))
    implementation(project(":data:categories"))
    implementation(project(":data:post_comments"))
    implementation(project(":data:comment_likes"))
    implementation(project(":data:playlists"))

    implementation(project(":feature:mainpage"))
    implementation(project(":feature:authorization"))
    implementation(project(":feature:postpage"))
    implementation(project(":feature:userpage"))
    implementation(project(":feature:postcreatepage"))
    implementation(project(":feature:commentpage"))
    implementation(project(":feature:post_categories"))
    implementation(project(":feature:user_playlists"))

    implementation(Deps.AndroidX.androidXCoreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.AndroidX.lifecycleRuntime)

    implementation(Deps.materialDesign)

    implementation(Deps.Compose.Ui)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.UiToolingPreview)

    implementation(Deps.Compose.Accompanist.insets)
    implementation(Deps.Compose.Accompanist.systemUi)

    implementation(Deps.MVI.Orbit.core)
    implementation(Deps.MVI.Orbit.testing)

    implementation(Deps.Compose.Icons.tabler)

    implementation(Deps.Navigation.composeNavigation)
    implementation(Deps.Navigation.brickNavigation)

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.authorization)
    implementation(Deps.Firebase.storage)
    implementation(Deps.Firebase.firestore)

    implementation(Deps.Facebook.login)

    implementation(Deps.Logging.timber)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.kapt)

    testImplementation(Deps.TestingTooling.junit)
    androidTestImplementation(Deps.TestingTooling.androidxJunit)
    androidTestImplementation(Deps.TestingTooling.androidxEspresso)
    androidTestImplementation(Deps.TestingTooling.composeUiTest)
    debugImplementation(Deps.TestingTooling.composeUiTooling)
}