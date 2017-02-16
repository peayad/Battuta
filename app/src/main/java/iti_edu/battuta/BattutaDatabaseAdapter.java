package iti_edu.battuta;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BattutaDatabaseAdapter {

    BattutaHelper helper;

    public BattutaDatabaseAdapter(Context context){
        helper = new BattutaHelper(context);
    }

    public long addTrip(Trip trip) {

        /*
        String insertTripQuery = "INSERT INTO T_TABLE_NAME(T_ID,U_ID,T_TITLLE,T_DATE,T_TIME,T_START_LOC,T_END_LOC,T_ONE_ROUND,T_NOW_LATER,T_NOTES) " +
                "VALUES (" + 0 + ","
                + 1 + ","
                + trip.getTitle() + ","
                + trip.getDate() + ","
                + trip.getTime() + ","
                + trip.getStartPoint() + ","
                + trip.getEndPoint() + ","
                + trip.getIsRound() + ","
                + trip.getIsDone() + ","
                + trip.getNotes() + ");";
        //db.execSQL(insertTripQuery);
        */

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BattutaHelper.T_ID,0);
        contentValues.put(BattutaHelper.U_ID,0);
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

        private static final String U_TABLE_NAME = "USERTABLE";
        private static final String U_ID = "_id";
        private static final String U_NAME = "Name";
        private static final String U_EMAIL = "Email";
        private static final String U_PASSWORD = "Password";
        String CREATE_USER_TABLE = "CREATE TABLE " + U_TABLE_NAME + "(" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + U_NAME + " VARCHAR(100), "
                + U_EMAIL + " VARCHAR(300) CONSTRAINT MailConst CHECK (Mail LIKE '%@%.com'), "
                + U_PASSWORD + " VARCHAR(100));";
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + U_TABLE_NAME + ";";


        private static final String T_TABLE_NAME = "TRIPTABLE";
        private static final String T_ID = "_id";
        private static final String T_TITLLE = "Title";
        private static final String T_DATE = "Date";
        private static final String T_TIME = "Time";
        private static final String T_START_LOC = "Start Location";
        private static final String T_END_LOC = "End Location";
        private static final String T_ONE_ROUND = "One/Round";
        private static final String T_NOW_LATER = "Now/Later";
        private static final String T_NOTES = "Notes";
        String CREATE_TRIP_TABLE = "CREATE TABLE " + T_TABLE_NAME + "(" + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + U_ID + " INTEGER CONSTRAINT FK_UID FOREIGN KEY REFERENCES USERTABLE(U_ID), "
                + T_TITLLE + " VARCHAR(100), "
                + T_DATE + " DATE, "
                + T_TIME + " TIME, "
                + T_START_LOC + " DECIMAL(20,20), "
                + T_END_LOC + " DECIMAL(20,20), "
                + T_ONE_ROUND + " BOOLEAN, "
                + T_NOW_LATER + " BOOLEAN, "
                + T_NOTES + " VARCHAR(MAX));";
        String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + T_TABLE_NAME + ";";


        private static final int DATABASE_VERSION = 1;
        Context context;

        public BattutaHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_USER_TABLE);
                db.execSQL(CREATE_TRIP_TABLE);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //OnUpgarde method is called when version number is changed
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_USER_TABLE);
                db.execSQL(DROP_TRIP_TABLE);
                onCreate(db);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
