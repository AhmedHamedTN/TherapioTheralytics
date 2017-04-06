package com.example.ahmed.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.ResultActivityAppetite;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by ahmed on 2/11/17.
 */
public class NotifyReceiverAppetite extends BroadcastReceiver{


/*    int NOTIFICATION_ID = 0;
    Context context;
    String sstring = "Tell us about your appetite, all it takes is seconds" ;*/


    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Notifying: ", LENGTH_SHORT).show();

        Intent service1 = new Intent(context, sendAppetiteService.class);
        context.startService(service1);

    }
}
