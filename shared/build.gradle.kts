import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
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
            baseName = "pretty_kmm_composable"
            isStatic = true
            version = "1.0.0"
        }
    }


    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "com.youxiang.pretty_kmm_composable"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

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
            kotlin.targets.withType(KotlinNativeTarget::class.java).configureEach {
                binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
                    val classifier = "${this.target.name}_${this.buildType.name}"
                    val zipFileTask = createZipFrameworkTask(this.outputFile, this.buildType, this.target)
                    artifact(zipFileTask) {
                        extension = "zip"
                        this.classifier = classifier
                    }
                }
            }

            // 發佈 Android AAR
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar").outputs.files.singleFile) {
                    classifier = "release"
                    extension = "aar"
                }
                artifact(tasks.getByName("bundleDebugAar").outputs.files.singleFile) {
                    classifier = "debug"
                    extension = "aar"
                }
            }
        }
    }
}

// 將 publish 任務依賴於 bundle 任務
tasks.withType<PublishToMavenRepository>().configureEach {
    dependsOn("bundleReleaseAar", "bundleDebugAar")
}

fun Project.createZipFrameworkTask(file: File, buildType: NativeBuildType, target: KotlinNativeTarget): Zip {
    return tasks.create<Zip>("zip${file.name}_${target.name}_${buildType.name}") {
        from(file)
        archiveFileName.set("${file.name}.zip") // 設置壓縮檔名
        destinationDirectory.set(file.parentFile)
    }
}