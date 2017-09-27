package com.example.ahmed.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.request.SimpleMultiPartRequest;
import com.example.ahmed.io.SaveFile;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.Auth.Activity_Register;
import com.example.ahmed.ui.MainActivity;
import com.example.ahmed.utils.Constants;
import com.example.ahmed.utils.volley.AppController;
import com.example.ahmed.utils.volley.Config_URL;
import com.example.ahmed.utils.volley.MyApplication;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by ahmed on 23/06/16.
 */
public class PhoneCallService extends BroadcastReceiver {

    //String uID = Activity_Login.getUserID();
    String uID = MainActivity.ALUSER;


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
    private int mServerResponseCode;

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
                    Toast.makeText(context, "state changed : " + state, Toast.LENGTH_LONG).show();
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
                        if (wasRinging) {

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
                                    String af = audioFile.getAbsolutePath().replaceAll("\\s+", "");
                                    Log.d("recording Stared", "File name : " + af);

                                } catch (Exception e) {
                                    Log.d("Recording Exception1 : ", e.toString());
                                }

                            }
                        }

                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        Toast.makeText(context, "Done || Calling ", Toast.LENGTH_LONG).show();
                        if (recordstarted) {
//                          recorder.stop();
                            Log.d("Message", "Stopping recording");
                            SaveRecording.stopRecording();
//                          saverecordings.stopRecording();
                            Thread xx = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d("Thread", "initiated" + audioFile.getAbsolutePath().toString());
                                        sleep(1000);
                                        //uploadFile(context, audioFile);
                                        //Upload(audioFile.getAbsolutePath().toString());
                                        //uploadFile1(audioFile.getAbsolutePath().toString());
                                        uploadFileFTP(context, audioFile);
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
                    Toast.makeText(context, "Calling : " + outCall, Toast.LENGTH_LONG).show();
                    //saverecordings.startRecording();
                    try {
                        String method = settings.getString("AUDIO_SOURCE", "");
                        Log.d("message", method);
                        audioFile = SaveRecording.startRecording(method, outCall, "-Out");
                        Log.d("recording Stared", "File name " + audioFile.getAbsolutePath().toString());
                    } catch (Exception e) {
                        Log.d("Recording Exception2", e.toString());
                    }
                    recordstarted = true;
                    setUpRecording = false;

                }
            }
        }

    }



 /*   private void Upload(final String iPath) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Config_URL.URL_STORE_PHONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addFile("image", iPath);
        MyApplication.getInstance().addToRequestQueue(smr);

    }*/


    public int uploadFile1(String sourceFileUri) {

        long t1 = System.currentTimeMillis();

        String filePath[] = sourceFileUri.split("/");
        String fileName = filePath[filePath.length - 1];
        Log.d(TAG, "Filename: " + fileName);

        fileName = URLEncoder.encode(fileName);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e(TAG, "Source file does not exist : " + "nope");

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Config_URL.URL_STORE_PHONE_File);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                conn.setConnectTimeout(5 * 6 * 10 * 1000);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                mServerResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i(TAG, "HTTP Response is : "+ serverResponseMessage + ": " + mServerResponseCode);

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Log.e(TAG, "error: " + ex.getMessage(), ex);

            }

        }

        if (mServerResponseCode == 200) {
            return 1;
        } else {
            return 2;
        }

    }


    public void uploadFileFTP(Context context, File fileName) {

        FTPClient client = new FTPClient();
        //client.setSecurity(FTPClient.SECURITY_FTPS); // enables FTPS
        /*SharedPreferences userShare = context.getSharedPreferences("AUDIO_SOURCE", 0);
        String USERNAME = userShare.getString("USERNAME", "");
        String PASSWORD = userShare.getString("PASSWORD", "");
        String FTP_HOST = userShare.getString("FTP_HOST", "");*/
        try {

            client.connect(Constants.FTP_HOST);
            client.login(Constants.FTP_USER_NAME, Constants.FTP_PWD);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/filesupload/speech");

            /*try {
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            } catch(FTPException e) {
                client.createDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            }*/

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
            Date currentDate = new Date();
            //String currentDateFormatted = dateFormat.format(currentDate);
            long dt = currentDate.getTime() / 1000;

            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("filepath", path);
                json.put("type", fileType);
                json.put("uid", uID);
                json.put("created_at", dt);
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


            new AudioTriggerPython(path).execute();


           /* URL url = new URL(Config_URL.URL_EXTRACT_AUDIO_FTR);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("userid", uID);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            Log.d("python WAV launched", "Query : "+query);*/


        }
        catch (Exception e) {
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

       /* catch (FTPIllegalReplyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        //audioFile.delete();
    }



    public void uploadFile(Context context, File fileName) {

        try {
            String sourceFileUri = audioFile.getAbsolutePath();
            Log.d("Source file :  ", sourceFileUri);

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {
                Log.d(" enter the if", "true");
                try {
                    String upLoadServerUri = Config_URL.URL_STORE_PHONE_File;

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    if (serverResponseCode == 200) {

                        Log.d(" FILE UPLOAD", "Complete");

                        // messageText.setText(msg);
                        //Toast.makeText(ctx, "File Upload Complete.",
                        //      Toast.LENGTH_SHORT).show();

                        // recursiveDelete(mDirectory1);

                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();
                    Log.d(" Exception", "not uploaded");


                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }


        //FTPClient client = new FTPClient();
        /*SharedPreferences userShare = context.getSharedPreferences("AUDIO_SOURCE", 0);
        String USERNAME = userShare.getString("USERNAME", "");
        String PASSWORD = userShare.getString("PASSWORD", "");
        String FTP_HOST = userShare.getString("FTP_HOST", "");*/
/*        try {

            client.connect(Constants.FTP_HOST);
            client.login(Constants.FTP_USER_NAME, Constants.FTP_PWD);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/");

            try {
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            } catch(FTPException e) {
                client.createDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
                client.changeDirectory(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
            }*/

            /*try {
                client.changeDirectory(name);
            } catch(FTPException e) {
                client.createDirectory(name);
                client.changeDirectory(name);
            }
*/
            //client.upload(fileName, new MyTransferListener());

            // prepare to insert into MySql database

            String fileType = "WAV";
            String path = "/"+((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId()+"/"+fileName.getName().replaceAll("\\s+","");
        //String path = fileName.getName();
        //String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        //String currentDateFormatted = dateFormat.format(currentDate);
            long dt = currentDate.getTime() / 1000;

            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("filepath", path);
                json.put("type", fileType);
                json.put("uid", uID);
                json.put("created_at", dt);
                //Log.d("creating", subject);
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,
                        90000);
                HttpConnectionParams.setSoTimeout(httpParams, 90000);
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


            new AudioTriggerPython(path).execute();


            /*URL url = new URL(Config_URL.URL_EXTRACT_AUDIO_FTR);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("userid", uID);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            Log.d("python WAV launched", "Query : "+query);*/


/*        } catch (Exception e) {
            e.printStackTrace();
            Log.e("upload Error", e.toString());
            Toast.makeText(context, "client not connected !", Toast.LENGTH_LONG).show();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                Log.e("upload Error", e.toString());
                //e2.printStackTrace();
            }
        }*/

        //audioFile.delete();
    }

    /*class TriggerPythonAudio extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {



                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Config_URL.URL_EXTRACT_AUDIO_FTR+"?userid="+uID);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        System.out.print(current);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }



                //Log.d("python WAV launched", "Query : " + query);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/

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
