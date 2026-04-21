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
composeResources/files/designSystem/colors.txt

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
- iOS

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
