package iti_edu.battuta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "ptr-bootReceiver";

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        BattutaDBadapter mDBadapter = new BattutaDBadapter(context);
        BattutaReminder.updateAllReminders(context, mDBadapter.getAllTrips());
    }
}
