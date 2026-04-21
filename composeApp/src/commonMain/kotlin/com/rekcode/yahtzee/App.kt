
package com.rekcode.yahtzee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rekcode.yahtzee.generated.AppColors
import com.rekcode.yahtzee.ui.root.AppRoot
import com.rekcode.yahtzee.ui.theme.UiConstants
import com.rekcode.yahtzee.ui.theme.YahtzeeTheme

/**
 * Shared application entry composable for the Yahtzee app.
 *
 * Responsibilities:
 * - Applies the global app theme.
 * - Applies the global app background.
 * - Resolves a single centered viewport using the canonical app aspect ratio.
 * - Measures AppRoot strictly inside that resolved viewport.
 *
 * Architectural Notes:
 * - This composable belongs in commonMain.
 * - Platform launchers must remain thin and call this composable only.
 * - The viewport uses maximum height when the available width can support it.
 * - Otherwise, the viewport uses maximum width and reduces height only as required.
 * - All child UI is measured from the viewport, not from the outer window.
 *
 * @param onExitRequested Callback invoked when the user requests to exit the application.
 */
@Composable
fun App(
    onExitRequested: () -> Unit
) {
    YahtzeeTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.table_felt)
        ) {
            val aspectRatio =
                UiConstants.AppAspectRatioWidth / UiConstants.AppAspectRatioHeight

            val viewportWidth =
                if (maxWidth / maxHeight > aspectRatio) {
                    maxHeight * aspectRatio
                } else {
                    maxWidth
                }

            val viewportHeight =
                if (maxWidth / maxHeight > aspectRatio) {
                    maxHeight
                } else {
                    maxWidth / aspectRatio
                }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(
                        width = viewportWidth,
                        height = viewportHeight
                    )
                ) {
                    AppRoot(
                        onExitRequested = onExitRequested
                    )
                }
            }
        }
    }
}