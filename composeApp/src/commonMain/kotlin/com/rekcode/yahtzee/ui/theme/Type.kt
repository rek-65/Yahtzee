
package com.rekcode.yahtzee.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Provides the Material 3 typography configuration for the application.
 *
 * This object defines the default text styling used across the UI layer,
 * including font family, weight, size, line height, and letter spacing.
 *
 * The configuration is centralized to ensure consistency and maintainability.
 */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)