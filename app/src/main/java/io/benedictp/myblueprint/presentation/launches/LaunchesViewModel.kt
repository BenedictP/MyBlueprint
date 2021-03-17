package io.benedictp.myblueprint.presentation.launches

import androidx.lifecycle.LiveData
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

	private val _upcomingLaunchesLiveData =
		savedStateHandle.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(LAUNCHES_KEY, RefreshableViewState.Init)
	val upcomingLaunchesLiveData: LiveData<RefreshableViewState<ArrayList<Launch>, Throwable>> = _upcomingLaunchesLiveData

	fun loadUpcomingLaunches() {
		viewModelScope.launch {
			_upcomingLaunchesLiveData.value = RefreshableViewState.Loading(_upcomingLaunchesLiveData.value?.getData())
			getUpcomingLaunchesUseCase()
				.onSuccess {
					_upcomingLaunchesLiveData.value = RefreshableViewState.Data(it)
				}
				.onFailure {
					_upcomingLaunchesLiveData.value = RefreshableViewState.Error(it, _upcomingLaunchesLiveData.value?.getData())
				}
		}
	}

}