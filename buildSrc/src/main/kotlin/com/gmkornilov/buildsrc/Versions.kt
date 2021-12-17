object Versions {
    const val compose = "1.0.5"

    const val kotlin = "1.5.31"

    const val android_gradle = "7.0.3"
    const val kotlin_gradle = kotlin

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
    }

    /**
     * MVI tools
     */
    object MVI {
        const val orbit = "4.3.0"
    }
}