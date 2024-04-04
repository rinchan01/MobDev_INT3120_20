package com.example.planegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.Player
import com.example.planegame.database.PlayerDatabase
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper

    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    @SuppressLint("MissingPermission")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                0
            )
        }

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
                var locationString: String
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                    override fun isCancellationRequested() = false
                    })
                    .addOnSuccessListener { location: Location? ->
                        if (location == null)
                            Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                        else {
                            val lat = location.latitude
                            val lon = location.longitude
                            locationString = "$lat, $lon"

                            val newPlayer = Player(username, 0, 0, "6", "defaultAvatar.png", locationString)
                            lifecycleScope.launch(Dispatchers.IO) {
                                playerDao.getPlayer(username) ?: playerDao.insertPlayer(newPlayer)
                            }
                            val toMain = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(toMain)
                        }
                    }

            }
        }

        val sharedPrefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE)
        val isSwitchChecked = sharedPrefs.getBoolean("switchState", false)
        if (isSwitchChecked) {
            startService(Intent(applicationContext, MusicService::class.java))
        }
    }
}