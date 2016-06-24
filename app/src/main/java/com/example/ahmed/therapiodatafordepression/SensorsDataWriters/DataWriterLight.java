package com.example.ahmed.therapiodatafordepression.SensorsDataWriters;

import android.hardware.SensorEvent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ahmed on 16/06/16.
 */
public class DataWriterLight {
    private File dataFile;
    private BufferedWriter writer;

    public DataWriterLight() throws IOException
    {
        dataFile = createLightFile();
        writer = new BufferedWriter(new FileWriter(dataFile));

        Log.d("DataWriterLight", "Writing to: "+dataFile.getAbsolutePath());
    }

    private File createLightFile() {
        File directory = new File(Environment.getExternalStorageDirectory(), "CSVData");
        File directory2 = null;

        if (directory.exists()) {
            directory2 = new File(directory, "LightData");
            if (!directory2.exists()) {
                directory2.mkdirs();
            }
        } else {
            directory.mkdir();
        }

        return new File(directory2, "LIGHT_" + System.currentTimeMillis() + ".csv");
    }

    public void append(SensorEvent event) throws IOException
    {
        String row = "" + System.currentTimeMillis();
        for (int i=0; i<3; i++)
        {
            row += "," + event.values[i];
        }
        writer.write(row + "\n");
    }

    public void finish() throws IOException
    {
        writer.flush();
        writer.close();
    }
}
