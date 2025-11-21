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

rootProject.name = "BeatU-Client"
include(":app")

// Shared modules (原 core/* 迁移而来)
include(":shared:common")
include(":shared:network")
include(":shared:database")
include(":shared:player")
include(":shared:designsystem")

// Business modules (按业务边界划分)
include(":business:videofeed:presentation")
include(":business:videofeed:domain")
include(":business:videofeed:data")

include(":business:user:presentation")
include(":business:user:domain")
include(":business:user:data")

include(":business:search:presentation")
include(":business:search:domain")
include(":business:search:data")

include(":business:ai:presentation")
include(":business:ai:domain")
include(":business:ai:data")

include(":business:landscape:presentation")
include(":business:landscape:domain")
include(":business:landscape:data")

include(":business:settings:presentation")
include(":business:settings:domain")
include(":business:settings:data")
