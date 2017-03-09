package iti_edu.battuta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "ptr-bootReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        // TODO firebase database
        FireDB fireDB = FireDB.getInstance();
        fireDB.userDB_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Trip> tripList = new ArrayList<Trip>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Trip trip = data.getValue(Trip.class);
                    tripList.add(trip);
                }
                FireDB.updateTripLists(tripList);
                BattutaReminder.updateAllReminders(context, FireDB.upcommingTrips);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
