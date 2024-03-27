package com.example.planegame

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planegame.provider.UserProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var userProvider : UserProvider

    private lateinit var preferenceHelper: PreferenceHelper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

//        contentResolver.delete(UserProvider.URI, null, null)

        preferenceHelper = PreferenceHelper(this)
        if (preferenceHelper.isLoggedIn()) {
            val toMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(toMain)
        }


        val button = findViewById<Button>(R.id.btnLogin)
        button.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            println(username)
            if (username.isNotEmpty()) {

                val selection = "name = ?"
                val selectionArgs = arrayOf(username)
                val projection = arrayOf(UserProvider.id, UserProvider.name, UserProvider.score)
                val cursor = contentResolver.query(UserProvider.URI, projection, selection, selectionArgs, null)

                if(cursor != null && cursor.moveToNext()) {
                    println("Player already exists!")
                }
                else {
                    val values = ContentValues()
//                    values.put(UserProvider.id, getNextId())
                    values.put(UserProvider.name, username)
                    values.put(UserProvider.score, "0")
                    if (contentResolver.insert(UserProvider.URI, values) != null) {
                        println("New Player Added!")
                    } else {
                        println("Failed to add new player")
                    }
                }
                cursor?.close()
                val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("username", username)
                    apply()
                }
                val toMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(toMain)
            }
        }

        val sharedPrefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE)
        val isSwitchChecked = sharedPrefs.getBoolean("switchState", false)
        if (isSwitchChecked) {
            startService(Intent(applicationContext, MusicService::class.java))
        }
    }

    @SuppressLint("Range")
    private fun getNextId(): Int {
        val projection = arrayOf(UserProvider.id)
        val cursor = contentResolver.query(UserProvider.URI, projection, null, null, null)

        val nextId = cursor?.count ?: 0

        return nextId + 1
    }
}