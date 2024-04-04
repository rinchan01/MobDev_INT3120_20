package com.example.planegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.planegame.database.PlayerDatabase


class LoginActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var workManager: WorkManager

    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    @SuppressLint("MissingPermission")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                0
            )
        }

        workManager = WorkManager.getInstance(applicationContext)

        preferenceHelper = PreferenceHelper(this)
        if (preferenceHelper.isLoggedIn()) {
            val toMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(toMain)
        }

        val button = findViewById<Button>(R.id.btnLogin)
        button.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            if (username.isNotEmpty()) {
                val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("username", username)
                    apply()
                }

                val request = OneTimeWorkRequestBuilder<RetrieveLocationWorker>()
                    .build()

                workManager.enqueue(request)

                val toMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(toMain)
            }
        }

        val sharedPrefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE)
        val isSwitchChecked = sharedPrefs.getBoolean("switchState", false)
        if (isSwitchChecked) {
            startService(Intent(applicationContext, MusicService::class.java))
        }
    }
}