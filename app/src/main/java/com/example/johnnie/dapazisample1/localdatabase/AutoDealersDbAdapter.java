package com.example.johnnie.dapazisample1.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Johnnie on 2016-08-14.
 */
public class AutoDealersDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_INFORMATION = "information";
    public static final String KEY_FQ = "FQ";

    private static final String TAG = "AutoDealersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Dapazi";
    private static final String SQLITE_TABLE = "AutoDealers";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CATEGORY + "," +
                    KEY_NAME + "," +
                    KEY_ADDRESS + "," +
                    KEY_INFORMATION + "," +
                    KEY_FQ + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public AutoDealersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public AutoDealersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createDealer(String category, String name,
                              String address, String information, String fQ) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_INFORMATION, information);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_FQ,fQ);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllDealers() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchDealersByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllDealers() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public  void insertSomeDealers() {
        //KEY_CATEGORY,  KEY_NAME,   KEY_ADDRESS,   KEY_INFORMATION,   KEY_FQ

        createDealer("dealer", "bmw", "835 Carling Ave,Ottawa", "Otto's BMW", "no question");
        createDealer("dealer", "bmw", "1040 Ogilvie Rd, Gloucester, ON K1J 7T3", "Elite BMW","844-267-4826");

        createDealer("dealer", "audi", "295 West Hunt Club Rd,Ottawa",
                "Mark Motors of Ottawa", "(613) 723-1221");
        createDealer("dealer", "audi", "458 Montreal Rd, Ottawa",
                "Mark Motors of Ottawa", "(613) 746-3533");
        createDealer("dealer", "audi", "611 Montreal Rd, Ottawa",
                "Mark Motors of Ottawa", "(613) 749-4275");

        createDealer("dealer", "mercedes", "400 Hunt Club Rd, ottawa",
                "Star Motors of Ottawa", "(613) 737-7827");
//        createDealer("dealer", "mercedes", "1110 St Laurent Blvd, Ottawa, ON K1K 4L8",
//                "Ogilvie Motors Ltd | Mercedes-Benz Ogilvie Ottawa","(613) 745-9000");
        createDealer("dealer", "mercedes", "1339 Boulevard la Vérendrye O, Gatineau, QC J8T 8K2",
                "Mercedes-Benz Gatineau","(613) 745-9000");

    }

}
