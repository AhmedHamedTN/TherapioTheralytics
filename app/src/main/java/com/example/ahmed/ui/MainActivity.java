package com.example.ahmed.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ahmed.listener.SensorEvent;
import com.example.ahmed.service.AlarmReceiver;
import com.example.ahmed.therapiodatafordepression.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {


    Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Calendar settings for AlarmReceiver
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 07);
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        // Retrieve a PendingIntent that will perform a broadcast
        alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30 * 1000, pendingIntent);


    }


    // When you click Start sensing button
    public void onSensingButtonClickedToStart(final View view) {
        if (!SensorEvent.isSensing) {
            SensorEvent.isSensing = true;

            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int interval = 10000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Toast.makeText(this, "Sensing On", Toast.LENGTH_SHORT).show();
        }
    }

    // when you click Stop sensing button
    public void onSensingButtonClickedToStop(final View view) {
        if (SensorEvent.isSensing) {

            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Sensing Off", Toast.LENGTH_SHORT).show();
            SensorEvent.isSensing = false;

        }
    }

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
        } else if (id == android.R.id.home) {
            Log.d("home", "clicked");
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        /*else if(id == R.id.action_refresh)
        {
            Log.d("Action" , "Refresh clicked");
            //refresh();
        }*/
        return super.onOptionsItemSelected(item);
    }
}
