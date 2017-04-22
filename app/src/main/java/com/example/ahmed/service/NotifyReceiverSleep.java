package com.example.ahmed.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.ResultActivityAppetite;
import com.example.ahmed.ui.ResultActivitySleep;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by ahmed on 2/11/17.
 */
public class NotifyReceiverSleep extends BroadcastReceiver{


  /*  int NOTIFICATION_ID = 0;
    Context context;
    String sstring = "Tell us about your sleep, all it takes is seconds" ;*/


    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Notifying: ", LENGTH_SHORT).show();

        Intent service1 = new Intent(context, sendSleepService.class);
        context.startService(service1);

        //SharedPreferences prefs = getSharedPreferences(
        //       DataAccessServer.PREFS_NAME, MODE_PRIVATE);
     /*   NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        intent = new Intent(context, ResultActivitySleep.class);
        Bundle bundle = new Bundle();
        bundle.putString("warning", sstring);
        bundle.putInt("warningId", NOTIFICATION_ID);
        intent.putExtras(bundle);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivitySleep.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Did you sleep well ?")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sstring))
                .setContentText(sstring);
            *//*String selectedSound = prefs.getString("selectedSound", "");
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
            }*//*

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/

    }
}
