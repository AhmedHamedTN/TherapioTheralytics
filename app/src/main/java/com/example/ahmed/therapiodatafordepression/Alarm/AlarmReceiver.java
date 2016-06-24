package com.example.ahmed.therapiodatafordepression.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.SensorClasses.Accelerometer;
import com.example.ahmed.therapiodatafordepression.SensorClasses.Gyroscope;
import com.example.ahmed.therapiodatafordepression.SensorClasses.Light;
import com.example.ahmed.therapiodatafordepression.SensorsDataWriters.DataWriterAcc;

import java.io.IOException;

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
        if (!arg1.getBooleanExtra("state",true))
        {
            stopSensing();
        }else {
            startSensing();

        }

    }
    /*******
     * Used to file upload and show progress
     **********/


    public void startSensing()
    {
        if ((!accelerometer.isSensing()) && (!light.isSensing()) && (!gyroscope.isSensing()))
        {
            try
            {
                accelerometer.start();
                gyroscope.start();
                light.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stopSensing()
    {
        if ((accelerometer.isSensing()) && (light.isSensing()) && (gyroscope.isSensing()))
        {
            try
            {
                accelerometer.stop();
                gyroscope.stop();
                light.stop();


                Log.d("DW", "SUCCESS");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
