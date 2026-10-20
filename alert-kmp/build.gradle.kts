import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "io.github.khubaibkhan4.alert.kmp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_1_8 }
    }
    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_1_8 }
    }
    @OptIn(ExperimentalWasmDsl::class)
    // No browser tests: this also keeps karma and its old dependencies out of kotlin-js-store.
    wasmJs { browser { testTask { enabled = false } } }
    js { browser { testTask { enabled = false } } }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    compilerOptions {
        // Usable from apps on Kotlin 2.2 and newer.
        apiVersion = KotlinVersion.KOTLIN_2_2
        languageVersion = KotlinVersion.KOTLIN_2_2
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.androidx.startup)
            implementation(libs.androidx.core)
        }
        // kotlinx.browser left the Kotlin/Wasm standard library in Kotlin 2.2.
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

/*
 * Publishing: ./gradlew :alert-kmp:publishToMavenCentral, then release the deployment on central.sonatype.com.
 * Credentials (mavenCentralUsername / mavenCentralPassword) and signing (signing.gnupg.keyName, or
 * signingInMemoryKey / signingInMemoryKeyPassword) go in ~/.gradle/gradle.properties or CI secrets, never here.
 */
mavenPublishing {
    publishToMavenCentral(automaticRelease = false)
    if (gradle.startParameter.taskNames.any { "MavenCentral" in it }) signAllPublications()
    coordinates("io.github.khubaibkhan4", "alert-kmp", "2.1.0")
    pom {
        name = "Alert-KMP"
        description = "Compose & Kotlin Multiplatform Library that Help You to Show Native Notifications on Android, iOS, Web & Desktop."
        inceptionYear = "2024"
        url = "https://github.com/KhubaibKhan4/Alert-KMP"
        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "khubaibkhan4"
                name = "Muhammad Khubaib Imtiaz"
                email = "18.bscs.803@gmail.com"
            }
        }
        scm {
            url = "https://github.com/KhubaibKhan4/Alert-KMP"
        }
    }
}
