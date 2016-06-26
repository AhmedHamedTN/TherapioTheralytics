package com.example.ahmed.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.listener.SensorEvent;
import com.example.ahmed.sensor.Accelerometer;
import com.example.ahmed.sensor.Gyroscope;
import com.example.ahmed.sensor.Light;
import com.example.ahmed.sensor.Magnetometer;


/**
 * Created by ahmed on 20/06/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static Accelerometer accelerometer;
    public static Gyroscope gyroscope;
    public static Light light;
    public static Magnetometer magnetometer;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        //just displaying a message
        //instead
        //i want to read sensors data and write em to CSV

        Log.d("accelerometer.isSensing", SensorEvent.isSensing + "");
        if (accelerometer.isSensing) {
            accelerometer = new Accelerometer(context);
            gyroscope = new Gyroscope(context);
            light = new Light(context);
            magnetometer = new Magnetometer(context);
            Toast.makeText(context, "I'm running instead of some sensors being captured", Toast.LENGTH_SHORT).show();
            Log.d("toast", "I'm running instead of some sensors being captured");
            startSensing();
        }
    }

    public void startSensing() {


        accelerometer.start();
        gyroscope.start();
        light.start();
        magnetometer.start();

    }

    public void stopSensing() {


        accelerometer.stop();
        gyroscope.stop();
        light.stop();
        magnetometer.stop();


    }
}
