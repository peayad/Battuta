package iti_edu.battuta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BattutaDBhelper extends SQLiteOpenHelper {

    private static final String TAG = "ptr-DBhelper";

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

    public boolean insertTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, trip.getTitle());
        cv.put(DATE_TIME, trip.getDateTime());
        cv.put(START_LOC, trip.getStartPoint());
        cv.put(END_LOC, trip.getEndPoint());
        cv.put(ONE_ROUND, trip.getIsRound());
        cv.put(DONE, trip.getIsDone());
        cv.put(NOTES, trip.getNotes());
        db.insert(TABLE_NAME, null, cv);

        Log.i(TAG, numberOfRows() + " insertTrip");
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public Integer updateTrip(Integer id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID + " = ? ", new String[]{Integer.toString(id)});
    }

   /* public void deleteTrip(String title) {
        SQLiteDatabase db = getWritableDatabase();
//        String selection = TITLE + " = ?";
//        String[] selectionArgs = {title};
//        db.delete(TABLE_NAME, selection, selectionArgs);
        ArrayList<String> myArray = getAllTrips();

        deleteTable();
        for (int i = 0;i<myArray.size();i++){
            if (myArray.get(i) == title) continue;
            insertTrip(myArray.get(i));
        }
    }
    */

    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery("select * from " + TABLE_NAME, null);
        cur.moveToPosition(0);
        while (cur.moveToNext()) {
            int index0 = cur.getColumnIndex(ID);
            int index1 = cur.getColumnIndex(TITLE);
            int index2 = cur.getColumnIndex(DATE_TIME);
            int index4 = cur.getColumnIndex(START_LOC);
            int index5 = cur.getColumnIndex(END_LOC);
            int index6 = cur.getColumnIndex(ONE_ROUND);
            int index7 = cur.getColumnIndex(DONE);
            int index8 = cur.getColumnIndex(NOTES);

            int id = cur.getInt(index0);
            String title = cur.getString(index1);
            String dateTime = cur.getString(index2);
            String start_loc = cur.getString(index4);
            String end_loc = cur.getString(index5);
            int one_round = cur.getInt(index6);
            int now_later = cur.getInt(index7);
            String notes = cur.getString(index8);

            Trip trip = new Trip(id, title, dateTime, start_loc, end_loc, one_round, now_later, notes);

            array_list.add(trip);
        }
        return array_list;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TRIP_TABLE);
        onCreate(db);
    }
}