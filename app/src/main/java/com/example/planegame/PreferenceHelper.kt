package com.example.planegame


import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedIn: Boolean, username: String) {
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", isLoggedIn)
            putString("username", username)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun getUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    fun clear() {
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", false)
//            putString("username", "")
            apply()
        }
    }
}