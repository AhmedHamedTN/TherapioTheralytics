package com.example.ahmed.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.ResultActivityAppetite;
import com.example.ahmed.ui.ResultActivityConsumption;

/**
 * Created by ahmed on 3/22/17.
 */

public class sendConsumptionService extends Service {


    int NOTIFICATION_ID = 0;
    Context context;
    String sstring = "Tell us about your Consumption, all it takes is seconds" ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //SharedPreferences prefs = getSharedPreferences(
        //       DataAccessServer.PREFS_NAME, MODE_PRIVATE);
        NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        intent = new Intent(this.getApplicationContext(), ResultActivityConsumption.class);
        Bundle bundle = new Bundle();
        bundle.putString("warning", sstring);
        bundle.putInt("warningId", NOTIFICATION_ID);
        intent.putExtras(bundle);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivityConsumption.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_ONE_SHOT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {500,1000};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this.getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("How is your Consumption ?")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sstring))
                .setContentText(sstring)
                .setSound(alarmSound)
                .setVibrate(v)
                .setAutoCancel(true);


            /*String selectedSound = prefs.getString("selectedSound", "");
            if (!selectedSound.equals("")) {
                Uri alarmSound = Uri.parse(selectedSound);
                mBuilder.setSound(alarmSound);

            } else {
                Uri alarmSound = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);
            }

            if (prefs.getBoolean("isVibrateOn", false)) {
                long[] pattern = { 500, 500, 500, 500, 500, 500, 500, 500, 500 };
                mBuilder.setVibrate(pattern);
            }*/

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
