package com.example.ahmed.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.io.SaveFile;
import com.example.ahmed.utils.Constants;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by ahmed on 23/06/16.
 */
public class PhoneCallService extends BroadcastReceiver {

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static MediaRecorder recorder = new MediaRecorder();
    public static boolean wasRinging = false;
    public static boolean setUpRecording = false;
    static File audioFile;
    static private boolean recordstarted = false;
    public SaveFile SaveRecording = new SaveFile();
    Context context;
    Handler updateHandler = new Handler();
    Bundle bundle;
    String state;
    String outCall;

    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("AUDIO_SOURCE", 0);
        SharedPreferences.Editor editor = settings.edit();
        this.context = context;
        Boolean SwitchState = settings.getBoolean("SWITCH", true);
        if (SwitchState) {
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
//                    Toast.makeText(context, "state changed : " + state, Toast.LENGTH_LONG).show();
                    Log.d("State ", state);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        String inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        editor.putString("inCall", inCall);
                        editor.commit();
                        wasRinging = true;
                        setUpRecording = true;
                        //Toast.makeText(context, "INcoming : " + inCall, Toast.LENGTH_LONG).show();
                        Log.d("Incoming Call", " from : " + inCall + "wasRinging : " + wasRinging);
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        String inCall = settings.getString("inCall", "");
                        Log.d("message", " Oh ! he picked the call" + inCall);
                        if (wasRinging == true) {

                            Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();
                            Log.d("Answered the call", "from :" + inCall);
                            if (setUpRecording) {
                                recordstarted = true;
                                setUpRecording = false;
                                //            SaveRecording.startRecording();
                                try {
                                    String method = settings.getString("AUDIO_SOURCE", "");
                                    Log.d("message", method + " : " + inCall);
                                    audioFile = SaveRecording.startRecording(method, inCall, "-In");
                                    Log.d("recording Stared", "File name" + audioFile.getAbsolutePath().toString());

                                } catch (Exception e) {
                                    Log.d("Recording Exception1 : ", e.toString());
                                }

                            }
                        }

                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                        if (recordstarted) {
//                            recorder.stop();
                            Log.d("Message", "Stopping recording");
                            SaveRecording.stopRecording();
//                            saverecordings.stopRecording();
                            Thread xx = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d("Thread", "initiated" + audioFile.getAbsolutePath().toString());
                                        sleep(1000);
                                        uploadFile(context, audioFile);
                                        Log.d("Thread", "Executed");
                                    } catch (Exception e) {
                                        Log.d("thread Exception", e.toString());
                                    }
                                }
                            };
                            xx.start();

                            //SaveFile.recorder.stop();
                            //updateHandler.postDelayed(timerRunnable, 1000);
                            recordstarted = false;
                        }

                    }

                }
            } else if (intent.getAction().equals(ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
                    //saverecordings.startRecording();
                    try {
                        String method = settings.getString("AUDIO_SOURCE", "");
                        Log.d("message", method);
                        audioFile = SaveRecording.startRecording(method, outCall, "-Out");
                        Log.d("recording Stared", "File name" + audioFile.getAbsolutePath().toString());
                    } catch (Exception e) {
                        Log.d("Recording Exception2", e.toString());
                    }
                    recordstarted = true;
                    setUpRecording = false;

                }
            }
        }
    }

    public void uploadFile(Context context, File fileName) {
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

            /*client.createDirectory("/"+"ahmed");
            client.changeDirectory("/"+"ahmed");*/

            client.upload(fileName, new MyTransferListener());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("upload Error", e.toString());
            Toast.makeText(context, "client not connected !", Toast.LENGTH_LONG).show();
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

}
