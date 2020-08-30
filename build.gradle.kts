import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    java
    kotlin("jvm") version "1.3.72"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.12.0"
}

group = "me.haroldmartin"
version = "0.2.0"

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
    testImplementation("junit", "junit", "4.12")
}

intellij {
    version = "2019.3"
    updateSinceUntilBuild = false
    setPlugins("Kotlin", "java")
//    alternativeIdePath = "/Applications/Android Studio 4.2 Preview.app"
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

configure<JavaPluginConvention> {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

tasks {
    publishPlugin {
        token(System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken"))
    }

    getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
        changeNotes(
            "v0.2.0 - Kotlin Data Class to SharedPreferences\nNow uses defaults from class constructor"
                .trimMargin()
        )
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        targetCompatibility = VERSION_1_8.toString()
        sourceCompatibility = VERSION_1_8.toString()
        kotlinOptions {
            jvmTarget = VERSION_1_8.toString()
        }
    }
}
