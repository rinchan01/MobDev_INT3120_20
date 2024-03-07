package com.example.app1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity

class OrderActivity : ComponentActivity() {
    private var cheese: String = ""
    private var shape: String = ""
    private var addOns: String = ""
    private var finalOrder: String = "Pizza"
    private var checkBoxes: Array<Boolean> = arrayOf(false, false, false)
    private lateinit var radioGroup1: RadioGroup
    private lateinit var radioGroup2: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val smsButton = findViewById<Button>(R.id.button2)
        smsButton.setOnClickListener { sendSMSOrder() }
        val exitButton = findViewById<Button>(R.id.button1)
        exitButton.setOnClickListener { finish() }
        updateCheese()
        updateShape()
        updateAddOns()
    }

    private fun updateFinalOrder() {
        finalOrder = "$cheese $shape Pizza$addOns"
        val finalOrderText: TextView = findViewById(R.id.textView3)
        finalOrderText.text = finalOrder
    }

    private fun updateCheese() {
        radioGroup1 = findViewById(R.id.radioGroup)
        radioGroup1.setOnCheckedChangeListener { group, i ->
            val checkedRadioButton = findViewById<RadioButton>(i)
            cheese = if(R.id.radioButton3 != i) checkedRadioButton.text.toString() else ""
            updateFinalOrder()
        }
    }

    private fun updateShape() {
        radioGroup2 = findViewById(R.id.radioGroup2)
        radioGroup2.setOnCheckedChangeListener { group, i ->
            val checkedRadioButton = findViewById<RadioButton>(i)
            shape = checkedRadioButton.text.toString()
            updateFinalOrder()
        }
    }

    private fun updateAddOns() {
        val checkBoxIds = arrayOf(R.id.checkBox1, R.id.checkBox2, R.id.checkBox3)
        for(i in 0..2) {
            val checkBox = findViewById<CheckBox>(checkBoxIds[i])
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                addOns = ""
                checkBoxes[i] = isChecked
                if(checkBoxes.contains(true)) {
                    addOns = " with:"
                    for(j in 0..2) {
                        if(checkBoxes[j]) addOns += "\n+ " + findViewById<CheckBox>(checkBoxIds[j]).text.toString() + " "
                    }
                }
                updateFinalOrder()
            }
        }
    }

    private fun sendSMSOrder() {
        val name: String = findViewById<TextView>(R.id.editText1).text.toString()
        val phone: String = findViewById<TextView>(R.id.editTextPhone).text.toString()
        val smsOrder = "Customer: $name\nPhone Number: $phone\nOrder: $finalOrder"
        val uri: Uri = Uri.parse("smsto:$phone");
        val intent = Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smsOrder);
        startActivity(intent)
    }

}