object Versions {
    const val compose = "1.1.0"

    const val android_gradle = "7.0.3"
    const val kotlin_gradle = Kotlin.version

    object Kotlin {
        const val version = "1.6.10"

        object Coroutines {
            const val version = "1.3.9"

            const val viewModel = "2.4.0"
        }
    }

    object Logging {
        const val timber = "5.0.1"
    }

    /**
     * AndroidX libraries
     */
    object AndroidX {
        const val androidx_core_ktx = "1.7.0"
        const val lifecycle_runtime_ktx = "1.4.0"
        const val app_compat = "1.4.0"
        const val material = "1.4.0"
        const val activity_compose = "1.4.0"
    }

    object Jetpack {
        const val dataStore = "1.0.0"
    }

    /**
     * Testing and tooling libraries
     */
    object TestingTooling {
        const val junit = "4.+"
        const val androidJunit = "1.1.3"
        const val espressoCore = "3.4.0"
    }

    /**
     * Navigation tools
     */
    object Navigation {
        const val compose = "2.4.0-rc01"

        const val brick = "1.0.0-beta02"
    }

    /**
     * MVI tools
     */
    object MVI {
        const val orbit = "4.3.0"
    }

    object Compose {
        object Icons {
            const val simpleIcons = "1.0.0"
            const val tablerIcons = "1.0.0"
        }

        object Libraries {
            const val constraintLayout = "1.0.0"
        }

        object Accompanist {
            const val insets = "0.22.0-rc"

            const val systemUi = "0.22.0-rc"

            const val pager = "0.22.0-rc"
            const val pagerIndicators = "0.22.0-rc"

            const val placeholder = "0.22.0-rc"

            const val swipeRefresh = "0.22.0-rc"

            const val flowLayouts = "0.22.0-rc"
        }

        object Images {
            const val coil = "2.0.0-rc01"
        }

        object Lottie {
            const val version = "5.0.3"
        }

        object Richtext {
            const val richtext = "0.11.0"
        }
    }

    /**
     * Firebase dependencies
     */
    object Firebase {
        const val bomVersion = "29.0.3"

        const val coroutinesPlayServices = "1.6.0"
    }

    object Google {
        const val googlePlayServices = "19.2.0"

        const val googlePlayServicesPlugin = "4.3.10"

        const val secrets = "2.0.0"
    }

    object Facebook {
        const val login = "12.2.0"
    }

    object Dagger {
        const val version = "2.41"
    }
}