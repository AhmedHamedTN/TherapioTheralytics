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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by ahmed on 23/06/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String uname="ahmed@anis.tunisia-webhosting.com";
    public static final String pw="ahmedahmed";
    public static final String host="ftp.anis.tunisia-webhosting.com";


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
            stopSensing(arg0);
        }else {
            startSensing();

        }

    }

    public void uploadFile( File fileName) {
        FTPClient client = new FTPClient();
        /*SharedPreferences userShare = context.getSharedPreferences("AUDIO_SOURCE", 0);
        String USERNAME = userShare.getString("USERNAME", "");
        String PASSWORD = userShare.getString("PASSWORD", "");
        String FTP_HOST = userShare.getString("FTP_HOST", "");*/
        Log.d("data" ,uname+" "+pw+" "+host);
        try {

            client.connect(host);
            client.login(uname, pw);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/");

            /*client.createDirectory("/ahmed");
            client.changeDirectory("/ahmed");*/

            client.upload(fileName, new MyTransferListener());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("upload Error", e.toString());
            //Toast.makeText(context,"client not connected !", Toast.LENGTH_LONG).show();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                Log.e("upload Error", e.toString());
                //e2.printStackTrace();
            }
        }
    }

    /*******
     * Used to file upload and show progress
     **********/

    private class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            //btn.setVisibility(View.GONE);
            // Transfer started
            //Toast.makeText(context, " Upload Started ...", Toast.LENGTH_SHORT).show();
            Log.d("upload", " started");
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            // Toast.makeText(context, " transferred ..." + length, Toast.LENGTH_SHORT).show();
            Log.d("upload", " transferred");
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {

            //btn.setVisibility(View.VISIBLE);
            // Transfer completed

            //    Toast.makeText(context, " completed ...", Toast.LENGTH_SHORT).show();
            Log.d("upload", " completed");
            //System.out.println(" completed ..." );
        }

        public void aborted() {

            //btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            // Toast.makeText(context," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
            Log.d("upload", " transfer aborted");
        }

        public void failed() {

            //btn.setVisibility(View.VISIBLE);
            // Transfer failed
            //  System.out.println(" failed ..." );
            Log.d("upload", " failed");
        }

    }


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

    public void stopSensing(Context context)
    {
        if ((accelerometer.isSensing()) && (light.isSensing()) && (gyroscope.isSensing()))
        {
            try
            {
                accelerometer.stop();
                gyroscope.stop();
                light.stop();

                Thread t = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        File datafile = DW.getDataFile();
                        Log.d("EEEEEEEEEEEEEEEEEEEEE",datafile.getName());
                        uploadFile(datafile);
                    }
                });
                t.start();
                Log.d("DW", "SUCCESS");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
