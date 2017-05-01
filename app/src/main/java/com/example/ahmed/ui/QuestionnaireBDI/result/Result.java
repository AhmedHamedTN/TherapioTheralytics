package com.example.ahmed.ui.QuestionnaireBDI.result;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.MainActivity;
import com.example.ahmed.ui.QuestionnaireBDI.Beginners;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Result extends Beginners {


	public int correct1;
	public TextView stateview,scoreView,thankuView;
	public ImageView img;
	//String uID = Activity_Login.getUserID();
    String uID = MainActivity.ALUSER;
	int sc;
	//long dt;



	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		thankuView = (TextView)findViewById(R.id.text2);
		scoreView = (TextView) findViewById(R.id.text1);
		stateview= (TextView) findViewById(R.id.text3);
		img = (ImageView) findViewById(R.id.imageView1) ;
		//int id = getResources().getIdentifier("yourpackagename:drawable/" + StringGenerated, null, null);


		 Bundle b =this.getIntent().getExtras();
		   // String[] array=b.getStringArray("mes");
		    String score = getIntent().getStringExtra("score");
			String state = getIntent().getStringExtra("state");
			sc = Integer.parseInt(score);



		//int sc = Integer.parseInt(score);

		//t1.setText(recive);
		scoreView.setText("Score : "+ score);
		stateview.setText(state);
		img.setImageDrawable(getResources().getDrawable(R.drawable.therapioblue250));


		//result.setText(rec.toString());
/*		if(sc == 15 ){
			 img.setBackgroundResource(R.drawable.excelent);
	          t1.setText("Excellent");}
	        if(point > 10 && point <= 14){
	        	 img.setBackgroundResource(R.drawable.excelent);
	            t1.setText("Good");}
	        if(point > 5 && point <= 9 ){
	        	 img.setBackgroundResource(R.drawable.excelent);
	            t1.setText("Average");}
	        if(point <= 5 ){
	            t1.setText("Poor");
	        img.setBackgroundResource(R.drawable.excelent);
	        }*/




	       // DisplayMetrics metrics = getResources().getDisplayMetrics();
		      //----------ACTION BAR----------//
			 //   ActionBar actionbar = getActionBar();
			    //actionbar.setDisplayUseLogoEnabled(false);//
		        //actionbar.setDisplayHomeAsUpEnabled(true);//use icon with back graphics
		     //   Drawable d=getResources().getDrawable(R.drawable.therapioblue);
		     //   getActionBar().setBackgroundDrawable(d);
		        //----------ACTION BAR----------//
		     //   getActionBar().setIcon(android.R.color.transparent);


			       /* if (metrics.densityDpi >=240 )
			        {
			        	TextView title;
			 	        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			 	        getActionBar().setCustomView(R.layout.actiobar);
			 	        title= (TextView)findViewById(R.id.title);
			 	        title.setText("RESULT");


			        }
			        else if(metrics.densityDpi <240)
			        {
			        	getActionBar().hide();
			        }*/











	        int[] ques={R.drawable.excelent,R.drawable.excelent,R.drawable.excelent,R.drawable.excelent};










    ///////////////////////////////////////////////////////////
		final View Button = findViewById(R.id.home);
		if (Button != null) {
			Button.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Result.this,MainActivity.class);
                     startActivity(intent);
                     finish();
                }
            });
		}


		new sendResult().execute();

	}


	public class sendResult extends AsyncTask<Void, Void, Void>{

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
				json.put("result", state);
				json.put("score", sc);
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
				String url = Config_URL.URL_STORE_RESULT_BDI;

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


	@Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Result.this);
        builder.setMessage("Do you really want to exit?.").setCancelable(
                false).setPositiveButton("Back to Home screen",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	Result.this.finish();
                    	Intent intent = new Intent(Result.this,MainActivity.class);
	     		           startActivity(intent);

                    }
                }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }







	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}


	}



