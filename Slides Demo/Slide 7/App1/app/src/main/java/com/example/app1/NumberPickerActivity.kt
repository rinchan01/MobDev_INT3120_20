package com.example.app1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.ComponentActivity

class NumberPickerActivity : ComponentActivity() {

    private var totalAmount: Int = 0
    private lateinit var numberPicker: NumberPicker
    private lateinit var buttonDonate: Button
    private lateinit var amountText: EditText
    private lateinit var textTotal: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_picker)

        buttonDonate = findViewById(R.id.button3)
        amountText = findViewById(R.id.editTextNumber)
        numberPicker = findViewById(R.id.numberPicker)
        textTotal = findViewById(R.id.textView)
        numberPicker.minValue = 990
        numberPicker.maxValue = 1010

        buttonDonate.setOnClickListener {
            totalAmount += numberPicker.value
            totalAmount += amountText.text.toString().toInt()
            val totalString =  "Total amount: $totalAmount"
            textTotal.text = totalString
        }


    }
}