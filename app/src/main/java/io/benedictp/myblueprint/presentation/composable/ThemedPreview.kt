package io.benedictp.myblueprint.presentation.composable

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
internal fun ThemedPreview(
    content: @Composable () -> Unit
) {
    MdcTheme {
        Surface {
            content()
        }
    }
}