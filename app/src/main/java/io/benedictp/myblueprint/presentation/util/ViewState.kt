package io.benedictp.myblueprint.presentation.util

sealed class ViewState<out Data, out Error> {
	object Init : ViewState<Nothing, Nothing>()
	object Loading : ViewState<Nothing, Nothing>()
	class Data<out Data>(val value: Data) : ViewState<Data, Nothing>()
	class Error<out Error>(val error: Error) : ViewState<Nothing, Error>()
}