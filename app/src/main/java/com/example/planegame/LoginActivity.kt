package com.example.planegame

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.planegame.provider.UserProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var userProvider : UserProvider

    private lateinit var preferenceHelper: PreferenceHelper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        preferenceHelper = PreferenceHelper(this)
//        if (preferenceHelper.isLoggedIn()) {
//            val toMain = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(toMain)
//        }


        val button = findViewById<Button>(R.id.btnLogin)
        button.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            if (username.isNotEmpty()) {
                val values = ContentValues()
                values.put(UserProvider.name, username)
                if (contentResolver.insert(UserProvider.URI, values) != null) {
                    val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("is_logged_in", true)
                        putString("username", username)
                        apply()
                    }
                    println("New Player Added!")
                } else {
                    println("Failed to add new player")
                }
                val toMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(toMain)
            }
        }

    }
}