package com.teamwork.javasresto.activity

import Config
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.teamwork.javasresto.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //hide keyboard

        //hide keyboard
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            private fun doNothing() {}
            override fun run() {
                val sp = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), MODE_PRIVATE)
                val loginToken = sp.getString(Config::LOGIN_TOKEN_SHARED_PREF.toString(), "")
                Log.d("RBA", "respon : $loginToken")
                if (loginToken.equals("", ignoreCase = true)) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                        val intent1 = Intent(
                            applicationContext,
                            DashboardActivity::class.java
                        )
                        startActivity(intent1)
                        finish()
                }
            }
        }, 2000)
    }
}