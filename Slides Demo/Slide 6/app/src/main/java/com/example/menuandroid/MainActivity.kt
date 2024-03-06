package com.example.menuandroid

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val button = findViewById<Button>(R.id.button)
        registerForContextMenu(button)

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val popupMenu = PopupMenu(this@MainActivity, button2)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                Toast.makeText(this@MainActivity, "You Clicked " + menuItem.title, Toast.LENGTH_SHORT).show()
                true
            }
            popupMenu.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val menuItem = menu.findItem(R.id.item1)
        val searchView = menuItem.actionView as SearchView
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.action_delete -> Toast.makeText(baseContext, "delete", Toast.LENGTH_LONG).show()
            R.id.action_edit -> Toast.makeText(baseContext, "edit", Toast.LENGTH_LONG).show()
            R.id.share -> Toast.makeText(baseContext, "share", Toast.LENGTH_LONG).show()
            R.id.face -> Toast.makeText(baseContext, "facebook", Toast.LENGTH_LONG).show()
            R.id.ins -> Toast.makeText(baseContext, "instagram", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.item2 -> Toast.makeText(baseContext, "item2", Toast.LENGTH_LONG).show()
            R.id.item3 -> Toast.makeText(baseContext, "item3", Toast.LENGTH_LONG).show()
            R.id.item4 -> Toast.makeText(baseContext, "item4", Toast.LENGTH_LONG).show()
            R.id.item5 -> Toast.makeText(baseContext, "item5", Toast.LENGTH_LONG).show()
            R.id.item6 -> Toast.makeText(baseContext, "item6", Toast.LENGTH_LONG).show()
            R.id.item6_1 -> Toast.makeText(baseContext, "item6.1", Toast.LENGTH_LONG).show()

            R.id.item6_2 -> Toast.makeText(baseContext, "item6.2", Toast.LENGTH_LONG).show()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}