package iti_edu.battuta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.database.DatabaseUtils.queryNumEntries;

class BattutaDBadapter {

    private static final String TAG = "ptr-DBadapter";
    BattutaDBhelper helper;

    BattutaDBadapter(Context context) {
        helper = new BattutaDBhelper(context);
    }

    boolean insertTrip(Trip trip) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BattutaDBhelper.TITLE, trip.getTitle());
        cv.put(BattutaDBhelper.DATE_TIME, trip.getDateTime());
        cv.put(BattutaDBhelper.START_LOC, trip.getStartPoint());
        cv.put(BattutaDBhelper.END_LOC, trip.getEndPoint());
        cv.put(BattutaDBhelper.ONE_ROUND, trip.getIsRound());
        cv.put(BattutaDBhelper.DONE, trip.getIsDone());
        cv.put(BattutaDBhelper.NOTES, trip.getNotes());

        db.insert(BattutaDBhelper.TABLE_NAME, null, cv);
        db.close();
        Log.i(TAG, numberOfRows() + " insertTrip");
        return true;
    }

    private int numberOfRows() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return (int) queryNumEntries(db, BattutaDBhelper.TABLE_NAME);
    }

    int getTripID(Trip trip) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + BattutaDBhelper.TABLE_NAME + " WHERE Title = ?", new String[]{trip.getTitle()});
        while (cur.moveToNext()) {
            int indexID = cur.getColumnIndex(BattutaDBhelper.ID);
            int tripID = cur.getInt(indexID);
            return tripID;
        }
        cur.close();
        return -1;
    }

    void updateTrip(int id, Trip newTrip) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(BattutaDBhelper.TITLE, newTrip.getTitle());
        cv.put(BattutaDBhelper.DATE_TIME, newTrip.getDateTime());
        cv.put(BattutaDBhelper.START_LOC, newTrip.getStartPoint());
        cv.put(BattutaDBhelper.END_LOC, newTrip.getEndPoint());
        cv.put(BattutaDBhelper.ONE_ROUND, newTrip.getIsRound());
        cv.put(BattutaDBhelper.DONE, newTrip.getIsDone());
        cv.put(BattutaDBhelper.NOTES, newTrip.getNotes());

        db.update(BattutaDBhelper.TABLE_NAME, cv, BattutaDBhelper.ID + " = ? ", new String[]{Integer.toString(id)});
    }

    void deleteTrip(Trip trip) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] selectionArgs = {Integer.toString(trip.getId())};
        db.delete(BattutaDBhelper.TABLE_NAME, BattutaDBhelper.ID + " = ?", selectionArgs);
    }


    ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> array_list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT * FROM " + BattutaDBhelper.TABLE_NAME, null);
        Log.i(TAG, "id starting");
        while (cur.moveToNext()) {
            int index0 = cur.getColumnIndex(BattutaDBhelper.ID);
            int index1 = cur.getColumnIndex(BattutaDBhelper.TITLE);
            int index2 = cur.getColumnIndex(BattutaDBhelper.DATE_TIME);
            int index4 = cur.getColumnIndex(BattutaDBhelper.START_LOC);
            int index5 = cur.getColumnIndex(BattutaDBhelper.END_LOC);
            int index6 = cur.getColumnIndex(BattutaDBhelper.ONE_ROUND);
            int index7 = cur.getColumnIndex(BattutaDBhelper.DONE);
            int index8 = cur.getColumnIndex(BattutaDBhelper.NOTES);

            int id = cur.getInt(index0);
            Log.i(TAG, "id : " + id);
            String title = cur.getString(index1);
            String dateTime = cur.getString(index2);
            String start_loc = cur.getString(index4);
            String end_loc = cur.getString(index5);
            int one_round = cur.getInt(index6);
            int now_later = cur.getInt(index7);
            String notes = cur.getString(index8);

            Trip trip = new Trip(id, title, start_loc, end_loc, dateTime, one_round, now_later, notes);

            array_list.add(trip);
        }
        cur.close();

        Log.i(TAG, "id ended");
        return array_list;
    }

    void deleteTable() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(helper.DROP_TRIP_TABLE);
        helper.onCreate(db);
    }

    private static class BattutaDBhelper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "battuta.db";
        private static final String TABLE_NAME = "TRIPS_TABLE";
        private static final String ID = "_id";
        private static final String TITLE = "Title";
        private static final String DATE_TIME = "DateTime";
        private static final String START_LOC = "StartLocation";
        private static final String END_LOC = "EndLocation";
        private static final String ONE_ROUND = "OneRound";
        private static final String DONE = "Done";
        private static final String NOTES = "Notes";


        String CREATE_TRIP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT, "
                + DATE_TIME + " TEXT, "
                + START_LOC + " TEXT, "
                + END_LOC + " TEXT, "
                + ONE_ROUND + " INTEGER, "
                + DONE + " INTEGER, "
                + NOTES + " TEXT);";

        String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


        private static final int DATABASE_VERSION = 1;


        BattutaDBhelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TRIP_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TRIP_TABLE);
            onCreate(db);
        }


    }
}