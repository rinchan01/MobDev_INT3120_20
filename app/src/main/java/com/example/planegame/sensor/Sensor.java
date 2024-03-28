package com.example.planegame.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Sensor extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    private SensorEventListener listener;
    private TextView textView;

    // check if the device has an accelerometer
    public boolean hasAccelerometer() {
        return manager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER) != null;
    }
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_accelerometer);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // create an instance of the SensorManager
        if (hasAccelerometer()) {
            android.hardware.Sensor accelerometer = manager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); // get the accelerometer sensor
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL); // register the listener
        }
//        textView = findViewById(R.id.textView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
