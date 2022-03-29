dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "Postium"
include(":app")
include(":data:post")
include(":core")
include(":core:design")
include(":core:common")
include(":core:authorization")
include(":feature:authorization")
include(":feature:authorization-api")
include(":core:secrets")
include(":core:activity-utils")
include(":feature:mainpage")
include(":data:post_likes")
include(":data:post_bookmarks")
include(":data:user")
include(":data:post_contents")
include(":feature:postpage")
include(":feature:userpage")
include(":core:post")
