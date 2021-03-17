package io.benedictp.myblueprint.presentation.composable

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.benedictp.myblueprint.R

@Preview
@Composable
fun MyBlueprintTopAppBar(@PreviewParameter(FakeTitleProvider::class) title: String) {
	TopAppBar(
		title = {
			Text(text = title)
		}
	)
}

@Composable
fun MyBlueprintTopAppBar(
	title: String,
	onBackClicked: () -> Unit
) {
	TopAppBar(
		title = {
			Text(text = title)
		},
		navigationIcon = {
			IconButton(onClick = onBackClicked) {
				Icon(
					imageVector = Icons.Filled.ArrowBack,
					contentDescription = stringResource(R.string.navigate_up)
				)
			}
		}
	)
}

class FakeTitleProvider : PreviewParameterProvider<String> {
	override val values: Sequence<String>
		get() = sequenceOf("Title")
}