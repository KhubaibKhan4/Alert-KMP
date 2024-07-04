plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.nexus.publish)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}