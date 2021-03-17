package io.benedictp.myblueprint.presentation.composable

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import io.benedictp.myblueprint.presentation.theme.JetnewsTheme

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    JetnewsTheme(darkTheme = darkTheme) {
        Surface {
            content()
        }
    }
}