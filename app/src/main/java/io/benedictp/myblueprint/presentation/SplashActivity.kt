package io.benedictp.myblueprint.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.benedictp.myblueprint.R

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(R.style.Theme_MyBlueprint)
		super.onCreate(savedInstanceState)
		window.setBackgroundDrawableResource(R.drawable.splash_screen)

		//here we could add logic if the user is logged in or not or we should show an onboarding

		val intent = Intent(this, MainActivity::class.java)
		//when the user hits the back button he does not see the splash screen
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		startActivity(intent)
		finish()
	}

}