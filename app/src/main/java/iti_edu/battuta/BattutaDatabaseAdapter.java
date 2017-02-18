//package iti_edu.battuta;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//
//import java.util.ArrayList;
//
//public class BattutaDatabaseAdapter {
//
//    BattutaHelper helper;
//
//    public BattutaDatabaseAdapter(Context context){
//        helper = new BattutaHelper(context);
//    }
//
//    public long addTrip(Trip trip) {
//
//
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(BattutaHelper.ID,0);
//        contentValues.put(BattutaHelper.TITLLE,trip.getTitle());
//        contentValues.put(BattutaHelper.DATE,trip.getDate());
//        contentValues.put(BattutaHelper.TIME,trip.getTime());
//        contentValues.put(BattutaHelper.STARLOC,trip.getStartPoint());
//        contentValues.put(BattutaHelper.END_LOC,trip.getEndPoint());
//        contentValues.put(BattutaHelper.ONE_ROUND,trip.getIsRound());
//        contentValues.put(BattutaHelper.NOW_LATER,trip.getIsDone());
//        contentValues.put(BattutaHelper.NOTES,trip.getNotes());
//
//        //long return -1 if insertion failed
//        long id = db.insert(BattutaHelper.TABLE_NAME, null, contentValues);
//
//        return id;
//    }
//
//
//    static class BattutaHelper extends SQLiteOpenHelper {
//
//        private static final String DATABASE_NAME = "battuta.db";
//
//        private static final String TABLE_NAME = "TRIPS_TABLE";
//        private static final String ID = "_id";
//        private static final String TITLLE = "Title";
//        private static final String DATE = "Date";
//        private static final String TIME = "Time";
//        private static final String STARLOC = "StartLocation";
//        private static final String END_LOC = "EndLocation";
//        private static final String ONE_ROUND = "OneRound";
//        private static final String NOW_LATER = "NowLater";
//        private static final String NOTES = "Notes";
////        String CREATE_TRIP_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, "
////                + TITLLE + " VARCHAR(255), "
////                //+ DATE + " DATE, "
////                //+ TIME + " TIME, "
////                //+ STARLOC + " DECIMAL(20,20), "
////                //+ END_LOC + " DECIMAL(20,20), "
////                + DATE + " VARCHAR(255), "
////                + TIME + " VARCHAR(255), "
////                + STARLOC + " VARCHAR(255), "
////                + END_LOC + " VARCHAR(255), "
////                + ONE_ROUND + " INTEGER, "
////                + NOW_LATER + " INTEGER, "
////                + NOTES + " VARCHAR(255));";
//
//        String CREATE_TRIP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, "
//                + TITLLE + " VARCHAR);";
//
//        String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
//
//
//        private static final int DATABASE_VERSION = 3;
//        Context context;
//
//        public BattutaHelper(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//            this.context = context;
//            Log.i("hh", "onCreate: tmam");
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            try {
//                Log.i("hh", "onCreate: yestry");
//                db.execSQL(CREATE_TRIP_TABLE);
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//                Log.i("hh", "onCreate: yescatch");
//            }
//        }
//
//        //OnUpgarde method is called when version number is changed
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            try {
//                db.execSQL(DROP_TRIP_TABLE);
//                onCreate(db);
//                Log.i("TAG", "onUpgrade: upgraded");
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public boolean insertTrip (String title){
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put(TITLLE, title);
//            db.insert(TABLE_NAME, null, cv );
//            return true;
//        }
//
//        public Integer updateTrip (Integer id, String title){
//            SQLiteDatabase db = this.getWritableDatabase();
//            return db.delete(TABLE_NAME, ID + " = ? ", new String[] {Integer.toString(id)});
//        }
//
//        public ArrayList<String> getAllTrips(){
//            ArrayList<String> array_list = new ArrayList<>();
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cur = db.rawQuery( "select * from " + TABLE_NAME, null);
//
//            while(cur.isAfterLast() == false){
//                array_list.add(cur.getString(cur.getColumnIndex(TITLLE)));
//                cur.moveToLast();
//            }
////            cur.close();
//            return  array_list;
//        }
//
//    }
//}
