pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Clippy"
include(":app")
include(":core")
include(":feature")


include(":core:util")

include(":feature:main")
include(":feature:clipboard")
include(":feature:clipboard_list")
include(":core:designsystem")
include(":core:data")



include(":core:domin")
include(":feature:clipboard_edit")

include(":feature:clipboard_save_animation")
include(":feature:shared")
include(":feature:sign_in")
include(":feature:splash")
