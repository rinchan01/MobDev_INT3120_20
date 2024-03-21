package com.example.planegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GameResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}