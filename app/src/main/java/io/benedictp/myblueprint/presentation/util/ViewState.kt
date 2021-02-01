package io.benedictp.myblueprint.presentation.util

import java.io.Serializable

sealed class ViewState<out Data : Serializable, out Error : Serializable> : Serializable {
	object Init : ViewState<Nothing, Nothing>(), Serializable
	class Loading<out Data : Serializable>(val data: Data?) : ViewState<Data, Nothing>(), Serializable
	class Data<out Data : Serializable>(val data: Data) : ViewState<Data, Nothing>(), Serializable
	class Error<out Error : Serializable, out Data : Serializable>(val error: Error, val data: Data?) : ViewState<Data, Error>(),
		Serializable
}

fun <Data : Serializable, Error : Serializable> ViewState<Data, Error>.getData(): Data? {
	return when (this) {
		ViewState.Init -> null
		is ViewState.Loading -> this.data
		is ViewState.Data -> this.data
		is ViewState.Error -> this.data
	}
}