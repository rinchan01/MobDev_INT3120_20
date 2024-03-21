package com.example.planegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switch1 = findViewById<SwitchMaterial>(R.id.switch1)
        val switch2 = findViewById<SwitchMaterial>(R.id.switch2)
        switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(applicationContext, MusicService::class.java))
            } else {
                stopService(Intent(applicationContext, MusicService::class.java))
            }
        }
    }
}