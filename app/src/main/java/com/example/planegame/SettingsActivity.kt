package com.example.planegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var hardModePicker: NumberPicker
    private lateinit var soundEffectSwitch: SwitchMaterial
    private lateinit var musicSwitch: SwitchMaterial

    val PREF_NAME = "SettingsPrefs"

    val SWITCH_STATE_KEY = "switchState"
    val HARD_MODE_KEY = "hardMode"
    val SOUND_EFFECT_KEY = "soundEffect"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val isSwitchChecked = sharedPrefs.getBoolean(SWITCH_STATE_KEY, false)
        val soundEffectChecked = sharedPrefs.getBoolean(SOUND_EFFECT_KEY, false)
        val currentHardMode = sharedPrefs.getInt(HARD_MODE_KEY, 1)

        musicSwitch = findViewById(R.id.switch2)
        musicSwitch.isChecked = isSwitchChecked

        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(applicationContext, MusicService::class.java))
            } else {
                stopService(Intent(applicationContext, MusicService::class.java))
            }
            saveSwitchState(isChecked)
        }

        hardModePicker = findViewById(R.id.numberPicker)
        hardModePicker.minValue = 2
        hardModePicker.maxValue = 4
        hardModePicker.displayedValues = arrayOf("Easy", "Normal", "Hard")
        hardModePicker.value = currentHardMode
        hardModePicker.setOnValueChangedListener { _, _, newVal ->
            hardMode = newVal
            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putInt(HARD_MODE_KEY, newVal).apply()
        }

        soundEffectSwitch = findViewById(R.id.switch1)
        soundEffectSwitch.isChecked = soundEffectChecked
        soundEffectSwitch.setOnCheckedChangeListener { _, isChecked ->
            playSoundEffect = isChecked
            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putBoolean(SOUND_EFFECT_KEY, isChecked).apply()
        }

        hardMode = sharedPrefs.getInt(HARD_MODE_KEY, 2)
        playSoundEffect = sharedPrefs.getBoolean(SOUND_EFFECT_KEY, false)
    }

    private fun saveSwitchState(isChecked: Boolean) {
        val sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(SWITCH_STATE_KEY, isChecked)
        editor.apply()
    }

    companion object {
        var hardMode = 1
        var playSoundEffect = false
    }
}