package iti_edu.battuta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "ptr-bootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO loop through all upcoming trips in database and create reminders for them
        createReminder(context);
    }

    private void createReminder(Context context) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, pendingIntent);

    }
}
