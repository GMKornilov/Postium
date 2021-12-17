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

    /**
     * Compose libraries
     */
    object Compose {
        const val Ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val UiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val Material = "androidx.compose.material:material:${Versions.compose}"
        const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
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
}