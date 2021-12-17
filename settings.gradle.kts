dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
         // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "Postium"
include(":app")
include(":core")
include(":core:design")
include(":core:feature-api")
include(":feature:most_popular_posts")
include(":data:post")
