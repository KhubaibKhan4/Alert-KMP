import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.30.0"
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release", "debug")
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    js {
        browser()
        binaries.executable()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.startup.runtime)
                implementation(libs.core)
            }
        }
        val iosMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val wasmJsMain by getting {
            dependencies {
            }
        }
    }
}

android {
    namespace = "io.github.khubaibkhan4.alert.kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
mavenPublishing {
    coordinates(
        groupId = "io.github.khubaibkhan4",
        artifactId = "alert-kmp",
        version = "1.0.7"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Alert-KMP")
        description.set("Compose & Kotlin Multiplatform Library that Help You to Show Native Notifications on Android, iOS, Web & Desktop.")
        inceptionYear.set("2024")
        url.set("https://github.com/KhubaibKhan4/Alert-KMP")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("khubaibkhan4")
                name.set("Muhammad Khubaib Imtiaz")
                email.set("18.bscs.803@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/KhubaibKhan4/Alert-KMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

task("testClasses") {}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17" // or "11" or "17"
    }
}