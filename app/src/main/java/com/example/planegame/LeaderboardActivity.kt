package com.example.planegame

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.PlayerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var leaderboardAdapter: LeaderBoardAdapter
    private lateinit var listView: ListView
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        listView = findViewById(R.id.lvLeaderboard)

        lifecycleScope.launch(Dispatchers.IO) {
            playerDao.getLeaderboard().collectLatest { players ->
                val leaderboardPlayers: List<LeaderBoardPlayer> = players.map {
                    val file = File(filesDir, it.avatarPath)
                    val bytes = file.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    LeaderBoardPlayer(it.name, it.highScore, bmp)
                }
                withContext(Dispatchers.Main) {
                    leaderboardAdapter = LeaderBoardAdapter(this@LeaderboardActivity, leaderboardPlayers)
                    listView.adapter = leaderboardAdapter
                }
            }
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private class LeaderBoardAdapter(context: Context, private val players: List<LeaderBoardPlayer>): ArrayAdapter<LeaderBoardPlayer>(context, 0, players) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            val holder: ViewHolder

            if(convertView == null) {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.leaderboard_item, parent, false)

                holder = ViewHolder()
                holder.ivAvatar = itemView.findViewById(R.id.ivAvatar)
                holder.tvPlayerName = itemView.findViewById(R.id.tvPlayerName)
                holder.tvCoins = itemView.findViewById(R.id.tvCoins)

                itemView.tag = holder
            } else {
                holder = itemView?.tag as ViewHolder
            }

            val currentItem = players[position]
            holder.tvPlayerName.text = currentItem.name
            holder.tvCoins.text = currentItem.coins.toString()
            holder.ivAvatar.setImageBitmap(currentItem.avatar)
            return itemView!!
        }

        class ViewHolder {
            lateinit var ivAvatar: ImageView
            lateinit var tvPlayerName: TextView
            lateinit var tvCoins: TextView
        }
    }

    private class LeaderBoardPlayer(val name: String, val coins: Int, val avatar: Bitmap)
}
