package com.example.ahmed.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.utils.volley.Config_URL;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

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

/*
Created by ahmed on 20/06/16.
*/

public class FbReceiver extends BroadcastReceiver {

    /*private CallbackManager mcallbackManager;
        private AccessTokenTracker mTokenTracker;
        private ProfileTracker mProfileTracker;
        private TextView largeTextView;*/


    @Override
    public void onReceive(Context context, Intent intent) {
        //new SenDem().execute();
        new SendFbTask().execute();
        //Toast.makeText(context, "posts rrr    "+ numbOfPosts, Toast.LENGTH_SHORT).show();
    }

    }

