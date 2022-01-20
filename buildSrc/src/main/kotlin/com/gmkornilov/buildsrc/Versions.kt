object Versions {
    const val compose = "1.0.5"

    const val android_gradle = "7.0.3"
    const val kotlin_gradle = Kotlin.version

    object Kotlin {
        const val version = "1.5.31"

        object Coroutines {
            const val version = "1.3.9"

            const val viewModel = "2.4.0"
        }
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
        }

        object Accompanist {
            const val insets = "0.22.0-rc"
        }
    }

    /**
     * Firebase dependencies
     */
    object Firebase {
        const val bomVersion = "29.0.3"
    }

    object Google {
        const val googlePlayServices = "19.2.0"

        const val googlePlayServicesPlugin = "4.3.10"

        const val secrets = "2.0.0"
    }

    object Facebook {
        const val login = "12.2.0"
    }

    /**
     * Hilt DI dependencies
     */
    object Hilt {
        const val version = "2.38.1"

        const val composeNavigation = "1.0.0-rc01"
    }
}