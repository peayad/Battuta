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

    String CREATE_TRIP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT);";

    String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    private static final int DATABASE_VERSION = 1;
    Context context;

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

    public boolean insertTrip(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        db.insert(TABLE_NAME, null, cv);

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

    public void deleteTrip(String title) {
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

    public ArrayList<String> getAllTrips() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery("select * from " + TABLE_NAME, null);
        cur.moveToPosition(0);
        while (cur.moveToNext()) {
            int currentIndex = cur.getColumnIndex(TITLE);
            String currentString = cur.getString(currentIndex);
            array_list.add(currentString);

            Log.i(TAG, currentString);
        }

        return array_list;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TRIP_TABLE);
        onCreate(db);
    }
}
