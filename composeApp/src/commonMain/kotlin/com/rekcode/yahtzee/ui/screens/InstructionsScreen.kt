
package com.rekcode.yahtzee.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import com.rekcode.yahtzee.generated.AppColors
import com.rekcode.yahtzee.generated.resources.Res
import com.rekcode.yahtzee.generated.resources.action_exit
import com.rekcode.yahtzee.generated.resources.instructions_document_path
import com.rekcode.yahtzee.ui.theme.UiConstants
import com.rekcode.yahtzee.ui.theme.rememberUiDimens
import org.jetbrains.compose.resources.stringResource
import com.rekcode.yahtzee.ui.content.InstructionsMarkdownSyntax

/**
 * Displays the in-app instructions document.
 *
 * This composable is responsible for loading and rendering the instruction
 * content from the shared Markdown resource file.
 *
 * @param onCloseRequested Callback invoked when the user requests to close the instructions screen.
 */
@Composable
fun InstructionsScreen(
    onCloseRequested: () -> Unit
) {
    val instructionsDocumentPath = stringResource(Res.string.instructions_document_path)
    var contentLines by remember { mutableStateOf<List<InstructionContentLine>>(emptyList()) }

    LaunchedEffect(instructionsDocumentPath) {
        val rawContent = Res.readBytes(instructionsDocumentPath).decodeToString()
        contentLines = parseInstructionContent(rawContent)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val uiDimens = rememberUiDimens(maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.table_felt)
                .padding(uiDimens.screenPadding),
            verticalArrangement = Arrangement.spacedBy(uiDimens.itemStackSpacing)
        ) {
            Spacer(modifier = Modifier.height(uiDimens.screenPadding))

            Box(
                modifier = Modifier
                    .weight(UiConstants.RowSpacerWeight)
                    .background(AppColors.dice_face_base)
                    .padding(uiDimens.screenPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(uiDimens.scoreRowSpacing)
                ) {
                    contentLines.forEach { line ->
                        when (line) {
                            is InstructionContentLine.DocumentTitle -> {
                                Text(
                                    text = line.text,
                                    fontSize = uiDimens.playerHeaderTextSize,
                                    fontWeight = UiConstants.ButtonLabelFontWeight,
                                    color = AppColors.black
                                )
                            }

                            is InstructionContentLine.SectionHeading -> {
                                Text(
                                    text = line.text,
                                    fontSize = uiDimens.buttonLabelFontSize,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.black
                                )
                            }

                            is InstructionContentLine.BulletEntry -> {
                                Text(
                                    text = line.text,
                                    fontSize = uiDimens.scoreTextSize,
                                    fontWeight = FontWeight.Medium,
                                    color = AppColors.black
                                )
                            }

                            is InstructionContentLine.Paragraph -> {
                                Text(
                                    text = line.text,
                                    fontSize = uiDimens.scoreTextSize,
                                    fontWeight = FontWeight.Medium,
                                    color = AppColors.black
                                )
                            }

                            InstructionContentLine.BlankLine -> {
                                Spacer(modifier = Modifier.height(uiDimens.scoreRowSpacing))
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onCloseRequested,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.table_rail,
                    contentColor = AppColors.text_on_table
                )
            ) {
                Text(
                    text = stringResource(Res.string.action_exit),
                    fontWeight = UiConstants.ButtonLabelFontWeight,
                    fontSize = uiDimens.buttonLabelFontSize
                )
            }
        }
    }
}

/**
 * Represents one rendered line of instruction content.
 */
private sealed interface InstructionContentLine {

    /**
     * Top-level document title line.
     *
     * @param text Display text for the title.
     */
    data class DocumentTitle(
        val text: AnnotatedString
    ) : InstructionContentLine

    /**
     * Section heading line.
     *
     * @param text Display text for the heading.
     */
    data class SectionHeading(
        val text: AnnotatedString
    ) : InstructionContentLine

    /**
     * Bullet entry line.
     *
     * @param text Display text for the bullet item.
     */
    data class BulletEntry(
        val text: AnnotatedString
    ) : InstructionContentLine

    /**
     * Standard paragraph line.
     *
     * @param text Display text for the paragraph.
     */
    data class Paragraph(
        val text: AnnotatedString
    ) : InstructionContentLine

    /**
     * Blank separator line.
     */
    data object BlankLine : InstructionContentLine
}

/**
 * Parses the instructions Markdown content into display lines understood by the screen.
 *
 * Supported syntax is limited to the subset used by the in-app manual:
 * top-level headings, section headings, bullet entries, blank lines, and bold markers.
 *
 * @param rawContent Raw Markdown document text.
 * @return Parsed display lines for rendering.
 */
private fun parseInstructionContent(rawContent: String): List<InstructionContentLine> {
    return rawContent
        .lineSequence()
        .map { line -> parseInstructionLine(line) }
        .toList()
}

/**
 * Parses a single Markdown line into a display line model.
 *
 * @param rawLine Raw line text from the Markdown document.
 * @return Parsed display line.
 */
private fun parseInstructionLine(rawLine: String): InstructionContentLine {
    val trimmedLine = rawLine.trim()

    return when {
        trimmedLine.isEmpty() -> InstructionContentLine.BlankLine
        trimmedLine.startsWith(InstructionsMarkdownSyntax.DocumentTitlePrefix) -> {
            InstructionContentLine.DocumentTitle(
                text = buildMarkdownAnnotatedString(
                    trimmedLine.removePrefix(InstructionsMarkdownSyntax.DocumentTitlePrefix)
                )
            )
        }

        trimmedLine.startsWith(InstructionsMarkdownSyntax.SectionHeadingPrefix) -> {
            InstructionContentLine.SectionHeading(
                text = buildMarkdownAnnotatedString(
                    trimmedLine.removePrefix(InstructionsMarkdownSyntax.SectionHeadingPrefix)
                )
            )
        }

        trimmedLine.startsWith(InstructionsMarkdownSyntax.BulletPrefix) -> {
            InstructionContentLine.BulletEntry(
                text = buildMarkdownAnnotatedString(
                    InstructionsMarkdownSyntax.BulletDisplayPrefix + trimmedLine.removePrefix(InstructionsMarkdownSyntax.BulletPrefix)
                )
            )
        }

        else -> {
            InstructionContentLine.Paragraph(
                text = buildMarkdownAnnotatedString(trimmedLine)
            )
        }
    }
}

/**
 * Builds annotated display text from a Markdown fragment that may contain bold markers.
 *
 * @param sourceText Source text fragment.
 * @return Annotated text with supported emphasis applied.
 */
private fun buildMarkdownAnnotatedString(sourceText: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        while (currentIndex < sourceText.length) {
            val openingIndex = sourceText.indexOf(
                string = InstructionsMarkdownSyntax.BoldMarker,
                startIndex = currentIndex
            )

            if (openingIndex == InstructionsMarkdownSyntax.NotFoundIndex) {
                append(sourceText.substring(currentIndex))
                break
            }

            append(sourceText.substring(currentIndex, openingIndex))

            val contentStartIndex = openingIndex + InstructionsMarkdownSyntax.BoldMarker.length
            val closingIndex = sourceText.indexOf(
                string = InstructionsMarkdownSyntax.BoldMarker,
                startIndex = contentStartIndex
            )

            if (closingIndex == InstructionsMarkdownSyntax.NotFoundIndex) {
                append(sourceText.substring(openingIndex))
                break
            }

            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(sourceText.substring(contentStartIndex, closingIndex))
            pop()

            currentIndex = closingIndex + InstructionsMarkdownSyntax.BoldMarker.length
        }
    }
}

