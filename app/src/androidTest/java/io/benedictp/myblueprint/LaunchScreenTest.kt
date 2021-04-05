package io.benedictp.myblueprint

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.android.material.composethemeadapter.MdcTheme
import com.karumi.shot.ScreenshotTest
import io.benedictp.domain.model.Launch
import io.benedictp.myblueprint.presentation.launches.LaunchesScreen
import io.benedictp.myblueprint.presentation.util.RefreshableViewState
import org.junit.Rule
import org.junit.Test

class LaunchScreenTest : ScreenshotTest {

	@get:Rule
	val composeTestRule = createComposeRule()

	@Test
	fun launchScreenInitState() {
		composeTestRule.setContent {
			MdcTheme {
				LaunchesScreen(
					launchesViewState = mutableStateOf(RefreshableViewState.Init),
					topBarTitle = "LaunchesScreenInitState",
					onRefresh = {},
					onInitialLoading = {},
					onLaunchClicked = {},
					scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
				)
			}
		}
		compareScreenshot(composeTestRule)
	}

	@Test
	fun launchScreenDataState() {
		composeTestRule.setContent {
			MdcTheme {
				LaunchesScreen(
					launchesViewState = mutableStateOf(RefreshableViewState.Data(arrayListOf(
						Launch("name", "id"),
						Launch("name2", "id2")
					))),
					topBarTitle = "LaunchesScreenDataState",
					onRefresh = {},
					onInitialLoading = {},
					onLaunchClicked = {},
					scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
				)
			}
		}
		compareScreenshot(composeTestRule)
	}
}