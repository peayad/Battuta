package iti_edu.battuta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class BattutaReminder {

    private static final String TAG = "ptr-BattutaReminder";

    static void updateAllReminders(Context context, ArrayList<Trip> tripList) {
        for (Trip trip : tripList) {
            createReminder(context, trip);
        }
    }

    static void createReminder(Context context, Trip trip) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("trip", (Serializable) trip);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(), alarmIntent, 0);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy  hh:mm aa");

        try {
            c.setTime(sdf.parse(trip.getDateTime()));
            c.add(Calendar.MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "didn't happen");
        }

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    static void deleteReminder(Context context, int intentID) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intentID, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
}
