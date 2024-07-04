import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("Alert-KMP")
            description.set("A Library that helps us to Display native Notifications on Android, iOS, Web and Desktop")
            url.set("https://github.com/KhubaibKhan4/Alert-KMP")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Muhammad Khubaib Imtiaz")
                    name.set("Codespacepro Team")
                    organization.set("Codespacepro")
                    organizationUrl.set("https://github.com/KhubaibKhan4/")
                }
            }
            scm {
                url.set("https://github.com/KhubaibKhan4/Alert-KMP")
            }
        }
    }
}


signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
