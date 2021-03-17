package io.benedictp.myblueprint.presentation.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.composethemeadapter.MdcTheme
import com.puculek.pulltorefresh.PullToRefresh
import dagger.hilt.android.AndroidEntryPoint
import io.benedictp.domain.model.Launch
import io.benedictp.myblueprint.R
import io.benedictp.myblueprint.presentation.composable.MyBlueprintTopAppBar
import io.benedictp.myblueprint.presentation.composable.ThemedPreview
import io.benedictp.myblueprint.presentation.util.RefreshableViewState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchesFragment : Fragment() {

	private val viewModel: LaunchesViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				MdcTheme {
					val launchesViewState: State<RefreshableViewState<ArrayList<Launch>, Throwable>> =
						viewModel.upcomingLaunchesLiveData.observeAsState(RefreshableViewState.Init)

					LaunchesScreen(
						launchesViewState = launchesViewState,
						topBarTitle = getString(R.string.launches),
						onRefresh = viewModel::loadUpcomingLaunches,
						onInitialLoading = viewModel::loadUpcomingLaunches,
						onLaunchClicked = { Toast.makeText(requireContext(), "Clicked: $it", Toast.LENGTH_LONG).show() }
					)
				}
			}
		}
	}

	@Composable
	private fun LaunchesScreen(
		launchesViewState: State<RefreshableViewState<ArrayList<Launch>, Throwable>>,
		topBarTitle: String,
		onRefresh: () -> Unit,
		onInitialLoading: () -> Unit,
		onLaunchClicked: (Launch) -> Unit
	) {
		val scaffoldState = rememberScaffoldState()
		val scope = rememberCoroutineScope()
		Scaffold(
			topBar = { MyBlueprintTopAppBar(title = topBarTitle) },
			scaffoldState = scaffoldState
		) { innerPadding ->
			LaunchesScreenBody(
				innerPadding = innerPadding,
				launchesViewState = launchesViewState,
				onRefresh = onRefresh,
				initialLoad = onInitialLoading,
				onLaunchClicked = onLaunchClicked,
				showErrorRetrySnackbar = {
					scope.launch {
						when (scaffoldState.snackbarHostState.showSnackbar("Error: $it", actionLabel = "Retry")) {
							SnackbarResult.ActionPerformed -> onRefresh()
							SnackbarResult.Dismissed -> {
								//ignore
							}
						}
					}
				})
		}
	}

	@Composable
	fun LaunchesScreenBody(
		innerPadding: PaddingValues,
		launchesViewState: State<RefreshableViewState<ArrayList<Launch>, Throwable>>,
		onRefresh: () -> Unit,
		initialLoad: () -> Unit,
		onLaunchClicked: (Launch) -> Unit,
		showErrorRetrySnackbar: (String) -> Unit
	) {
		PullToRefresh(
			modifier = Modifier.padding(innerPadding),
			isRefreshing = launchesViewState.value is RefreshableViewState.Loading,
			onRefresh = onRefresh
		) {
			when (launchesViewState.value) {
				RefreshableViewState.Init -> initialLoad()
				is RefreshableViewState.Loading -> {
					val data = (launchesViewState.value as RefreshableViewState.Loading<ArrayList<Launch>>).data
					if (data != null && data.isNotEmpty()) {
						ShowData(
							launches = data,
							onLaunchClicked = onLaunchClicked
						)
					}
				}
				is RefreshableViewState.Data -> {
					ShowData(
						launches = (launchesViewState.value as RefreshableViewState.Data<ArrayList<Launch>>).data,
						onLaunchClicked = onLaunchClicked
					)
				}
				is RefreshableViewState.Error -> {
					ShowError(
						showErrorRetrySnackbar = showErrorRetrySnackbar,
						launches = (launchesViewState.value as RefreshableViewState.Error<Throwable, ArrayList<Launch>>).data,
						onLaunchClicked = onLaunchClicked,
						error = (launchesViewState.value as RefreshableViewState.Error<Throwable, ArrayList<Launch>>).error,
						onRefresh = onRefresh
					)
				}
			}
		}
	}

	@Composable
	fun ShowData(
		launches: List<Launch>,
		onLaunchClicked: (Launch) -> Unit
	) {
		if (launches.isEmpty()) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.fillMaxHeight()
			) {
				Text("List is empty")
			}
		} else {
			LazyColumn(
				modifier = Modifier
					.fillMaxWidth()
					.fillMaxHeight()
			) {
				items(launches) { launch ->
					Column(modifier = Modifier
						.fillMaxWidth()
						.clickable { onLaunchClicked(launch) }
						.padding(16.dp)) {
						Text(launch.name)
					}
				}
			}
		}
	}

	@Composable
	fun ShowError(
		showErrorRetrySnackbar: (String) -> Unit,
		launches: List<Launch>?,
		onLaunchClicked: (Launch) -> Unit,
		error: Throwable,
		onRefresh: () -> Unit
	) {
		if (launches != null) {
			ShowData(launches = launches, onLaunchClicked = onLaunchClicked)
			showErrorRetrySnackbar("${error.message}")
		} else {
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.fillMaxHeight()
					.padding(16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text("Error: ${error.message}")
				Spacer(
					modifier = Modifier
						.fillMaxWidth()
						.height(16.dp)
				)
				Button(onClick = onRefresh) {
					Text("Retry")
				}
			}
		}
	}

	@Preview
	@Composable
	fun PreviewInitialEmpty() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Init
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewInitialEmptyDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Init
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewLoadingEmpty() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Loading()
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewLoadingEmptyDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Loading()
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewData() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Data(
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewDataDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Data(
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewError() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Error(IllegalStateException("Message"))
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewErrorDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Error(IllegalStateException("Message"))
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewLoadingWithData() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Loading(
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewLoadingWithDataDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Loading(
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewErrorWithData() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Error(
							IllegalStateException("Message"),
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

	@Preview
	@Composable
	fun PreviewErrorWithDataDarkMode() {
		ThemedPreview() {
			LaunchesScreen(
				launchesViewState = object : State<RefreshableViewState<ArrayList<Launch>, Throwable>> {
					override val value: RefreshableViewState<ArrayList<Launch>, Throwable>
						get() = RefreshableViewState.Error(
							IllegalStateException("Message"),
							arrayListOf(
								Launch("Launch 1", "id"),
								Launch("Launch 2", "id"),
								Launch("Launch 2", "id"),
							)
						)
				},
				topBarTitle = "Launches",
				onRefresh = {},
				onInitialLoading = {},
				onLaunchClicked = {}
			)
		}
	}

}