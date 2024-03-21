package com.example.planegame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planegame.provider.UserProvider

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val leaderboardList: LinearLayout = findViewById(R.id.leaderboardList)
        val players: List<Player> = getPlayerDataFromDatabase()

        players.forEach { player ->
            val playerView = LayoutInflater.from(this).inflate(R.layout.leaderboard_item, leaderboardList, false)

            val ivAvatar: ImageView = playerView.findViewById(R.id.ivAvatar)
            val tvPlayerName: TextView = playerView.findViewById(R.id.tvPlayerName)
            val tvCoins: TextView = playerView.findViewById(R.id.tvCoins)

            ivAvatar.setImageResource(getResources().getIdentifier("avt5", "drawable", packageName))
            tvPlayerName.text = player.name
            tvCoins.text = player.score.toString()

            leaderboardList.addView(playerView)
        }

        val ivBack: ImageView = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }


    data class Player(val name: String, val score: Int)

    @SuppressLint("Range")
    private fun getPlayerDataFromDatabase(): List<Player> {
        val projection = arrayOf("name", "score")
        val cursor = contentResolver.query(UserProvider.URI, projection, null, null, "score DESC")
        val players = mutableListOf<Player>()

        if(cursor != null) {
            while(cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(UserProvider.name))
                val score = cursor.getInt(cursor.getColumnIndex(UserProvider.score))
                players.add(Player(name, score))
            }
            cursor.close()
        }

        return players
    }
}
