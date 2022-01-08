object Deps {
    /**
     * AndroidX libraries
     */
    object AndroidX {
        const val androidXCoreKtx = "androidx.core:core-ktx:${Versions.AndroidX.androidx_core_ktx}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.app_compat}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle_runtime_ktx}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.AndroidX.activity_compose}"
    }

    const val materialDesign = "com.google.android.material:material:${Versions.AndroidX.material}"

    object Kotlin {
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.Coroutines.version}"
        const val coroutinesViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Kotlin.Coroutines.viewModel}"
    }

    /**
     * Compose libraries
     */
    object Compose {
        const val Ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val UiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val Material = "androidx.compose.material:material:${Versions.compose}"
        const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val IconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"

        object Icons {
            const val simple = "br.com.devsrsouza.compose.icons.android:simple-icons:${Versions.Compose.Icons.simpleIcons}"
        }

        object Accompanist {
            const val insets = "com.google.accompanist:accompanist-insets:${Versions.Compose.Accompanist.insets}"
        }
    }

    /**
     * Testing and tooling libraries
     */
    object TestingTooling{
        const val junit = "junit:junit:${Versions.TestingTooling.junit}"
        const val androidxJunit = "androidx.test.ext:junit:${Versions.TestingTooling.androidJunit}"
        const val androidxEspresso = "androidx.test.espresso:espresso-core:${Versions.TestingTooling.espressoCore}"
        const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    }

    /**
     * Navigation tools
     */
    object Navigation {
        const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.Navigation.compose}"
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

        const val authorization = "com.google.firebase:firebase-auth-ktx"
    }

    object Google {
        const val googlePlayServicesAuth = "com.google.android.gms:play-services-auth:${Versions.Google.googlePlayServices}"
        const val googlePlayServicesPlugin = "com.google.gms.google-services"

        const val secrets = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.Google.secrets}"
        const val secretsPlugin = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin"
    }

    object Facebook {
        const val login = "com.facebook.android:facebook-login:${Versions.Facebook.login}"
    }

    /**
     * Hilt DI
     */
    object Hilt {
        const val plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.version}"
        const val android = "com.google.dagger:hilt-android:${Versions.Hilt.version}"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Hilt.version}"

        const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.Hilt.composeNavigation}"
    }
}