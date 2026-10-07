pluginManagement {
    // build-logic is an "included build": Gradle compiles it first and exposes its
    // convention plugins (jetbase.android.*) to the rest of the modules.
    includeBuild("build-logic")
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

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    // Forbids modules from declaring their own repositories: everything is resolved here.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JetBase"

// Lets us write projects.core.data instead of project(":core:data").
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":core:common")
include(":core:model")
include(":core:navigation")
include(":core:datastore")
include(":core:database")
include(":core:data")
include(":core:designsystem")
include(":core:ui")
include(":core:testing")

// Each feature = :api (navigation keys, what others may see) + :impl (screens, VMs).
include(":feature:notes:api")
include(":feature:notes:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
