# 🎲 Yahtzee — Kotlin Multiplatform

A professionally architected Yahtzee application built using Kotlin Multiplatform and Compose Multiplatform, backed by a fully decoupled, platform-independent game engine.

## 🧠 Overview

This project is a UI layer that consumes an external Yahtzee engine.

- UI handles display and interaction only
- Game engine handles rules, scoring, and state
- Designed for multi-platform support with a shared UI

## 🧩 Architecture

Platform Entry (Android / Desktop / iOS)
            ↓
           App
            ↓
         AppRoot
            ↓
     Screen Composables
            ↓
     GameCoordinator
            ↓
   External Game Engine

## 🎯 Design Rules

### UI
- Stateless where possible
- No inline UI text
- No hardcoded layout values

### Resources
- All text → strings.xml
- All colors → generated from colors.txt
- All layout values → Dimens / UiConstants

### Performance
- No unnecessary recomposition
- Lightweight animations only
- No blocking UI

## 🎨 Design System

### Colors
composeApp/src/commonMain/designSystem/colors.txt

- Stored as XML (with .txt extension)
- Processed by Gradle
- Generates AppColors.kt

### Dimensions
- Base values → Dimens.kt
- Runtime scaling → UiDimens
- Driven by viewport height

## 🧾 Instructions System

composeResources/files/instructions_en.md

- Loaded via Res.readBytes(...)
- Parsed using InstructionsMarkdownSyntax.kt

## 🎮 Game Engine

External dependency:
com.rekcode.yahtzee:yahtzee-engine-kmp

- Treated as a third-party library
- UI uses public API only

## 🖥️ Platforms

- Android
- Desktop (JVM)
- iOS (framework)

### iOS Framework

iOS support is provided as a compiled Kotlin Multiplatform framework.

- Built using Kotlin/Native
- Generated via CI on macOS runners
- Output is a `.framework` artifact

This framework is included in GitHub Release assets as a compressed (.zip) build artifact.

Build output path:
composeApp/build/bin/ios*/releaseFramework/

### iOS Usage

The iOS framework is not a standalone application and cannot be installed directly on a device.

To use:

1. Download and extract the provided framework archive
2. Create or open an iOS project in Xcode
3. Import the extracted `.framework`
4. Link it to your app target
5. Sign the app using an Apple account (free or paid)

### Limitations

- No `.ipa` is produced
- No TestFlight or App Store distribution
- Signing and device deployment must be handled in Xcode

This is intentional and aligns with the architectural goal of keeping the UI and platform layers decoupled from the shared game engine.

## 🏗️ Build

### Requirements
- JDK 17
- Gradle Wrapper

### Android
./gradlew :composeApp:assembleDebug

### Desktop
./gradlew :composeApp:run

## 🔐 Environment Setup

Set JAVA_HOME:
C:\Program Files\Java\jdk-17

## 🚫 Repository Rules

- Gradle-driven
- CI/CD ready
- IDE-independent

Ignored:
- .idea/
- .gradle/
- build/
- local.properties

No build artifacts are committed.

## 📌 Notable Features

### Viewport-Based Layout
- Fixed aspect ratio
- UI scales within a controlled container

### Dice System
- Stateless rendering
- Controlled animation

### Score System
- Engine-driven
- Dynamic rendering

## 🧼 Code Quality

- Full KDoc documentation
- No unnecessary comments
- No magic numbers
- Centralized constants

## 📚 Related Project

Yahtzee Engine (KMP)

## 🧠 Notes

This project emphasizes clean architecture, maintainability, and platform independence.
