package iti_edu.battuta;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BattutaDatabaseAdapter {

    BattutaHelper helper;

    public BattutaDatabaseAdapter(Context context){
        helper = new BattutaHelper(context);
    }

    public long addTrip(Trip trip) {


        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BattutaHelper.T_ID,0);
        contentValues.put(BattutaHelper.T_TITLLE,trip.getTitle());
        contentValues.put(BattutaHelper.T_DATE,trip.getDate());
        contentValues.put(BattutaHelper.T_TIME,trip.getTime());
        contentValues.put(BattutaHelper.T_START_LOC,trip.getStartPoint());
        contentValues.put(BattutaHelper.T_END_LOC,trip.getEndPoint());
        contentValues.put(BattutaHelper.T_ONE_ROUND,trip.getIsRound());
        contentValues.put(BattutaHelper.T_NOW_LATER,trip.getIsDone());
        contentValues.put(BattutaHelper.T_NOTES,trip.getNotes());

        //long return -1 if insertion failed
        long id = db.insert(BattutaHelper.T_TABLE_NAME, null, contentValues);

        return id;
    }


    static class BattutaHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "battuta.db";

        private static final String T_TABLE_NAME = "TRIPTABLE";
        private static final String T_ID = "_id";
        private static final String T_TITLLE = "Title";
        private static final String T_DATE = "Date";
        private static final String T_TIME = "Time";
        private static final String T_START_LOC = "StartLocation";
        private static final String T_END_LOC = "EndLocation";
        private static final String T_ONE_ROUND = "OneRound";
        private static final String T_NOW_LATER = "NowLater";
        private static final String T_NOTES = "Notes";
        String CREATE_TRIP_TABLE = "CREATE TABLE " + T_TABLE_NAME + "(" + T_ID + " INTEGER PRIMARY KEY, "
                + T_TITLLE + " VARCHAR(255), "
                //+ T_DATE + " DATE, "
                //+ T_TIME + " TIME, "
                //+ T_START_LOC + " DECIMAL(20,20), "
                //+ T_END_LOC + " DECIMAL(20,20), "
                + T_DATE + " VARCHAR(255), "
                + T_TIME + " VARCHAR(255), "
                + T_START_LOC + " VARCHAR(255), "
                + T_END_LOC + " VARCHAR(255), "
                + T_ONE_ROUND + " INTEGER, "
                + T_NOW_LATER + " INTEGER, "
                + T_NOTES + " VARCHAR(255));";
        String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + T_TABLE_NAME + ";";


        private static final int DATABASE_VERSION = 3;
        Context context;

        public BattutaHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            Log.i("hh", "onCreate: tmam");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i("hh", "onCreate: yestry");
                db.execSQL(CREATE_TRIP_TABLE);
            }
            catch (SQLException e) {
                e.printStackTrace();
                Log.i("hh", "onCreate: yescatch");
            }
        }

        //OnUpgarde method is called when version number is changed
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TRIP_TABLE);
                onCreate(db);
                Log.i("TAG", "onUpgrade: upgradded");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
