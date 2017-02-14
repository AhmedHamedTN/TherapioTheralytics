package com.example.ahmed.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.R;
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

import java.util.Date;

/**
 * Created by ahmed on 2/11/17.
 */
public class ResultActivityAppetite extends AppCompatActivity{

    public static String state;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    //static String state;
    String uID = MainActivity.ALUSER;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_appetite);


        final RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radio);
        Button bt = (Button) findViewById(R.id.button3);

        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radiogroup.getCheckedRadioButtonId();

                // find the radio button by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(ResultActivityAppetite.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();

                state = (String) radioButton.getText();

                new sendResultAppetite().execute();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static String getState() {
        return state;
    }


    public class sendResultAppetite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

/*
            // **** YOUR CODE **** BEGIN ****
            long ts = System.currentTimeMillis();
            Date localTime = new Date(ts);
            String format = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            // Convert Local Time to UTC (Works Fine)
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gmtTime = new Date(sdf.format(localTime));
            System.out.println("Local:" + localTime.toString() + "," + localTime.getTime() + " --> UTC time:"
                    + gmtTime.toString() + "," + gmtTime.getTime());

            // **** YOUR CODE **** END ****

            // Convert UTC to Local Time
            Date fromGmt = new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
            System.out.println("UTC time:" + gmtTime.toString() + "," + gmtTime.getTime() + " --> Local:"
                    + fromGmt.toString() + "-" + fromGmt.getTime());
            long locally = fromGmt.getTime() ;
            Log.d("Date milliseconds :","" + locally);*/


          /*  String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
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


            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("uid", uID);
                json.put("state", state);
                //json.put("score", sc);
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
                String url = Config_URL.URL_STORE_APPETITE;

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





 /*   public class sendAppetite extends AsyncTask<Void, Void, Void> {

        //private final String s;

        //private final Context context;


        Date currentDate = new Date();
        long dt = currentDate.getTime() / 1000;

    *//*public sendAppetite(Context context, String s){
        this.context= context;
        this.s = s;
    }*//*

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


            try {
                JSONObject json = new JSONObject();
                json.put("id", null);
                json.put("uid", uID);
                json.put("state", state);
            *//*json.put("longi", longi);
            json.put("place", place);*//*
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
                String url = "http://10.42.0.1/therapio/android_login_api/insertAppetite.php";

                HttpPost request = new HttpPost(url);
                request.setEntity(new ByteArrayEntity(json.toString().getBytes(
                           *//*if (path!=null) {
            new CSVtriggerPython(path).execute();
        }*//*         "UTF8")));
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

*/

    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        btnDisplay = (Button) findViewById(R.id.button3);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                String state = (String) radioButton.getText();
                new sendResultAppetite().execute();
                Toast.makeText(ResultActivityAppetite.this, state
                        , Toast.LENGTH_SHORT).show();

            }

        });

    }




}
