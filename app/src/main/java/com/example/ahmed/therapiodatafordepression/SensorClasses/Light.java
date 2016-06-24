package com.example.ahmed.therapiodatafordepression.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.ahmed.therapiodatafordepression.SensorsDataWriters.DataWriterLight;

import java.io.IOException;

/**
 * Created by ahmed on 16/06/16.
 */
public class Light implements SensorEventListener
{

    private static final String LOG_TAG = "Light";
    private static Light instance;

    public static Light getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = new Light(context);
        }
        return instance;
    }

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private DataWriterLight fileWriter;
    private boolean isSensing;

    protected Light(final Context context)
    {
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.isSensing = false;
    }

    public boolean isSensing()
    {
        return isSensing;
    }

    public void start() throws IOException
    {
        fileWriter = new DataWriterLight();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        isSensing = true;
    }

    public void stop() throws IOException
    {
        fileWriter.finish();
        mSensorManager.unregisterListener(this);
        isSensing = false;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        try {
            fileWriter.append(event);
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG, e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
