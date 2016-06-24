package com.example.ahmed.therapiodatafordepression.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.ahmed.therapiodatafordepression.SensorsDataWriters.DataWriterGyro;

import java.io.IOException;

/**
 * Created by ahmed on 16/06/16.
 */
public class Gyroscope implements SensorEventListener
{
    private static final String LOG_TAG = "Gyroscope";
    private static Gyroscope instance;

    public static Gyroscope getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = new Gyroscope(context);
        }
        return instance;
    }

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private DataWriterGyro fileWriter;
    private boolean isSensing;

    protected Gyroscope(final Context context)
    {
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.isSensing = false;
    }

    public boolean isSensing()
    {
        return isSensing;
    }

    public void start() throws IOException
    {
        fileWriter = new DataWriterGyro();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
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
