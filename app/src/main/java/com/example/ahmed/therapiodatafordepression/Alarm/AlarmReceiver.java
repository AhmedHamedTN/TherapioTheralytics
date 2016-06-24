package com.example.ahmed.therapiodatafordepression.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.SensorClasses.Accelerometer;
import com.example.ahmed.therapiodatafordepression.SensorClasses.Gyroscope;
import com.example.ahmed.therapiodatafordepression.SensorClasses.Light;
import com.example.ahmed.therapiodatafordepression.SensorsDataWriters.DataWriterAcc;

/**
 * Created by ahmed on 23/06/16.
 */
public class AlarmReceiver extends BroadcastReceiver {


    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    private Light light;
    public  static DataWriterAcc DW;
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        //just displaying a message
        //instead
        //i want to read sensors data and write em to CSV
        Toast.makeText(arg0, "I'm running instead of some sensors being captured", Toast.LENGTH_SHORT).show();

        accelerometer = Accelerometer.getInstance(arg0);
        gyroscope = Gyroscope.getInstance(arg0);
        light = Light.getInstance(arg0);
        if (Accelerometer.isSensing) {
            startSensing();
        }

    }
    /*******
     * Used to file upload and show progress
     **********/


    public void startSensing()
    {
                accelerometer.start();
              //  gyroscope.start();
            //    light.start();
    }

    }

