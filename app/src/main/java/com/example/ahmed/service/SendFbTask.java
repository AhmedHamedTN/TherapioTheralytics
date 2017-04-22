package com.example.ahmed.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.MainActivity;
import com.example.ahmed.utils.volley.Config_URL;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
*
 * Created by ahmed on 8/22/16.


*/


public class SendFbTask extends AsyncTask<Void, Void, Void> {


    private JSONArray jsonArray;
    private JSONObject jsonObject;
    int numbOfPosts = 0;
    //String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    Date currentDate = new Date();
    long dt = currentDate.getTime() / 1000;

    //String uID = Activity_Login.getUserID();
    String uID = MainActivity.ALUSER;
    @Override
    protected Void doInBackground(Void... params) {

        Log.d("Datee 1 :","" + currentDate);


        Log.d("Dateeeee long :","" + currentDate.getTime());

        Log.d("Date milliseconds :","" + dt);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                             /* handle the result */
                        jsonObject = response.getJSONObject();
                        String data = "";
                        try {
                            //Get the instance of JSONArray that contains JSONObjects
                            jsonArray = jsonObject.optJSONArray("data");

                            //Iterate the jsonArray and print the info of JSONObjects
                            int numbOfPosts = 0;
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String PostID = jsonObject.optString("id").toString();
                                String message = jsonObject.optString("message").toString();
                                String time = jsonObject.optString("created_time").toString();
                                String story = jsonObject.optString("story").toString();

                                String PostDate = time.substring(0,10);
                                //Log.d(" POSTDATE : ",PostDate);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String todayDate = dateFormat.format(date);
                                //Log.d(" TODAYSDATE : ",todayDate);
                                if (PostDate.equals(todayDate)){
                                    numbOfPosts ++;
                                }
                                data += "\nNode" + i + " : \n id = " + PostID +" : \n post = " + message + " \n Time = " + time + " \n Story = "+ story;
                            }
                            Log.d("NumberOFFFFFFFFPosts", numbOfPosts + "  user id :"+ uID + "  date :" + currentDate );
                            Log.d("Data is :", data);

                            final int finalNumbOfPosts = numbOfPosts;




                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("id", null);
                                        json.put("numpost", finalNumbOfPosts);
                                        json.put("created_at", dt);
                                        json.put("uid", uID);
                                        Log.d("Ccreating", String.valueOf(finalNumbOfPosts));
                                        HttpParams httpParams = new BasicHttpParams();
                                        HttpConnectionParams.setConnectionTimeout(httpParams,
                                                9000);
                                        HttpConnectionParams.setSoTimeout(httpParams, 9000);
                                        HttpClient httpclient = new DefaultHttpClient(httpParams);
                                        //
                                        //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" +
                                        //             "json={\"UserName\":1,\"FullName\":2}";
                                        String url = Config_URL.URL_STORE_FB;

                                        HttpPost request = new HttpPost(url);
                                        request.setEntity(new ByteArrayEntity(json.toString().getBytes(
                                                "UTF8")));
                                        Log.d("creating", "connecting");
                                        request.setHeader("json", json.toString());
                                        HttpResponse responses = httpclient.execute(request);
                                        HttpEntity entity = responses.getEntity();
                                        // If the response does not enclose an entity, there is no need
                                        Log.d("creating", "done");
                                    } catch (Throwable t) {
                                    }
                                }
                            };

                            thread.start();
                            } catch (JSONException e) {e.printStackTrace();}
                        }
                    }
            ).executeAsync();

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