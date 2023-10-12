package com.example.ortseguros.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val screenSplash = installSplashScreen()

        screenSplash.setKeepOnScreenCondition{false}



        val sharedPrefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isLoggedIn =  sharedPrefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


}