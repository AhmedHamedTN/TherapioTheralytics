/*
package com.example.ahmed.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ahmed.ui.Auth.Activity_Login;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

*/
/**
 * Created by ahmed on 02/08/16.*//*



public class FbService extends Service {

    private JSONArray jsonArray;
    private JSONObject jsonObject = null;
    int numbOfPosts;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
    String uID = Activity_Login.getUserID();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
// handle the result

                        jsonObject = response.getJSONObject();
                        String data = "";
                        try {
                            //Get the instance of JSONArray that contains JSONObjects
                            jsonArray = jsonObject.optJSONArray("data");

                            //Iterate the jsonArray and print the info of JSONObjects
                            int j = 0;
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String PostID = jsonObject.optString("id").toString();
                                String message = jsonObject.optString("message").toString();
                                String time = jsonObject.optString("created_time").toString();
                                String story = jsonObject.optString("story").toString();

                                String PostDate = time.substring(0,10);
                                Log.d(" POSTDATE : ",PostDate);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String todayDate = dateFormat.format(date);
                                Log.d(" TODAYSDATE : ",todayDate);
                                if (PostDate.equals(todayDate)){
                                    j++;
                                    numbOfPosts = j;
                                }
                                data += "\nNode" + i + " : \n id = " + PostID +" : \n post = " + message + " \n Time = " + time + " \n Story = "+ story +" \n";

                            }
                            //txtName.setVisibility(View.VISIBLE);
                            //largeTextView.setText(numberOfLikes);
                            Log.d("NumberOFFFFFFFFPosts", String.valueOf(numbOfPosts) + uID + currentDate );
                            senDem();
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                }
        ).executeAsync();

        return super.onStartCommand(intent, flags, startId);
        //Toast.makeText(context, "posts rrr"+ String.valueOf(numbOfPosts), Toast.LENGTH_SHORT).show();
    }


    public void senDem(){
        try {
            JSONObject json = new JSONObject();
            json.put("id", null);
            json.put("numpost", String.valueOf(numbOfPosts));
            json.put("created_at", currentDate);
            json.put("uid", uID);
            Log.d("creating", String.valueOf(numbOfPosts));
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
}
*/
