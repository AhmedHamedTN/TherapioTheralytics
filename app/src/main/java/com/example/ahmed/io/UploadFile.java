package com.example.ahmed.io;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.utils.Constants;

import java.io.File;
import java.io.StreamCorruptedException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;

/**
 * Created by Khalil on 24-Jun-16.
 */

public class UploadFile {

    private static Context mAppContext;

/*      TelephonyManager telephonyManager = (TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        String folder = telephonyManager.getDeviceId();*/

    public static void uploadFile(File fileName) {
        FTPClient client = new FTPClient();
        /*SharedPreferences userShare = context.getSharedPreferences("AUDIO_SOURCE", 0);
        String USERNAME = userShare.getString("USERNAME", "");
        String PASSWORD = userShare.getString("PASSWORD", "");
        String FTP_HOST = userShare.getString("FTP_HOST", "");*/
        try {

            client.connect(Constants.FTP_HOST);
            client.login(Constants.FTP_USER_NAME, Constants.FTP_PWD);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/");
            /*try {
                client.changeDirectory(folder);
            } catch(FTPException e) {
                client.createDirectory(folder);
                client.changeDirectory(folder);
            }*/
            client.upload(fileName, new MyTransferListener());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("upload Error", e.toString());
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                Log.e("upload Error", e.toString());
                //e2.printStackTrace();
            }
        }
    }

    private static class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            Log.d("upload", " started");

        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            Log.d("upload", " transferred");
        }

        public void completed() {

            // Transfer completed

            Log.d("upload", " completed");
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

    /*public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }*/
}
