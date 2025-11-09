package me.dogs.fivenine.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TerminalGreen,
    secondary = TerminalGreenDark,
    background = TerminalBlack,
    onBackground = TerminalGreen,
    onSurface = TerminalGreen
)

@Composable
fun FivenineTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}