package com.example.planegame.gameplay;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements SensorEventListener {
    private Main main;
    public static float xchange, ychange;
    private SensorManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        if (manager.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE) == null) {
            Log.d("Sensor", "No Gyroscrope found");
        }
        else {
            android.hardware.Sensor gyro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            manager.registerListener(this, gyro, 1000000);
        }
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;
        if (main != null) {
            main.dispose();
        }
        main = new Main(this.getContext());
        initialize(main, configuration);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if (y > 0) {
            xchange = 0.4f;
        } else if (y < 0) {
            xchange = -0.4f;
        }
        if (x > 0) {
            ychange = -0.4f;
        } else if (x < 0) {
            ychange = 0.4f;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
