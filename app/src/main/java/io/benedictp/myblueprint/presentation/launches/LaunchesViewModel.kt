package io.benedictp.myblueprint.presentation.launches

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.benedictp.domain.model.Launch
import io.benedictp.domain.usecase.GetUpcomingLaunchesUseCase
import io.benedictp.myblueprint.presentation.util.ViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAUNCHES_KEY = "launches"

@HiltViewModel
class LaunchesViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCase
) : ViewModel() {

	val upcomingLaunchesLiveData: MutableLiveData<ViewState<List<Launch>, Throwable>>

	init {
		val savedLaunches = savedStateHandle.get<List<Launch>>(LAUNCHES_KEY)
		upcomingLaunchesLiveData = if (savedLaunches == null) {
			MutableLiveData(ViewState.Init)
		} else {
			MutableLiveData(ViewState.Data(savedLaunches))
		}
	}

	fun loadUpcomingLaunches() {
		viewModelScope.launch {
			upcomingLaunchesLiveData.value = ViewState.Loading
			getUpcomingLaunchesUseCase()
				.onSuccess {
					upcomingLaunchesLiveData.value = ViewState.Data(it)
				}
				.onFailure {
					upcomingLaunchesLiveData.value = ViewState.Error(it)
				}
		}
	}

	override fun onCleared() {
		super.onCleared()
		upcomingLaunchesLiveData.value?.let { upcomingLaunchesViewState ->
			if (upcomingLaunchesViewState is ViewState.Data) {
				savedStateHandle.set(LAUNCHES_KEY, upcomingLaunchesViewState.value)
			}
		}
	}

}