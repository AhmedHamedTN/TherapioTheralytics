package com.example.ahmed.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.helper.SQLiteHandler;
import com.example.ahmed.helper.SessionManager;
import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.Auth.Activity_Register;
import com.example.ahmed.ui.MainActivity;
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
import java.util.HashMap;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;

/**
 * Created by Khalil on 24-Jun-16.
 */

public class UploadFile {

    public static void uploadFile(Context context, File fileName) {

        /*String uID = Activity_Login.userID;*/
        //Activity_Login AL = new Activity_Login();
        String uID = Activity_Login.getUserID();

       //String uID = Activity_Login.getuID();

       /* SharedPreferences shared = context.getSharedPreferences(Activity_Login.MyPREFERENCES, Activity_Login.MODE_PRIVATE);

        String uID = (shared.getString("userid", ""));*/

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

            /*try {
                client.changeDirectory(name);
            } catch(FTPException e) {
                client.createDirectory(name);
                client.changeDirectory(name);
            }*/

            client.upload(fileName, new MyTransferListener());

            // prepare to insert into MySql database

            String fileType = "CSV";
            String path = "/"+((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId()+"/"+fileName.getName();
            String currentDate = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());

            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("filepath", path);
                json.put("type", fileType);
                json.put("uid", uID);
                json.put("created_at", currentDate);
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
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                Log.e("upload Error", e.toString());
                //e2.printStackTrace();
            }
        }
        //fileName.delete();
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
