package iti_edu.battuta;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

public class AlarmReceiver extends BroadcastReceiver{

    final private String TAG = "ptr-AlarmReceiver";

    Context appContext;
    String contentext ="Battuta reminder you for your information trip";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "i got here!");

        appContext = context;
        String tripTitle = intent.getStringExtra("title");
        Toast.makeText(context, tripTitle + "should begin now!", Toast.LENGTH_SHORT).show();
        showNormalViewNotification();
    }


    public void showNormalViewNotification() {
        // This function was made by fatma ali to create a notification

        //Build the content of Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext);
        builder.setContentTitle("Notification");
        builder.setContentText(contentext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(" hey this is ticker.....!");
        builder.setAutoCancel(true);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(appContext, notification);
            long[] v = {100,1000};
            builder.setVibrate(v);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }


        //provide Explicit intent,pending Intent and Back Stack Task Builder for Action Buttons

        Trip myTrip = new Trip("my trip title", null, null, "kaman 7aba", 0, "trip notes");

        Intent intent = new Intent(appContext, TripInfoActivity.class);
        intent.putExtra("trip", (Serializable) myTrip );

        //Add the Back Stack using TaskBuilder and set the Intent to Pending Intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        stackBuilder.addParentStack(TripInfoActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        // Notification through notification Manage
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1234, notification);
    }
}
