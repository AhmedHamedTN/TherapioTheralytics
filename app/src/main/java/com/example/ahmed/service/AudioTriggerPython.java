package com.example.ahmed.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.MainActivity;
import com.example.ahmed.utils.volley.Config_URL;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ahmed on 9/3/16.
 */


class AudioTriggerPython extends AsyncTask<Void, Void, Void> {


    //String uID = Activity_Login.getUserID();
    private String uID = MainActivity.ALUSER;
    private String path;

    public AudioTriggerPython(String path) {
        this.path=path;
    }

    @Override
    protected Void doInBackground(Void... params) {

        //Log.d("user id before TRY : ",uID);
        //String sth = "&audiopath=\""+path+"\"";
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(Config_URL.URL_EXTRACT_AUDIO_FTR+"?userid="+uID+"&audiopath=\""+path+"\"");

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
}
