package com.example.planegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.planegame.R.*

class GameResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_game_result)

        val score = intent.getIntExtra("score", 0)
        findViewById<TextView>(R.id.score).text = "Your score: $score"
        findViewById<Button>(id.buttonBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}