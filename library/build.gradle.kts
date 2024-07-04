import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.29.0"
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
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "alert-kmp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val androidMain by getting {
            dependencies {

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
        artifactId = "browser-kmp",
        version = "1.0.3"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Alert-KMP")
        description.set("Library used to open up a display notification on both Android/iOS/Web/Desktop")
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