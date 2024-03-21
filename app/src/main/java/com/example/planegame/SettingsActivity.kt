package com.example.planegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val PREF_NAME = "SettingsPrefs"
    private val SWITCH_STATE_KEY = "switchState"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val isSwitchChecked = sharedPrefs.getBoolean(SWITCH_STATE_KEY, false)

        val switch2 = findViewById<SwitchMaterial>(R.id.switch2)
        switch2.isChecked = isSwitchChecked

        switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(applicationContext, MusicService::class.java))
            } else {
                stopService(Intent(applicationContext, MusicService::class.java))
            }
            saveswitchState(isChecked)
        }
    }

    private fun saveswitchState(isChecked: Boolean) {
        val sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(SWITCH_STATE_KEY, isChecked)
        editor.apply()
    }
}