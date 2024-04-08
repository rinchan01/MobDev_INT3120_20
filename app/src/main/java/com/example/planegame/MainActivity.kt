package com.example.planegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.planegame.gameplay.AndroidLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var rootView: ConstraintLayout
    private lateinit var backgroundLiveData: LiveData<WorkInfo>
    private lateinit var workManager: WorkManager
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceHelper = PreferenceHelper(this)

        rootView = findViewById(R.id.root_view)
        if(preferenceHelper.getWeatherBackground() == "bg3") rootView.setBackgroundResource(R.drawable.bg3)

        workManager = WorkManager.getInstance(applicationContext)

        findViewById<Button>(R.id.startGameButton).setOnClickListener {
            startActivity(Intent(this, AndroidLauncher::class.java))
        }

        if(intent.getStringExtra("BackgroundWorkerID") != null) {
            val backgroundWorkerID = UUID.fromString(intent.getStringExtra("BackgroundWorkerID"))
            backgroundLiveData = workManager.getWorkInfoByIdLiveData(backgroundWorkerID)
            backgroundLiveData.observe(this) {
                if (it.state.isFinished) {
                    val weatherText = it.outputData.getString("WEATHER_STRING")
                    val weatherCode = it.outputData.getInt("WEATHER_CODE", 0)
                    Log.d("Weather", weatherText ?: "Couldn't get weather")
                    if (weatherCode <= 1030) {
                        getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit().putString("background", "bg3").apply()
                        rootView.setBackgroundResource(R.drawable.bg3)
                    }
                }
            }
        }

        val leaderboard = findViewById<Button>(R.id.leaderboardButton)
        leaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        val help = findViewById<Button>(R.id.helpButton)
        help.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.shopButton).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        findViewById<TextView>(R.id.welcome).text = "Hello ${preferenceHelper.getUsername()}"

        lifecycleScope.launch(Dispatchers.IO) {
            val defaultAvatarFileName = "defaultAvatar.png"
            val file = File(filesDir, defaultAvatarFileName)
            if(!file.exists()) {
                val drawable = ContextCompat.getDrawable(this@MainActivity, R.drawable.avt1)
                val bmp = (drawable as BitmapDrawable).bitmap
                openFileOutput(defaultAvatarFileName, MODE_PRIVATE).use {
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.logout -> {
                preferenceHelper.clear()
                getSharedPreferences("SettingsPrefs", MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}