package com.example.ahmed.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.helper.SQLiteHandler;
import com.example.ahmed.helper.SessionManager;
import com.example.ahmed.service.CSVtriggerPython;
//import com.example.ahmed.service.CSVtriggerPython;
import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.Auth.Activity_Register;
import com.example.ahmed.ui.MainActivity;
import com.example.ahmed.utils.Constants;
import com.example.ahmed.utils.volley.Config_URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;

/**
 * Created on 24-Jun-16.
 */

public class UploadFile extends AsyncTask<Void, Void, Void> {

    private Context context;
    private File fileName;
    String path;
    private long dt;


    public UploadFile(Context c, File f){
        context = c;
        fileName = f;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (path!=null) {
            new CSVtriggerPython(path).execute();
        }
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {

        //final String uID = Activity_Login.getUserID();
        final String uID = MainActivity.ALUSER;

        //public static void uploadFile(Context context, File fileName) {

        /*String uID = Activity_Login.userID;*/
            //Activity_Login AL = new Activity_Login();

            //String uID = Activity_Login.getuID();

       /* SharedPreferences shared = context.getSharedPreferences(Activity_Login.MyPREFERENCES, Activity_Login.MODE_PRIVATE);

        String uID = (shared.getString("userid", ""));*/

        try {
            String sourceFileUri = fileName.getAbsolutePath();
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

                try {
                    String upLoadServerUri = Config_URL.URL_STORE_ACC_File;

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

                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }




            /*FTPClient client = new FTPClient();
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
                }*/

            /*try {
                client.changeDirectory(name);
            } catch(FTPException e) {
                client.createDirectory(name);
                client.changeDirectory(name);
            }*/

                //client.upload(fileName, new MyTransferListener());

                // prepare to insert into MySql database

                String fileType = "CSV";
                 path = "/"+((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId()+"/"+fileName.getName();

        //path = fileName.getName();


/*        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dd = dateFormat.parse(date);
            long dt = dd.getTime() / 1000;
            Log.d("Date normal :","" + dd.toString());
            Log.d("Date milliseconds :","" + dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        Date currentDate = new Date();
        long dt = currentDate.getTime() / 1000;



        //String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                /*Date currentDate = new Date();
                long dt = currentDate.getTime() / 1000;
                Log.d("Dateeeee :","" + currentDate.getTime());
                Log.d("Date milliseconds :","" + dt);
*/
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
                           /*if (path!=null) {
            new CSVtriggerPython(path).execute();
        }*/         "UTF8")));
                    Log.d("creating", "connecting");
                    request.setHeader("json", json.toString());
                    HttpResponse response = httpclient.execute(request);
                    HttpEntity entity = response.getEntity();
                    // If the response does not enclose an entity, there is no need
                    Log.d("creating", "done");
                } catch (Throwable t) {
                    Log.d("Error ", t.toString());
                }

/*                Thread t = new Thread(){

                    @Override
                    public void run() {
                        URL url;
                        HttpURLConnection urlConnection = null;
                        try {

                            url = new URL(Config_URL.URL_EXTRACT_CSV_FTR+"?userid="+uID);
                            urlConnection = (HttpURLConnection) url
                                    .openConnection();

                            InputStream in = urlConnection.getInputStream();

                            InputStreamReader isw = new InputStreamReader(in);

                            int data = isw.read();
                            while (data != -1) {
                                char curhttp://www.tutorialspoint.com/microsoft_technologies_tutorials.htmrent = (char) data;
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
                    }
                };
                t.start();*/


           /* } catch (Exception e) {
                e.printStackTrace();
                Log.e("upload Error", e.toString());
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    Log.e("upload Error", e.toString());
                    //e2.printStackTrace();
                }
            }*/

/*        try {

            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(Config_URL.URL_UPLOAD_FILE).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            Thread.sleep(1000);
            InputStream fis = new BufferedInputStream(new FileInputStream(fileName));


            byte[] bytes = IOUtils.toByteArray(fis);

            for (int i = 0; i < bytes.length; i++) {
                os.write(fis.read());
            }

            os.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            httpUrlConnection.getInputStream()));

            String s = null;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            in.close();
            fis.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }*/

        //fileName.delete();
        //}

        return null;
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
