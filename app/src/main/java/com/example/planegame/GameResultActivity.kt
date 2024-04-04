package com.example.planegame

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.PlayerDatabase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameResultActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    @SuppressLint("Range", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val score = intent.getIntExtra("score", 0)
        preferenceHelper = PreferenceHelper(this)

        findViewById<TextView>(R.id.textView2).text = "Your score: " + score

        lifecycleScope.launch(Dispatchers.IO) {
            val player = playerDao.getPlayer(preferenceHelper.getUsername())
            if (player != null) {
                if(player.highScore < score) {
                    var locationString: String
                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@GameResultActivity)
                    fusedLocationProviderClient
                        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                            override fun isCancellationRequested() = false
                        })
                        .addOnSuccessListener { location: Location? ->
                            if (location == null)
                                Toast.makeText(this@GameResultActivity, "Cannot get location.", Toast.LENGTH_SHORT).show()
                            else {
                                val lat = location.latitude
                                val lon = location.longitude
                                locationString = "$lat, $lon"
                                val newPlayer = player.copy(highScore = score, coins = player.coins + score / 10, location = locationString)
                                lifecycleScope.launch(Dispatchers.IO) {
                                    playerDao.upsertPlayer(newPlayer)
                                }
                            }
                        }
                } else {
                    val newPlayer = player.copy(coins = player.coins + score / 10)
                    playerDao.upsertPlayer(newPlayer)
                }

            }
        }

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}