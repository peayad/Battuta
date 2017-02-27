package iti_edu.battuta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    final private String TAG = "ptr-AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Trip trip = (Trip) intent.getSerializableExtra("trip");
        BattutaReminder.showNotification(context, trip);
    }
}
