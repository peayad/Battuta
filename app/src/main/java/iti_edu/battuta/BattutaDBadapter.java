package iti_edu.battuta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BattutaDBadapter {

    private static final String TAG = "ptr-DBadapter";
    BattutaDBhelper helper;

    BattutaDBadapter(Context context){
        helper = new BattutaDBhelper(context);
    }

    public boolean insertTrip(Trip trip) {
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

    public int numberOfRows() {
        SQLiteDatabase db = helper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, helper.TABLE_NAME);
        return numRows;
    }

    public Integer updateTrip(Integer id, String title) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(helper.TABLE_NAME, helper.ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public void deleteTrip(Trip trip) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] selectionArgs = {Integer.toString(trip.getId())};
        db.delete(helper.TABLE_NAME, helper.ID + " = ?", selectionArgs);
    }


    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> array_list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME, null);
//        cur.moveToPosition(0);
        while (cur.moveToNext()) {
            int index0 = cur.getColumnIndex(helper.ID);
            int index1 = cur.getColumnIndex(helper.TITLE);
            int index2 = cur.getColumnIndex(helper.DATE_TIME);
            int index4 = cur.getColumnIndex(helper.START_LOC);
            int index5 = cur.getColumnIndex(helper.END_LOC);
            int index6 = cur.getColumnIndex(helper.ONE_ROUND);
            int index7 = cur.getColumnIndex(helper.DONE);
            int index8 = cur.getColumnIndex(helper.NOTES);

            int id = cur.getInt(index0);
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
        return array_list;
    }

    public void deleteTable() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(helper.DROP_TRIP_TABLE);
        helper.onCreate(db);
    }

    private static class BattutaDBhelper extends SQLiteOpenHelper{


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


        public BattutaDBhelper(Context context) {
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