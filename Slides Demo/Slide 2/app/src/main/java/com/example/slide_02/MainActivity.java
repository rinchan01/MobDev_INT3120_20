package com.example.slide_02;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;

import android.os.Bundle;
import android.widget.NumberPicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private int donate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Custom tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Donation.1.5");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        // Custom number picker
        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(1000);
        numberPicker.setValue(999);

        // Custom floating button
        FloatingActionButton btn = findViewById(R.id.floatingActionButton);
        btn.setBackgroundColor(Color.parseColor("#6750A4"));

    }
}