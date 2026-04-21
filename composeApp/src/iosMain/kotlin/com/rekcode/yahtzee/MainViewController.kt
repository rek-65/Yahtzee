
package com.rekcode.yahtzee

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Provides the iOS entry point for the Compose Multiplatform UI.
 *
 * Bridges the shared application composable into a UIKit-compatible
 * view controller for use within the iOS application.
 */
fun MainViewController() = ComposeUIViewController {
    App(
        onExitRequested = {}
    )
}