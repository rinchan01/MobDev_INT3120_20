package com.example.planegame

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.PlayerDatabase
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class SettingsActivity : AppCompatActivity() {
    private lateinit var hardModePicker: NumberPicker
    private lateinit var soundEffectSwitch: SwitchMaterial
    private lateinit var musicSwitch: SwitchMaterial
    private lateinit var avatarImage: ImageView
    private lateinit var changeAvatarButton: Button
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            avatarImage.setImageBitmap(selectedImage)
            try {
                val preferenceHelper = PreferenceHelper(this)
                val playerName = preferenceHelper.getUsername()
                openFileOutput("avatar${playerName}.png", MODE_PRIVATE).use {
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 90, it)
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    val player = playerDao.getPlayer(playerName)
                    val newPlayer = player!!.copy(avatarPath = "avatar${playerName}.png")
                    playerDao.upsertPlayer(newPlayer)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val PREF_NAME = "SettingsPrefs"

    val SWITCH_STATE_KEY = "switchState"
    val HARD_MODE_KEY = "hardMode"
    val SOUND_EFFECT_KEY = "soundEffect"

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                0
            )
        }

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


        avatarImage = findViewById(R.id.avatarImage)
        changeAvatarButton = findViewById(R.id.button)
        changeAvatarButton.setOnClickListener {
            setAvatarImage()
        }

        val preferenceHelper = PreferenceHelper(this)
        val playerName = preferenceHelper.getUsername()
        try {
            val file = File(filesDir, "avatar${playerName}.png")
            val bytes = file.readBytes()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            avatarImage.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
            avatarImage.setImageResource(R.drawable.avt1)
        }
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

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun setAvatarImage() {
        val photoPickerIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
        photoPickerIntent.type = "image/*"
        activityResultLauncher.launch(photoPickerIntent)
    }
}