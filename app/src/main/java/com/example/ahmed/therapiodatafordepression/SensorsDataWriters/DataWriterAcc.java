package com.example.ahmed.therapiodatafordepression.SensorsDataWriters;

import android.hardware.SensorEvent;
import android.os.Environment;
import android.util.Log;

import com.example.ahmed.therapiodatafordepression.ServiceThatRecordCalls.UploadFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ahmed on 16/06/16.
 */

/**
 *
 *
 This class will write data from the sensor in your SD card
 under a folder called CSVData
 and within that folder
 you find folders of the appropriate sensor logs in CSV format
 *
 *
 Same goes with all similar classes
 */
public class DataWriterAcc {

    private File dataFile;
    private BufferedWriter writer;

    public DataWriterAcc()  {
        try {
            dataFile = createAccFile();
            writer = new BufferedWriter(new FileWriter(dataFile));

        Log.d("DataWriterAcc", "Writing to: " + dataFile.getAbsolutePath());
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public File getDataFile() {
        return dataFile;
    }

    private File createAccFile()
    {
        File directory = new File(Environment.getExternalStorageDirectory(), "CSVData");
        File directory2 = new File(directory, "AccelerometerData");
        if (!directory2.exists())
        {
            directory2.mkdirs();
        }
        return new File(directory2, "ACC_" + System.currentTimeMillis() + ".csv");
    }

    public void append(SensorEvent event)
    {   try {
        String row = "" + System.currentTimeMillis();
        for (int i=0; i<3; i++)
        {
            row += "," + event.values[i];
        }

            writer.write(row + "\n");
        } catch (IOException e) {
            e.printStackTrace();

    }
    }

    public void finish()
    {
        try {
            writer.flush();
            writer.close();
            new Thread(new Runnable() {
                @Override
                public void run() {
                   UploadFile.uploadFile(dataFile);

                }
            }).start();

        } catch (IOException e)
        {
            e.printStackTrace();
        }



    }

}
