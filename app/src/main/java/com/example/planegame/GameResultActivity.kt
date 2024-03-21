package com.example.planegame

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.planegame.provider.UserProvider

class GameResultActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_game_result)

        val score = intent.getIntExtra("score", 0)
        preferenceHelper = PreferenceHelper(this)

        findViewById<TextView>(R.id.textView2).text = "Your score: " + score

        val selection = "name = ?"
        val selectionArgs = arrayOf(preferenceHelper.getUsername())
        val projection = arrayOf(UserProvider.id, UserProvider.name, UserProvider.score)
        val cursor = contentResolver.query(UserProvider.URI, projection, selection, selectionArgs, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val highscore = cursor?.getInt(cursor.getColumnIndex("score"))
                if(highscore!! < score) {
                    val values = ContentValues()
                    values.put(UserProvider.score, score)
                    contentResolver.update(UserProvider.URI, values, selection, selectionArgs)
                }
            }
        }
        cursor?.close()

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}