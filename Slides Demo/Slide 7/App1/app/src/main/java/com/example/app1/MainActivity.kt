package com.example.app1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> startActivity(Intent(this, OrderActivity::class.java))
            R.id.item2 -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("trinhlk@vnu.edu.vn"))
                    putExtra(Intent.EXTRA_SUBJECT, "Test Email")
                    putExtra(Intent.EXTRA_TEXT, "Hello World!")
                }
                startActivity(Intent.createChooser(intent, "Send Email"))
            }
            R.id.item3 -> {
                val url = "https://vnexpress.net/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if(intent.resolveActivity(packageManager) != null) startActivity(intent)

            }
            R.id.item4 -> {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.`package` = "com.google.android.youtube"
                startActivity(intent)
            }
            else -> {
                val myPerson = Person("Minh", 90)
                val intent = Intent(this, SubActivity::class.java).apply {
                    putExtra("person", myPerson)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

