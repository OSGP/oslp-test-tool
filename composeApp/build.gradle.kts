import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.spotless)
}

kotlin {
    jvm("desktop")
    jvmToolchain(18)

    sourceSets {
        val desktopMain by getting
        all { dependencies { implementation(project(":protobuf")) } }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.protobufKotlin)
            implementation(libs.ktor)
            implementation(libs.kotlinSerialization)
            implementation(libs.protobufJavaUtil)
        }
    }
}

compose.desktop {
    application {
        mainClass = "nl.alliander.oslp.MainKt"

        buildTypes.release.proguard {
            isEnabled = true
            optimize = false
            obfuscate = false
            configurationFiles.from(file("proguard-rules.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "oslp-test-tool"
            packageVersion = "1.0.0"
        }
    }
}

extensions.configure<SpotlessExtension> {
    kotlinGradle { ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) } }

    kotlin {
        target("src/**/*.kt")
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }

        licenseHeaderFile(file("../spotless/license-header-template.kt"))
    }
}
