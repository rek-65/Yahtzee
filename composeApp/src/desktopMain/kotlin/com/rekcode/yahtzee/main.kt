
package com.rekcode.yahtzee

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Desktop entry point for the Yahtzee application.
 *
 * Responsibilities:
 * - Launch the desktop application process.
 * - Create the desktop window host for the shared App() composable.
 * - Provide the desktop-specific exit callback.
 *
 * Architectural Notes:
 * - This file belongs only in the desktop source set.
 * - Shared UI and navigation logic remain in commonMain.
 * - Platform-specific code must not implement viewport sizing logic.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Yahtzee",
        resizable = true
    ) {
        window.minimumSize = java.awt.Dimension(350, 770)
        App(
            onExitRequested = ::exitApplication
        )
    }
}