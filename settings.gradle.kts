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
include(":data:post")
include(":core")
include(":core:design")
include(":core:common")
include(":core:authorization")
include(":feature:most_popular_posts")
include(":feature:authorization")
include(":feature:authorization-api")
include(":core:secrets")
include(":core:activity-utils")
include(":feature:mainpage")
include(":data:post_likes")
include(":data:post_bookmarks")
include(":data:user")
