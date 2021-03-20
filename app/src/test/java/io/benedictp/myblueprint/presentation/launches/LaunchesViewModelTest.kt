package io.benedictp.myblueprint.presentation.launches

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.benedictp.domain.model.Launch
import io.benedictp.domain.usecase.GetUpcomingLaunchesUseCase
import io.benedictp.domain.util.Failure
import io.benedictp.domain.util.Success
import io.benedictp.myblueprint.presentation.util.RefreshableViewState
import io.benedictp.myblueprint.util.CoroutinesTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LaunchesViewModelTest {

	@MockK
	private lateinit var savedStateHandler: SavedStateHandle

	@MockK
	private lateinit var getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCase

	@get:Rule
	var coroutinesTestRule = CoroutinesTestRule()

	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@Before
	fun before() {
		MockKAnnotations.init(this)
	}

	@Test
	fun `loadUpcomingLaunches returnsListOfLaunches upcomingLaunchesLiveDataShouldHaveCorrectData`() =
		coroutinesTestRule.testDispatcher.runBlockingTest {
			//GIVEN
			every {
				savedStateHandler.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(
					LaunchesViewModel.LAUNCHES_KEY,
					RefreshableViewState.Init
				)
			} returns MutableLiveData(
				RefreshableViewState.Init
			)
			coEvery { getUpcomingLaunchesUseCase.invoke() } coAnswers { Success(arrayListOf(Launch("name", "id"))) }
			val viewModel = LaunchesViewModel(savedStateHandler, getUpcomingLaunchesUseCase)

			//WHEN
			viewModel.loadUpcomingLaunches()

			//THEN
			assert(viewModel.upcomingLaunchesLiveData.value is RefreshableViewState.Data)
			val launches = (viewModel.upcomingLaunchesLiveData.value as RefreshableViewState.Data).data
			assert(launches.size == 1)
			val firstLaunch = launches[0]
			assert(firstLaunch.id == "id")
			assert(firstLaunch.name == "name")
		}

	@Test
	fun `loadUpcomingLaunches returnsError upcomingLaunchesLiveDataShouldHaveError`() =
		coroutinesTestRule.testDispatcher.runBlockingTest {
			//GIVEN
			every {
				savedStateHandler.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(
					LaunchesViewModel.LAUNCHES_KEY,
					RefreshableViewState.Init
				)
			} returns MutableLiveData(
				RefreshableViewState.Init
			)
			coEvery { getUpcomingLaunchesUseCase.invoke() } coAnswers { Failure(Throwable("Test")) }
			val viewModel = LaunchesViewModel(savedStateHandler, getUpcomingLaunchesUseCase)

			//WHEN
			viewModel.loadUpcomingLaunches()

			//THEN
			assert(viewModel.upcomingLaunchesLiveData.value is RefreshableViewState.Error)
			val value = (viewModel.upcomingLaunchesLiveData.value as RefreshableViewState.Error).error
			assert(value.message == "Test")
		}

	@Test
	fun `initViewModel upcomingLaunchesLiveDataStartsWithInitState upcomingLaunchesLiveDataStartsWithInitState`() =
		coroutinesTestRule.testDispatcher.runBlockingTest {
			//GIVEN
			every {
				savedStateHandler.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(
					LaunchesViewModel.LAUNCHES_KEY,
					RefreshableViewState.Init
				)
			} returns MutableLiveData(
				RefreshableViewState.Init
			)
			coEvery { getUpcomingLaunchesUseCase.invoke() } coAnswers { Failure(Throwable("Test")) }
			val viewModel = LaunchesViewModel(savedStateHandler, getUpcomingLaunchesUseCase)

			//WHEN
			//nothing

			//THEN
			assert(viewModel.upcomingLaunchesLiveData.value is RefreshableViewState.Init)
		}

	@Test
	fun `loadUpcomingLaunches upcomingLaunchesErrorAndOldDataAvailable upcomingLaunchesLiveDataIsInStateErrorAndOldLaunchesAvailable`() =
		coroutinesTestRule.testDispatcher.runBlockingTest {
			//GIVEN
			every {
				savedStateHandler.getLiveData<RefreshableViewState<ArrayList<Launch>, Throwable>>(
					LaunchesViewModel.LAUNCHES_KEY,
					RefreshableViewState.Init
				)
			} returns MutableLiveData(
				RefreshableViewState.Data(arrayListOf(Launch("name", "id")))
			)
			coEvery { getUpcomingLaunchesUseCase.invoke() } coAnswers { Failure(Throwable("Test")) }
			val viewModel = LaunchesViewModel(savedStateHandler, getUpcomingLaunchesUseCase)

			//WHEN
			viewModel.loadUpcomingLaunches()

			//THEN
			val viewState = viewModel.upcomingLaunchesLiveData.value
			assert(viewState is RefreshableViewState.Error)
			val launches = (viewState as RefreshableViewState.Error).data
			val error = viewState.error
			assert(error.message == "Test")
			assert(launches != null)
			assert(launches!!.size == 1)
			assert(launches[0].id == "id")
			assert(launches[0].name == "name")
		}

}