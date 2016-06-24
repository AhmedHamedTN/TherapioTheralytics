package com.example.ahmed.therapiodatafordepression.ServiceThatRecordCalls;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by Khalil on 24-Jun-16.
 */
public class UploadFile {

    public static final String uname="ahmed@anis.tunisia-webhosting.com";
    public static final String pw="ahmedahmed";
    public static final String host="ftp.anis.tunisia-webhosting.com";








    public static class Operation extends AsyncTask<String, Void, String> {

        File fileName;

        public Operation(File fileName) {
            this.fileName = fileName;
        }

        public File getFileName() {
            return fileName;
        }

        public void setFileName(File fileName) {
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(String... params) {

            FTPClient client = new FTPClient();
        /*SharedPreferences userShare = context.getSharedPreferences("AUDIO_SOURCE", 0);
        String USERNAME = userShare.getString("USERNAME", "");
        String PASSWORD = userShare.getString("PASSWORD", "");
        String FTP_HOST = userShare.getString("FTP_HOST", "");*/
            Log.d("data" ,uname+" "+pw+" "+host);



            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
