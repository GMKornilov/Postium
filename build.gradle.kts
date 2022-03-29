// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(BuildPlugins.AndroidTools)
        classpath(BuildPlugins.KotlinGradlePlugin)
        classpath(BuildPlugins.GooglePlayServices)
        classpath(Deps.Google.secrets)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(file("gradle/projectDependencyGraph.gradle"))