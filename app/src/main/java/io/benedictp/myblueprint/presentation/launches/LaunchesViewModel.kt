package io.benedictp.myblueprint.presentation.launches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.benedictp.domain.model.Launch
import io.benedictp.domain.usecase.GetUpcomingLaunchesUseCase
import io.benedictp.myblueprint.presentation.util.RefreshableViewState
import io.benedictp.myblueprint.presentation.util.getData
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAUNCHES_KEY = "launches"

@HiltViewModel
class LaunchesViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCase
) : ViewModel() {

	val upcomingLaunchesLiveData =
		savedStateHandle.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(LAUNCHES_KEY, RefreshableViewState.Init)

	fun loadUpcomingLaunches() {
		viewModelScope.launch {
			upcomingLaunchesLiveData.value = RefreshableViewState.Loading(upcomingLaunchesLiveData.value?.getData())
			getUpcomingLaunchesUseCase()
				.onSuccess {
					upcomingLaunchesLiveData.value = RefreshableViewState.Data(it)
				}
				.onFailure {
					upcomingLaunchesLiveData.value = RefreshableViewState.Error(it, upcomingLaunchesLiveData.value?.getData())
				}
		}
	}

}