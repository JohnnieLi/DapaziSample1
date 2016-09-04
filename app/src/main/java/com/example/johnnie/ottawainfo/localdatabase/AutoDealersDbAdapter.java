package com.example.johnnie.ottawainfo.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.johnnie.ottawainfo.model.DealerModel;

import java.util.ArrayList;
import java.util.List;

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
    public static final String KEY_IMG = "imageUri";

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
                    KEY_IMG + "," +
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

    public long createDealer(DealerModel model) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ADDRESS, model.getAddress());
        initialValues.put(KEY_NAME, model.getName());
        initialValues.put(KEY_INFORMATION, model.getInformation());
        initialValues.put(KEY_CATEGORY, model.getCategory());
        initialValues.put(KEY_FQ,model.getFAQ());
        initialValues.put(KEY_IMG,model.getImageUri());
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }


    public boolean deleteAllDealers() {

        int doneDelete;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

//    public Cursor fetchDealersByName(String inputText) throws SQLException {
//        Log.w(TAG, inputText);
//        Cursor mCursor = null;
//        if (inputText == null  ||  inputText.length () == 0)  {
//            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
//                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ},
//                    null, null, null, null, null);
//
//        }
//        else {
//            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
//                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ},
//                    KEY_NAME + " like '%" + inputText + "%'", null,
//                    null, null, null, null);
//        }
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//
//    }

    public List<DealerModel> fetchDealersByName(String inputText) throws SQLException {
        List<DealerModel> models = new ArrayList<>();
        Log.w(TAG, inputText);
        Cursor mCursor;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ,KEY_IMG},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ,KEY_IMG},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor.getCount() > 0) {
            while(mCursor.moveToNext()){
                DealerModel model = new DealerModel();
                model.setAddress(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ADDRESS)));
                model.setCategory(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                model.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_NAME)));
                model.setInformation(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_INFORMATION)));
                model.setFAQ(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_FQ)));
                model.setDealId(mCursor.getLong(mCursor.getColumnIndexOrThrow(KEY_ROWID)));
                model.setImageUri(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_IMG)));
                models.add(model);
            }
        }
        return models;

    }

    public List<DealerModel> fetchAllDealers() {
        List<DealerModel> models = new ArrayList<>();

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CATEGORY,KEY_ADDRESS, KEY_NAME, KEY_INFORMATION,KEY_FQ,KEY_IMG},
                null, null, null, null, null);
        Log.i(TAG,"Returned "+mCursor.getCount()+ " rows");

        if (mCursor.getCount() > 0) {
            while(mCursor.moveToNext()){
                DealerModel model = new DealerModel();
                model.setAddress(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ADDRESS)));
                model.setCategory(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                model.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_NAME)));
                model.setInformation(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_INFORMATION)));
                model.setFAQ(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_FQ)));
                model.setDealId(mCursor.getLong(mCursor.getColumnIndexOrThrow(KEY_ROWID)));
                model.setImageUri(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_IMG)));
                models.add(model);
            }
        }
        return models;
    }


}
