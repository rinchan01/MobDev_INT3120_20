package com.example.app1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class SubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
        val myPerson: Person = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra("person", Person::class.java)!!
        else
            intent.getParcelableExtra<Person>("person")!!
        val textView = findViewById<TextView>(R.id.textView4)
        textView.text = getString(R.string.person_info, myPerson.name, myPerson.age)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}