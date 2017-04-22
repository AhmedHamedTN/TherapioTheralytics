package com.example.ahmed.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.MainActivity;
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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed on 9/17/16.
 */
public class sendLocation extends AsyncTask<Void, Void, Void> {


    public sendLocation(Context context) {
        this.context = context;
    }

    Context context;

    Date currentDate = new Date();
    long dt = currentDate.getTime() / 1000;

    //String uID = Activity_Login.getUserID();
    String uID = MainActivity.ALUSER;
    Double lat = MainActivity.getLatit();
    Double longi = MainActivity.getLongit();

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
/*            GUIStatics.currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            GUIStatics.latitude = obj.getLatitude();
            GUIStatics.longitude = obj.getLongitude();
            GUIStatics.currentCity= obj.getSubAdminArea();
            GUIStatics.currentState= obj.getAdminArea();*/
            //add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + " " + obj.getAdminArea();
            //add = add + "\n" + obj.getPostalCode();
            //add = add + " " + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();
            //add = add + "\n" + obj.getSubThoroughfare();

            //Log.d("IGA Address", add);
            //Toast.makeText(context, "Address=> " + add, Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {


        String place = getAddress(lat, longi);


        try {
            JSONObject json = new JSONObject();
            json.put("id", null);
            json.put("uid", uID);
            json.put("lati", lat);
            json.put("longi", longi);
            json.put("place", place);
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
            String url = Config_URL.URL_STORE_LOCATION;

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


        return null;
    }

}
