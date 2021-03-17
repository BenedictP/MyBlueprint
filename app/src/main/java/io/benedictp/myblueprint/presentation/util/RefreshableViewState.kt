package io.benedictp.myblueprint.presentation.util

import java.io.Serializable

sealed class RefreshableViewState<out Data : Serializable, out Error : Serializable> : Serializable {
	object Init : RefreshableViewState<Nothing, Nothing>(), Serializable
	class Loading<out Data : Serializable>(val data: Data? = null) : RefreshableViewState<Data, Nothing>(), Serializable
	class Data<out Data : Serializable>(val data: Data) : RefreshableViewState<Data, Nothing>(), Serializable
	class Error<out Error : Serializable, out Data : Serializable>(val error: Error, val data: Data? = null) :
		RefreshableViewState<Data, Error>(),
		Serializable
}

fun <Data : Serializable, Error : Serializable> RefreshableViewState<Data, Error>.getData(): Data? {
	return when (this) {
		RefreshableViewState.Init -> null
		is RefreshableViewState.Loading -> this.data
		is RefreshableViewState.Data -> this.data
		is RefreshableViewState.Error -> this.data
	}
}

sealed class ViewState<out Data : Serializable, out Error : Serializable> : Serializable {
	object Init : ViewState<Nothing, Nothing>(), Serializable
	object Loading : ViewState<Nothing, Nothing>(), Serializable
	class Data<out Data : Serializable>(val data: Data) : ViewState<Data, Nothing>(), Serializable
	class Error<out Error : Serializable>(val error: Error) : ViewState<Nothing, Error>(), Serializable
}