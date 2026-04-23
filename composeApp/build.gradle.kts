
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.File
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val appName = providers.gradleProperty("APP_NAME").get()
val appVersionName = providers.gradleProperty("VERSION_NAME").get()
val appVersionCode = providers.gradleProperty("VERSION_CODE").get().toInt()

compose.resources {
    packageOfResClass = "com.rekcode.yahtzee.generated.resources"
    publicResClass = true
}

// --- START OF CUSTOM SYNC LOGIC ---
tasks.register("syncDesignSystem") {
    group = "design"

    // Use layout.projectDirectory to ensure we start from /composeApp/
    val colorsSource = layout.projectDirectory.file("src/commonMain/designSystem/colors.txt").asFile
    val templateDir = layout.projectDirectory.dir("src/commonMain/vectorTemplates").asFile
    val xmlOutputDir = layout.projectDirectory.dir("src/commonMain/composeResources/drawable").asFile
    val ktOutputFile = layout.projectDirectory.file("src/commonMain/kotlin/com/rekcode/yahtzee/generated/AppColors.kt").asFile

    val androidResOutputDir = layout.buildDirectory.dir("generated/designSystem/androidRes").get().asFile
    val androidDrawableOutputDir = File(androidResOutputDir, "drawable")
    val androidValuesOutputDir = File(androidResOutputDir, "values")
    val androidColorsOutputFile = File(androidValuesOutputDir, "colors.xml")

    doLast {
        if (!colorsSource.exists()) {
            println("CRITICAL ERROR: colors.txt not found at: ${colorsSource.absolutePath}")
            return@doLast
        }
        if (!xmlOutputDir.exists()) xmlOutputDir.mkdirs()
        ktOutputFile.parentFile.mkdirs()
        androidDrawableOutputDir.mkdirs()
        androidValuesOutputDir.mkdirs()

        val colorMap = mutableMapOf<String, String>()
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(colorsSource)
        val nodes = doc.getElementsByTagName("color")

        val ktClassBody = StringBuilder("// AUTO-GENERATED - DO NOT MODIFY\npackage com.rekcode.yahtzee.generated\n\nimport androidx.compose.ui.graphics.Color\n\nobject AppColors {\n")

        for (i in 0 until nodes.length) {
            val el = nodes.item(i) as Element
            val name = el.getAttribute("name")
            val hexRaw = el.textContent.trim()
            val ktHex = hexRaw.replace("#", "0xFF")

            colorMap["@color/$name"] = hexRaw
            ktClassBody.append("    val ${name} = Color($ktHex)\n")
        }
        ktClassBody.append("}\n")

        ktOutputFile.writeText(ktClassBody.toString())

        val androidColorsXml = buildString {
            appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
            appendLine("<resources>")
            for (i in 0 until nodes.length) {
                val el = nodes.item(i) as Element
                val name = el.getAttribute("name")
                val hexRaw = el.textContent.trim()
                appendLine("""    <color name="$name">$hexRaw</color>""")
            }
            appendLine("</resources>")
        }

        androidColorsOutputFile.writeText(androidColorsXml)

        templateDir.walk().filter { it.extension == "xml" }.forEach { template ->
            var composeContent = template.readText()
            colorMap.forEach { (key, hex) ->
                composeContent = composeContent.replace(key, hex)
            }
            File(xmlOutputDir, template.name).writeText(composeContent)

            if (template.name == "splash_dice.xml") {
                val androidContent = template.readText()
                File(androidDrawableOutputDir, template.name).writeText(androidContent)
            }
        }
    }
}

// Trigger the sync before the Compose Resource generator runs
tasks.matching { it.name.contains("generateComposeResClass", ignoreCase = true) }.configureEach {
    dependsOn("syncDesignSystem")
}
// --- END OF CUSTOM SYNC LOGIC ---

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    /**
     * Desktop JVM target for the shared Compose application.
     *
     * This target enables creation of a desktop launcher that hosts the shared
     * App() entry point without introducing platform logic into commonMain.
     */
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = appName
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.core:core-splashscreen:1.0.1")
        }
        getByName("desktopMain").dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("com.rekcode.yahtzee:yahtzee-engine-kmp:1.0.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.rekcode.yahtzee"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets.getByName("main").res.srcDir(
        layout.buildDirectory.dir("generated/designSystem/androidRes")
    )

    defaultConfig {
        applicationId = "com.rekcode.yahtzee"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersionName
    }
    signingConfigs {
        create("release") {
            storeFile = file(providers.gradleProperty("YAHTZEE_UPLOAD_STORE_FILE").get())
            storePassword = providers.gradleProperty("YAHTZEE_UPLOAD_STORE_PASSWORD").get()
            keyAlias = providers.gradleProperty("YAHTZEE_UPLOAD_KEY_ALIAS").get()
            keyPassword = providers.gradleProperty("YAHTZEE_UPLOAD_KEY_PASSWORD").get()
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "com.rekcode.yahtzee.MainKt"

        nativeDistributions {
            packageName = appName
            packageVersion = appVersionName
            description = "Cross-platform Yahtzee application built with Kotlin Multiplatform and Compose Multiplatform."
            copyright = "© 2026 Robert Kennedy. All rights reserved."
            vendor = "Robert Kennedy"
            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Rpm
            )
            windows {
                iconFile.set(project.file("desktop-icons/icon.ico"))
                shortcut = true
                menu = true
                menuGroup = appName
            }
            linux {
                iconFile.set(project.file("desktop-icons/icon.png"))
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

val releaseApkArtifactName = providers.gradleProperty("APP_NAME")
    .zip(providers.gradleProperty("VERSION_NAME")) { name, version ->
        "$name-$version.apk"
    }

tasks.register<Copy>("prepareReleaseApk") {
    dependsOn("assembleRelease")

    from(layout.buildDirectory.dir("outputs/apk/release")) {
        include("*.apk")
        rename { "$appName-$appVersionName.apk" }
    }

    into(layout.buildDirectory.dir("release-artifacts/android"))
}

val releaseExeArtifactName = providers.gradleProperty("APP_NAME")
    .zip(providers.gradleProperty("VERSION_NAME")) { name, version ->
        "$name-$version.exe"
    }

tasks.register<Copy>("prepareReleaseExe") {
    dependsOn("packageReleaseExe")

    from(layout.buildDirectory.file("compose/binaries/main-release/exe/$appName-$appVersionName.exe"))
    into(layout.buildDirectory.dir("release-artifacts/windows"))
    rename("$appName-$appVersionName.exe", releaseExeArtifactName.get())
}

tasks.register<Copy>("prepareReleaseLinux") {
    dependsOn("packageReleaseDeb", "packageReleaseRpm")

    val appName = providers.gradleProperty("APP_NAME").get()
    val version = providers.gradleProperty("VERSION_NAME").get()

    from(layout.buildDirectory.dir("compose/binaries/main-release/deb")) {
        include("*.deb")
        rename { "$appName-$version.deb" }
    }

    from(layout.buildDirectory.dir("compose/binaries/main-release/rpm")) {
        include("*.rpm")
        rename { "$appName-$version.rpm" }
    }

    into(layout.buildDirectory.dir("release-artifacts/linux"))
}

tasks.register("prepareReleaseAndroidArtifacts") {
    dependsOn("prepareReleaseApk")
}

tasks.register("prepareReleaseWindowsArtifacts") {
    dependsOn("prepareReleaseExe")
}

tasks.register("prepareReleaseLinuxArtifacts") {
    dependsOn("prepareReleaseLinux")
}

tasks.register("prepareReleaseArtifacts") {
    dependsOn(
        "prepareReleaseAndroidArtifacts",
        "prepareReleaseWindowsArtifacts",
        "prepareReleaseLinuxArtifacts"
    )
}