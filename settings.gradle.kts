pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "alert-kmp"
include(":alert-kmp")
// The demo app: every notification type on Android, iOS, desktop and the web.
include(":sample:shared", ":sample:androidApp")
