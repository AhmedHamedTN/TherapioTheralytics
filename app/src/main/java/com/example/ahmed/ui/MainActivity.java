package com.example.ahmed.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.helper.SQLiteHandler;
import com.example.ahmed.helper.SessionManager;
import com.example.ahmed.io.UploadFile;
//import com.example.ahmed.listener.AppUsageStats;
import com.example.ahmed.listener.SensorEvent;
//import com.example.ahmed.listener.UStats;
//import com.example.ahmed.listener.UStats;
import com.example.ahmed.service.AlarmReceiver;
import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.Welcome.InfoActivity;
import com.example.ahmed.ui.Welcome.WelcomeActivity;
import com.honu.aloha.WelcomeHelper;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;


    Button StrB, StpB, AppBtn;
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;


   /* public static String getuID() {
        return uID;
    }

    static String uID = Activity_Login.getUserID();*/
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String us= Activity_Login.getUserID();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (TextView) findViewById(R.id.textView6);
        btnLogout = (Button) findViewById(R.id.btnLogout);

/*
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorf = sharedpreferences.edit();
        editorf.putString("userid", us);
        editorf.commit();*/
        //Toast.makeText(this, "shaaaaaaaaaa"+ us , Toast.LENGTH_SHORT).show();
        /*SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("userid",null));

        Toast.makeText(this, "shaaaaaaaaaa"+ channel , Toast.LENGTH_SHORT).show();*/

        //String uID = (shared.getString("userid", ""));

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        // Displaying the user details on the screen
        txtName.setText(name);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        /**
         * Logging out the user. Will set isLoggedIn flag to false in shared
         * preferences Clears the user data from sqlite users table
         * */

        //Check if permission enabled
       /* if (AppUsageStats.getUsageStatsList(this).isEmpty()) {
            Intent intentP = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intentP);
        }*/

        SharedPreferences prefs = getSharedPreferences("appName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Intent intent;

        if (prefs.getBoolean("isInitialAppLaunch", true)) {
            //First Time App launched, you are putting isInitialAppLaunch to false and calling create splash activity.
            editor.putBoolean("isInitialAppLaunch", false);
            editor.commit();
            if (savedInstanceState == null)
                maybeShowWelcomeActivity();

        }

        // Calendar settings for AlarmReceiver
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 7);
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        // Retrieve a PendingIntent that will perform a broadcast
        alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30000, pendingIntent);


        StrB = (Button) findViewById(R.id.StartSensingButton);
        StpB = (Button) findViewById(R.id.StopSensingButton);
        //AppBtn = (Button) findViewById(R.id.AppUsageButton);



    /*    AppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UStats.printCurrentUsageStatus(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


        StrB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!SensorEvent.isSensing) {
                    SensorEvent.isSensing = true;

                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int interval = 20000;
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                    Toast.makeText(getApplicationContext(), "Sensing On", LENGTH_SHORT).show();
                }


            }
        });


        StpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SensorEvent.isSensing) {
                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(), "Sensing Off", LENGTH_SHORT).show();
                    SensorEvent.isSensing = false;
                }
             /*   try {
                    AppUsageStats.printCurrentUsageStatus(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void maybeShowWelcomeActivity() {
        // FOR TESTING ONLY: resetting the versionCode will FORCE the welcome activity to display every time
        WelcomeHelper.clearLastRunVersionCode(this);

        if (WelcomeHelper.isWelcomeRequired(this)) {
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    // When you click Start sensing button
    /*public void onSensingButtonClickedToStart(final View view) {
        if (!SensorEvent.isSensing) {
            SensorEvent.isSensing = true;

            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int interval = 20000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Toast.makeText(this, "Sensing On", LENGTH_SHORT).show();
        }
    }*/

    // when you click Stop sensing button
/*    public void onSensingButtonClickedToStop(final View view) {
        try {
            UStats.printCurrentUsageStatus(MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (SensorEvent.isSensing) {
            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Sensing Off", LENGTH_SHORT).show();
            SensorEvent.isSensing = false;
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //setButtonText();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d("action", "setting clicked");
            Intent intent1 = new Intent(this, SettingActivity.class);
            startActivity(intent1);
            return true;
        } else if (id == R.id.home) {
            Log.d("home", "clicked");
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.action_info) {
            startActivity(new Intent(this, InfoActivity.class));
            return true;
        }
        /*else if(id == R.id.action_refresh)
        {
            Log.d("Action" , "Refresh clicked");
            //refresh();
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // shared bla
        //SharedPreferences sharedpreferences = getSharedPreferences(Activity_Login.MyPREFERENCES, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.clear();
        //editor.commit();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }
}
