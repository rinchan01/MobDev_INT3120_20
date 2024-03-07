package com.example.app1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity

class TestActivity : ComponentActivity() {
    private var firstSpinner: Boolean = true
    private val fruits: Array<String> = arrayOf(" ", "Apple", "Banana", "Orange", "Mango", "Pineapple", "Grapes", "Strawberry")
    private val foods: Array<String> = arrayOf("Pizza", "Burger", "Pasta", "Sandwich", "Hot Dog", "Taco", "Burrito", "Salad",
                                                "Sushi", "Nuggets", "Fries", "Ice Cream", "Cake", "Donut", "Cupcake", "Muffin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val exitButton = findViewById<Button>(R.id.button)
        exitButton.setOnClickListener { finish() }

        setUpSpinner()
        setUpList()
        displayTimePicker()
    }

    private fun setUpSpinner() {
        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fruits)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(firstSpinner) {
                    firstSpinner = false
                    return
                }
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setUpList() {
        val list: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, R.layout.my_red_list, foods)
        list.adapter = adapter
        list.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(applicationContext, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayTimePicker() {
        val timePicker: TimePicker = findViewById(R.id.timePicker)
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            Toast.makeText(applicationContext, "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
        }
    }

}