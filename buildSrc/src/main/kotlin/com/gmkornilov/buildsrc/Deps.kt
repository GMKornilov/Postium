object Deps {
    /**
     * AndroidX libraries
     */
    object AndroidX {
        const val androidXCoreKtx = "androidx.core:core-ktx:${Versions.AndroidX.androidx_core_ktx}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.app_compat}"
        const val lifecycleRuntime =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle_runtime_ktx}"
        const val activityCompose =
            "androidx.activity:activity-compose:${Versions.AndroidX.activity_compose}"
    }

    object Logging {
        const val timber = "com.jakewharton.timber:timber:${Versions.Logging.timber}"
    }

    const val materialDesign = "com.google.android.material:material:${Versions.AndroidX.material}"

    object Kotlin {
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.Coroutines.version}"
        const val coroutinesViewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Kotlin.Coroutines.viewModel}"
    }

    object Jetpack {
        const val dataStorePreferences = "androidx.datastore:datastore-preferences:${Versions.Jetpack.dataStore}"
    }

    /**
     * Compose libraries
     */
    object Compose {
        const val Ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val UiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val Material = "androidx.compose.material:material:${Versions.compose}"
        const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val IconsExtended =
            "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val ConstraintLayout =
            "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.Libraries.constraintLayout}"

        object Icons {
            const val simple =
                "br.com.devsrsouza.compose.icons.android:simple-icons:${Versions.Compose.Icons.simpleIcons}"
            const val tabler =
                "br.com.devsrsouza.compose.icons.android:tabler-icons:${Versions.Compose.Icons.tablerIcons}"
        }

        object Accompanist {
            const val insets =
                "com.google.accompanist:accompanist-insets:${Versions.Compose.Accompanist.insets}"

            const val systemUi =
                "com.google.accompanist:accompanist-systemuicontroller:${Versions.Compose.Accompanist.systemUi}"

            const val pager =
                "com.google.accompanist:accompanist-pager:${Versions.Compose.Accompanist.pager}"
            const val pagerIndicators =
                "com.google.accompanist:accompanist-pager-indicators:${Versions.Compose.Accompanist.pagerIndicators}"

            const val placeholderMaterial =
                "com.google.accompanist:accompanist-placeholder-material:${Versions.Compose.Accompanist.placeholder}"

            const val swipeRefresh =
                "com.google.accompanist:accompanist-swiperefresh:${Versions.Compose.Accompanist.swipeRefresh}"

            const val flowLayouts =
                "com.google.accompanist:accompanist-flowlayout:${Versions.Compose.Accompanist.flowLayouts}"
        }

        object Images {
            const val coil = "io.coil-kt:coil-compose:${Versions.Compose.Images.coil}"
        }

        object Lottie {
            const val lottieCompose =
                "com.airbnb.android:lottie-compose:${Versions.Compose.Lottie.version}"
        }

        object Richtext {
            const val commonmark =
                "com.halilibo.compose-richtext:richtext-commonmark:${Versions.Compose.Richtext.richtext}"
            const val materialUi =
                "com.halilibo.compose-richtext:richtext-ui-material:${Versions.Compose.Richtext.richtext}"
        }
    }

    /**
     * Testing and tooling libraries
     */
    object TestingTooling {
        const val junit = "junit:junit:${Versions.TestingTooling.junit}"
        const val androidxJunit = "androidx.test.ext:junit:${Versions.TestingTooling.androidJunit}"
        const val androidxEspresso =
            "androidx.test.espresso:espresso-core:${Versions.TestingTooling.espressoCore}"
        const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    }

    /**
     * Navigation tools
     */
    object Navigation {
        const val composeNavigation =
            "androidx.navigation:navigation-compose:${Versions.Navigation.compose}"

        const val brickNavigation = "io.github.alphicc:brick:${Versions.Navigation.brick}"
    }

    /**
     * MVI tools
     */
    object MVI {
        object Orbit {
            const val core = "org.orbit-mvi:orbit-core:${Versions.MVI.orbit}"
            const val viewModel = "org.orbit-mvi:orbit-viewmodel:${Versions.MVI.orbit}"
            const val testing = "org.orbit-mvi:orbit-viewmodel:${Versions.MVI.orbit}"
        }
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bomVersion}"

        const val firestore = "com.google.firebase:firebase-firestore-ktx"

        const val storage = "com.google.firebase:firebase-storage"

        const val authorization = "com.google.firebase:firebase-auth-ktx"

        const val coroutinesPlayServices =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Firebase.coroutinesPlayServices}"
    }

    object Google {
        const val googlePlayServicesAuth =
            "com.google.android.gms:play-services-auth:${Versions.Google.googlePlayServices}"
        const val googlePlayServicesPlugin = "com.google.gms.google-services"

        const val secrets =
            "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.Google.secrets}"
        const val secretsPlugin = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin"
    }

    object Facebook {
        const val login = "com.facebook.android:facebook-login:${Versions.Facebook.login}"
    }

    object Dagger {
        const val core = "com.google.dagger:dagger:${Versions.Dagger.version}"

        const val kapt = "com.google.dagger:dagger-compiler:${Versions.Dagger.version}"
    }
}