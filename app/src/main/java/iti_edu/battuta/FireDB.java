package iti_edu.battuta;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FireDB {

    final private static String TAG = "ptr-FireDB";

    public static FirebaseAuth mAuth;
    public static FirebaseUser user;

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference userDB_ref;
//    public static userID;

    public static ArrayList<Trip> tripList = new ArrayList<>();
    public static ArrayList<Trip> upcommingTrips = new ArrayList<>();
    public static ArrayList<Trip> passedTrips = new ArrayList<>();
    public static ArrayList<Trip> selectedTrips = new ArrayList<>();

    public static boolean selectPassedTrips;

    private static FireDB ourInstance = new FireDB();

    public static FireDB getInstance() {
        return ourInstance;
    }

    private FireDB() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tripList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true); // allow us to save data offline
        String email = user.getEmail();
        String username = email.split("@")[0] + (email.split("@")[1]).split("\\.")[0] + (email.split("@")[1]).split("\\.")[1]; // store user's data under his unique email
        userDB_ref = firebaseDatabase.getReference().child(username);
    }

    public static void updateTripLists(ArrayList<Trip> newList) {
        tripList = newList;

        upcommingTrips.clear();
        passedTrips.clear();

        for (Trip t : tripList) {
            if (t.getIsDone() == 0) {
                upcommingTrips.add(t);
            } else {
                passedTrips.add(t);
            }
        }
        getSelectedTrips();
    }

    public static void getSelectedTrips() {
        selectedTrips = selectPassedTrips ? passedTrips : upcommingTrips;
    }

    public static void deleteTrip(Trip trip) {
        String tripID = String.valueOf(trip.getId());
        userDB_ref.child(tripID).removeValue();
    }

    public static void insertTrip(Trip trip) {
        Log.i(TAG, "inserting trip");

        int newId = getNewId();
        trip.setId(newId);
        String tripID = String.valueOf(trip.getId());
        userDB_ref.child(tripID).setValue(trip);

        Log.i(TAG, trip.getId() + " " + trip.getTitle());
    }

    public static void updateTrip(int trip_id, Trip trip) {
        trip.setId(trip_id);
        String tripID = String.valueOf(trip.getId());
        userDB_ref.child(tripID).setValue(trip);
        Log.i(TAG, "updating trip");
        Log.i(TAG, trip.getId() + " " + trip.getTitle());
    }

    private static int getNewId() {
        int min = 1000000;
        int max = 9999999;
        int randomNum = min + (int) (Math.random() * max);
        return randomNum;
    }
}
