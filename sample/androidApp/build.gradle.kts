plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.khubaibkhan4.alert.sample"
    compileSdk = libs.versions.sample.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "io.github.khubaibkhan4.alert.sample"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.sample.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(libs.androidx.activity.compose)
}
