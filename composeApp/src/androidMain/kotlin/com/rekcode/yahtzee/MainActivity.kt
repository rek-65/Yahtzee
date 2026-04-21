
package com.rekcode.yahtzee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * Android launcher for the Yahtzee application.
 *
 * Responsibilities:
 * - Serve as the Android platform entry point.
 * - Host the shared [App] composable.
 * - Provide the Android-specific exit callback.
 *
 * Architectural Notes:
 * - This class must remain Android-specific.
 * - Shared UI and navigation logic live in commonMain.
 * - This launcher should contain no app flow or game logic.
 */
class MainActivity : ComponentActivity() {

    /**
     * Android lifecycle entry point.
     *
     * Initializes Compose and hosts the shared application entry composable.
     *
     * @param savedInstanceState Previously saved instance state, or null if none exists.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        setContent {
            App(
                onExitRequested = { finish() }
            )
        }
    }
}