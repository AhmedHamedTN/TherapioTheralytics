package com.example.ahmed.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.ahmed.io.DataWriter;
import com.example.ahmed.utils.Constants;

import java.io.IOException;

/**
 * Created by Khalil on 21-Jun-16.
 */
public class SensorEvent implements SensorEventListener {

    public static boolean isSensing;
    SensorManager mSensorManager;
    Sensor mSensor;
    DataWriter fileWriter;
    Constants.SensorType sensorType;
    Context context;


    private SensorEvent()
    {}

    private static SensorEvent INSTANCE = null;

    public static SensorEvent getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new SensorEvent();
        }
        return INSTANCE;
    }

    public static SensorEvent getInstance(Context context, Constants.SensorType sensorType)
    {
        if (INSTANCE == null)
        { 	INSTANCE = new SensorEvent(context, sensorType);
        }
        return INSTANCE;
    }


    public SensorEvent(Context context, Constants.SensorType sensorType) {
        this.sensorType = sensorType;
        this.context = context;
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = mSensorManager.getDefaultSensor(sensorType.getType());
      //  this.isSensing = true;
    }


    public void start() {
        isSensing = true;
        fileWriter = new DataWriter(context,sensorType);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        try {
            fileWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isSensing = false;
    }


    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {

        try {
            if (isSensing) {
                fileWriter.append(event);

            } else {
                fileWriter.finish();
                mSensorManager.unregisterListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
