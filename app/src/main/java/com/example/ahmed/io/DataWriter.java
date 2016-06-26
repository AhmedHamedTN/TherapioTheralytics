package com.example.ahmed.io;

import android.content.Context;
import android.hardware.SensorEvent;
import android.os.Environment;
import android.util.Log;

import com.example.ahmed.utils.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ahmed on 16/06/16.
 */

/**
 * This class will write data from the sensor in your SD card
 * under a folder called CSVData
 * and within that folder
 * you find folders of the appropriate sensor logs in CSV format
 * <p/>
 * <p/>
 * Same goes with all similar classes
 */
public class DataWriter {
    private File dataFile;
    private BufferedWriter writer;
    private Context context;

    public DataWriter(Context context, Constants.SensorType sensorType) {
        try {
            this.context=context;
            dataFile = createFile(sensorType);
            writer = new BufferedWriter(new FileWriter(dataFile));
            Log.d("DataWriter", "Writing to: " + dataFile.getAbsolutePath());
        } catch (IOException e) {
        }
    }

    private File createFile(Constants.SensorType sensorType) {
        File directory = new File(Environment.getExternalStorageDirectory(), Constants.mainDirectoryName);
        directory = new File(directory, sensorType.directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, String.format(sensorType.fileName, System.currentTimeMillis()));

    }

    public void append(SensorEvent event) throws IOException {
        String row = "" + System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            row += "," + event.values[i];
        }
        writer.write(row + "\n");
    }

    public void finish() throws IOException {
        writer.flush();
        writer.close();
        new Thread(new Runnable() {
            @Override
            public void run() {

                UploadFile.uploadFile(context,dataFile);

            }
        }).start();
    }
}
