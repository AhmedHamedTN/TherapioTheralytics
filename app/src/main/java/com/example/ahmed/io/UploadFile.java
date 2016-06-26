package com.example.ahmed.io;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.ahmed.utils.Constants;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;

/**
 * Created by Khalil on 24-Jun-16.
 */

public class UploadFile {


    public static void uploadFile(Context context, File fileName) {
        FTPClient client = new FTPClient();


        try {

            client.connect(Constants.FTP_HOST);
            client.login(Constants.FTP_USER_NAME, Constants.FTP_PWD);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/");

         try {
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            } catch(FTPException e) {
                client.createDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            }

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

    public static class MyTransferListener implements FTPDataTransferListener {

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
