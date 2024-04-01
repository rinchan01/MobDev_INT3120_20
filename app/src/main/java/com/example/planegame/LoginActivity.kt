package com.example.planegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.Player
import com.example.planegame.database.PlayerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper

    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

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

                val newPlayer = Player(username, 0, 0, "6", "defaultAvatar.png", "none")
                lifecycleScope.launch(Dispatchers.IO) {
                    playerDao.getPlayer(username)?: playerDao.insertPlayer(newPlayer)
                }
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