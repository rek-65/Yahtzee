
package com.rekcode.yahtzee.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.rekcode.yahtzee.generated.AppColors

private val YahtzeeColorScheme = lightColorScheme(
    primary = AppColors.table_rail,
    onPrimary = AppColors.text_on_table,
    secondary = AppColors.text_section_header,
    onSecondary = AppColors.text_on_table,
    background = AppColors.table_felt,
    onBackground = AppColors.text_on_table,
    surface = AppColors.table_felt,
    onSurface = AppColors.text_on_table
)

/**
 * Applies the application-wide Material 3 theme configuration.
 *
 * This theme uses a single fixed color scheme for the entire application.
 * It does not vary by platform theme or system dark mode.
 *
 * @param content Composable content that will receive the theme styling.
 */
@Composable
fun YahtzeeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = YahtzeeColorScheme,
        typography = Typography,
        content = content
    )
}