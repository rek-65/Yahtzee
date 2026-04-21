
package com.rekcode.yahtzee.ui.content

/**
 * Defines the Markdown syntax tokens supported by the in-app instructions
 * renderer.
 *
 * This object centralizes parsing markers and display replacements used by the
 * instructions presentation layer so that screen code remains focused on
 * loading and rendering responsibilities.
 *
 * The supported syntax is intentionally limited to the subset required by the
 * current instructions document.
 */
object InstructionsMarkdownSyntax {

    /**
     * Prefix used to identify a top-level document title line.
     */
    const val DocumentTitlePrefix: String = "# "

    /**
     * Prefix used to identify a section heading line.
     */
    const val SectionHeadingPrefix: String = "## "

    /**
     * Prefix used to identify a bullet entry line.
     */
    const val BulletPrefix: String = "- "

    /**
     * Display prefix rendered for bullet entries after parsing.
     */
    const val BulletDisplayPrefix: String = "• "

    /**
     * Marker used to identify bold text spans.
     */
    const val BoldMarker: String = "**"

    /**
     * Sentinel value returned when a token search fails.
     */
    const val NotFoundIndex: Int = -1
}