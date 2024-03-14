package com.example.planegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText username = findViewById(R.id.username);
        final Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toMain);
            }
        });
    }


}
