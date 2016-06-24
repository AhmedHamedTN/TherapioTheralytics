package com.example.ahmed.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.listener.SensorEvent;
import com.example.ahmed.utils.Constants;


/**
 * Created by ahmed on 20/06/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static SensorEvent accelerometer;
    public static SensorEvent gyroscope;
    public static SensorEvent light;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        //just displaying a message
        //instead
        //i want to read sensors data and write em to CSV

Log.d("accelerometer.isSensing",accelerometer.isSensing+"");
        if (accelerometer.isSensing) {
            accelerometer = new SensorEvent(context, Constants.SensorType.Accelerometer);
            gyroscope = new SensorEvent(context, Constants.SensorType.Gyroscope);
            light = new SensorEvent(context, Constants.SensorType.Light);
            Toast.makeText(context, "I'm running instead of some sensors being captured", Toast.LENGTH_SHORT).show();
            Log.d("toast", "I'm running instead of some sensors being captured");
            startSensing();
        }
    }

    public void startSensing() {


        accelerometer.start();

        gyroscope.start();
        light.start();


    }

    public void stopSensing() {


        accelerometer.stop();

        gyroscope.stop();
        light.stop();


    }
}
