import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    java
    kotlin("jvm") version "1.3.72"
}

group = "me.haroldmartin"
version = "0.1.0"

repositories {
    mavenCentral()
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
    getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
        changeNotes("""First release""")
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        targetCompatibility = VERSION_1_8.toString()
        sourceCompatibility = VERSION_1_8.toString()
        kotlinOptions {
            jvmTarget = VERSION_1_8.toString()
        }
    }
}