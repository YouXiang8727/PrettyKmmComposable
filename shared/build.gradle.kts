import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())
val usr: String = localProperties.getProperty("gpr.usr")
val key: String = localProperties.getProperty("gpr.key")

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/youxiang8727/PrettyKmmComposable")
            credentials {
                username = usr
                password = key
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["kotlin"])
            groupId = "com.youxiang8727"
            artifactId = "pretty-kmm-composable"
            version = "1.0.0"

            // 將 iOS 平台的 framework 添加為 artifact
            kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).configureEach {
                binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
                    artifact(this.outputFile)
                }
            }
        }
    }
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "pretty_kmm_composable" // 使用下划线以确保一致性
            isStatic = true
        }
    }


    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "com.example.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
