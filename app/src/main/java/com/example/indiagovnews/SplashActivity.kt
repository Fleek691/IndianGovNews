package com.example.indiagovnews

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.indiagovnews.utils.PreferencesManager

class SplashActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferencesManager = PreferencesManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (preferencesManager.isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000) // 2 seconds delay
    }
}
