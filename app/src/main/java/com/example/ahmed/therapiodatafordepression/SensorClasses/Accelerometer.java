package com.example.ahmed.therapiodatafordepression.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.SensorsDataWriters.DataWriterAcc;
import com.example.ahmed.therapiodatafordepression.ServiceThatRecordCalls.PhoneCallService;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by ahmed on 16/06/16.
 */
public class Accelerometer implements SensorEventListener
{
    Context context;
    private static final String LOG_TAG = "Accelerometer";
    private static Accelerometer instance;

    public static Accelerometer getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = new Accelerometer(context);
        }
        return instance;
    }

    public static SensorManager mSensorManager;
    public static Sensor mSensor;
    public static DataWriterAcc fileWriter;
    public static boolean isSensing;


    protected Accelerometer(final Context context)
    {
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.isSensing = false;
    }

    public boolean isSensing()
    {
        return isSensing;
    }


    public void start() throws IOException
    {
        fileWriter = new DataWriterAcc();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        isSensing = true;
    }

    public void stop() throws IOException
    {
        fileWriter.finish();
        mSensorManager.unregisterListener(this);
        isSensing = false;
        //Toast.makeText(context, datafile.getAbsolutePath(), LENGTH_LONG).show();
        //fileWriter.uploadFile(context, datafile);
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
