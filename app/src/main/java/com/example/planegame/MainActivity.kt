package com.example.planegame

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.planegame.gameplay.AndroidLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceHelper = PreferenceHelper(this)
        findViewById<Button>(R.id.startGameButton).setOnClickListener {
            startActivity(Intent(this, AndroidLauncher::class.java))
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

//        findViewById<Button>(R.id.shopButton).setOnClickListener {
//            startActivity(Intent(this, ShopActivity::class.java))
//        }
//
//        findViewById<Button>(R.id.settingButton).setOnClickListener {
//            startActivity(Intent(this, SettingsActivity::class.java))
//        }
//
//        findViewById<Button>(R.id.logoutButton).setOnClickListener {
//            preferenceHelper.clear()
//            getSharedPreferences("SettingsPrefs", MODE_PRIVATE).edit().clear().apply()
//            startActivity(Intent(this, LoginActivity::class.java))
//        }

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
//            R.id.shop -> startActivity(Intent(this, ShopActivity::class.java))
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