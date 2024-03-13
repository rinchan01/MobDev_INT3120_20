package com.example.planegame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        findViewById<Button>(R.id.skin_shop).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

    }
}