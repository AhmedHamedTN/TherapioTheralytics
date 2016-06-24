package com.example.ahmed.sensor;

import android.content.Context;

import com.example.ahmed.listener.SensorEvent;
import com.example.ahmed.utils.Constants;

/**
 * Created by Khalil on 24-Jun-16.
 */
public class Accelerometer extends SensorEvent {
    public Accelerometer(Context context) {
        super(context, Constants.SensorType.Accelerometer);
    }
}
