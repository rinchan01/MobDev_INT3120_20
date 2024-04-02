package com.example.planegame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.PlayerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameResultActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val score = intent.getIntExtra("score", 0)
        preferenceHelper = PreferenceHelper(this)

        findViewById<TextView>(R.id.textView2).text = "Your score: " + score

        lifecycleScope.launch(Dispatchers.IO) {
            val player = playerDao.getPlayer(preferenceHelper.getUsername())
            if (player != null) {
                val newScore = if(player.highScore < score) score else player.highScore
                val newPlayer = player.copy(highScore = newScore, coins = player.coins + score / 10)
                playerDao.upsertPlayer(newPlayer)
            }
        }

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}