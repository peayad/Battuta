package iti_edu.battuta;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class BattutaReminder {

    private static final String TAG = "ptr-BattutaReminder";

    static void updateAllReminders(Context context, ArrayList<Trip> tripList) {
        for (Trip trip : tripList) {
            if (trip.getIsDone() > 0) continue;
            createReminder(context, trip);
        }
    }

    static void createReminder(Context context, Trip trip) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("trip", (Serializable) trip);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        try {
            c.setTime(sdf.parse(trip.getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    static void deleteReminder(Context context, int intentID) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intentID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intentID);
    }

    static void removeAllReminders(Context context, ArrayList<Trip> tripArrayList){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for(Trip trip: tripArrayList){
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    static void showNotification(Context context, Trip trip) {
        String tripInfo = trip.getEndPoint() + "\n" + trip.getDateTime();

        //Build the content of Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(trip.getTitle());
        builder.setContentText(trip.getEndPoint());
        builder.setSmallIcon(R.drawable.ic_compass);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(tripInfo));
        builder.setTicker("Upcoming Trip: " + trip.getTitle());
        builder.setAutoCancel(true);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            long[] v = {100, 1000};
            builder.setVibrate(v);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //provide Explicit intent,pending Intent and Back Stack Task Builder for Action Buttons
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("trip", (Serializable) trip);
        intent.putExtra("isFromNotification", true);

        //Add the Back Stack using TaskBuilder and set the Intent to Pending Intent
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(intent);

//        PendingIntent pi = stackBuilder.getPendingIntent(trip.getId(), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi = PendingIntent.getActivity(context,trip.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        // Notification through notification Manage
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(trip.getId(), notification);
    }
}
