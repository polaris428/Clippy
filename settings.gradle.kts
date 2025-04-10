pluginManagement {
    plugins {
        id("com.vanniktech.dependency.graph.generator") version "0.6.0"
    }
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



include(":core:domain")
include(":feature:clipboard_edit")

include(":feature:clipboard_save_animation")

include(":feature:sign_in")
include(":feature:folder_edit")
include(":feature:splash")
include(":core:model")

include(":feature:clipboard_shared_list")
include(":feature:main_slide_panel")

include(":core:database")
include(":feature:folder_join")
include(":feature:main_save")
include(":feature:folder_setting")
