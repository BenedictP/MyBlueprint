package io.benedictp.myblueprint.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import io.benedictp.myblueprint.R
import io.benedictp.myblueprint.presentation.launches.Launches
import io.benedictp.myblueprint.presentation.rockets.Rockets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val bottomNavItems = listOf(
			Screen.Launches,
			Screen.Rockets
		)

		setContent {
			val navController = rememberNavController()
			val scaffoldState = rememberScaffoldState()
			MdcTheme {
				Scaffold(
					scaffoldState = scaffoldState,
					bottomBar = {
						BottomNavigation {
							val navBackStackEntry by navController.currentBackStackEntryAsState()
							val currentRoute = navBackStackEntry?.destination?.route
							bottomNavItems.forEach { screen ->
								BottomNavigationItem(
									icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
									label = { Text(stringResource(screen.label)) },
									selected = currentRoute == screen.route,
									onClick = {
										navController.navigate(screen.route) {
											// Pop up to the start destination of the graph to
											// avoid building up a large stack of destinations
											// on the back stack as users select items
											popUpTo(navController.graph.startDestinationRoute!!) {
												saveState = true
											}
											// Avoid multiple copies of the same destination when
											// reselecting the same item
											launchSingleTop = true
											// Restore state when reselecting a previously selected item
											restoreState = true
										}
									}
								)
							}

						}
					}
				) {
					NavHost(navController, startDestination = Screen.Launches.route) {
						composable(Screen.Launches.route) { backStackEntry ->
							Launches(
								navController,
								scaffoldState,
								hiltViewModel(backStackEntry)
							)
						}
						composable(Screen.Rockets.route) { Rockets() }
					}
				}
			}
		}
	}

}

sealed class Screen(val route: String, @StringRes val label: Int) {
	object Launches : Screen("launches", R.string.launches)
	object Rockets : Screen("rockets", R.string.rockets)
}