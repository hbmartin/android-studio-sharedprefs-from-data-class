import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    kotlin("jvm") version "1.5.10"
    id("org.jetbrains.intellij") version "1.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("com.github.ben-manes.versions") version "0.39.0"
}

group = "me.haroldmartin"
version = properties("pluginVersion")

repositories {
    mavenCentral()
    jcenter()
}

detekt {
    failFast = true // fail build on any finding
    buildUponDefaultConfig = true // preconfigure defaults
    config =
        files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation("junit", "junit", "4.13.2")
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set("2019.3")
    type.set("IC")
    downloadSources.set(true)
    updateSinceUntilBuild.set(false)
    plugins.set(listOf("Kotlin", "java"))
//    alternativeIdePath = "/Applications/Android Studio 4.2 Preview.app"
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "1.8"
    }

    publishPlugin {
        token.set(System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken"))
        channels.set(listOf("default"))
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        changeNotes.set(file("CHANGELOG.html").readText())
    }
}
