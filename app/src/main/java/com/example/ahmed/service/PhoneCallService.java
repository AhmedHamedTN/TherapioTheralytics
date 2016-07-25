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
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.Auth.Activity_Register;
import com.example.ahmed.utils.Constants;
import com.example.ahmed.utils.volley.Config_URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;

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

        String uID = Activity_Login.getUserID();

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

            try {
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            } catch(FTPException e) {
                client.createDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            }

            /*try {
                client.changeDirectory(name);
            } catch(FTPException e) {
                client.createDirectory(name);
                client.changeDirectory(name);
            }
*/
            client.upload(fileName, new MyTransferListener());

            // prepare to insert into MySql database

            String fileType = "WAV";
            String path = "/"+((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId()+"/"+fileName.getName();
            //String currentDate = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());

            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("filepath", path);
                json.put("type", fileType);
                json.put("uid", uID);
                //json.put("created_at", currentDate);
                //Log.d("creating", subject);
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,
                        9000);
                HttpConnectionParams.setSoTimeout(httpParams, 9000);
                HttpClient httpclient = new DefaultHttpClient(httpParams);
                //
                //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" +
                //             "json={\"UserName\":1,\"FullName\":2}";
                String url = Config_URL.URL_STORE_PATH;

                HttpPost request = new HttpPost(url);
                request.setEntity(new ByteArrayEntity(json.toString().getBytes(
                        "UTF8")));
                Log.d("creating", "connecting");
                request.setHeader("json", json.toString());
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                // If the response does not enclose an entity, there is no need
                Log.d("creating", "done");
            } catch (Throwable t) {
            }


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
        audioFile.delete();
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
