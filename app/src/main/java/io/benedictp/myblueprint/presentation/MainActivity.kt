package io.benedictp.myblueprint.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.benedictp.myblueprint.R
import io.benedictp.myblueprint.presentation.util.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

	private var currentNavController: LiveData<NavController>? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			setupBottomNavigationBar()
		} // Else, need to wait for onRestoreInstanceState
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		// Now that BottomNavigationBar has restored its instance state
		// and its selectedItemId, we can proceed with setting up the
		// BottomNavigationBar with Navigation
		setupBottomNavigationBar()
	}

	private fun setupBottomNavigationBar() {
		val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

		val navGraphIds = listOf(R.navigation.launches, R.navigation.rockets)

		// Setup the bottom navigation view with a list of navigation graphs
		val controller = bottomNavigationView.setupWithNavController(
			navGraphIds = navGraphIds,
			fragmentManager = supportFragmentManager,
			containerId = R.id.nav_host_container,
			intent = intent
		)

		currentNavController = controller
	}

	override fun onSupportNavigateUp(): Boolean {
		return currentNavController?.value?.navigateUp() ?: false
	}

}