package com.example.planegame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val leaderboardList: LinearLayout = findViewById(R.id.leaderboardList)
        val players: List<Player> = getPlayerData()

        players.forEach { player ->
            val playerView = LayoutInflater.from(this).inflate(R.layout.leaderboard_item, leaderboardList, false)

            val ivAvatar: ImageView = playerView.findViewById(R.id.ivAvatar)
            val tvPlayerName: TextView = playerView.findViewById(R.id.tvPlayerName)
            val tvCoins: TextView = playerView.findViewById(R.id.tvCoins)

            ivAvatar.setImageResource(getResources().getIdentifier(player.avatar, "drawable", packageName))
            tvPlayerName.text = player.name
            tvCoins.text = player.coins.toString()

            leaderboardList.addView(playerView)
        }

        val ivBack: ImageView = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }


    data class Player(val name: String, val coins: Int, val avatar: String)


    fun getPlayerData(): List<Player> {
        // Cần sửa là lấy data từ database TvT
        val playerData = arrayOf(
            arrayOf("Player 1", 5568, "avt1"),
            arrayOf("Player 2", 4968, "avt2"),
            arrayOf("Player 3", 5578, "avt3"),
            arrayOf("Player 4", 2457, "avt4"),
            arrayOf("Player 5", 8907, "avt5"),
            arrayOf("Player 6", 1255, "avt1"),
            arrayOf("Player 7", 8900, "avt2"),
            arrayOf("Player 8", 4467, "avt3"),
            arrayOf("Player 9", 3468, "avt4"),
            arrayOf("Player 10", 6890, "avt5"),
            arrayOf("Player 11", 5678, "avt1"),
            arrayOf("Player 12", 4968, "avt2"),
            arrayOf("Player 13", 5689, "avt3"),
            arrayOf("Player 14", 9097, "avt4"),
            arrayOf("Player 15", 3456, "avt5")
        )

        val sortedPlayerData = playerData.sortedByDescending { it[1] as Int }

        val players = mutableListOf<Player>()
        for (data in sortedPlayerData) {
            val name = data[0] as String
            val coins = data[1] as Int
            val avatar = data[2] as String
            players.add(Player(name, coins, avatar))
        }

        return players
    }
}
