package com.westframework.waterbillingapp.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class HelperDBClassifications {
    String TAG = "DBAdapter";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 43;
    private static final String DB_NAME = "water_billing_app_database";
    public static final String tablename = "ws_classification_tbl";

    //Classifications fields
    public static final String id = "id";
    public static final String class_cat = "class_cat";
    public static final String cu_m_min = "cu_m_min";
    public static final String cu_m_max = "cu_m_max";
    public static final String amount = "amount";
    public static final String increment_by = "increment_by";
    public static final String updated_at = "updated_at";
    public static final String created_at = "created_at";

    public HelperDBClassifications(Context context) {
        dbHelper = new Database(context);
    }

    public void open() {
        if (null == db || !db.isOpen()) {
            try {
                db = dbHelper.getWritableDatabase();
            } catch (SQLiteException sqLiteException) {
            }
        }
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public int insert(String table, ContentValues values) {
        try {
            db = dbHelper.getWritableDatabase();
            int y = (int) db.insert(table, null, values);
            db.close();
            Log.e("Data Inserted", "Data Inserted");
            Log.e("y", y + "");
            return y;
        } catch (Exception ex) {
            Log.e("Error Insert", ex.getMessage().toString());
            return 0;
        }
    }

    public void delete() {
        db.execSQL("delete from " + tablename);
    }

    public Cursor getAllRow(String table) {
        return db.query(table, null, null, null, null, null, id);
    }


    public ArrayList<HashMap<String, String>> getClassifications() {
        ArrayList<HashMap<String, String>> classifications;
        classifications = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + tablename;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(class_cat, cursor.getString(1));
                map.put(cu_m_min, cursor.getString(2));
                map.put(cu_m_max, cursor.getString(3));
                map.put(amount, cursor.getString(4));
                map.put(increment_by, cursor.getString(5));
                map.put(updated_at, cursor.getString(6));
                map.put(created_at, cursor.getString(7));
                classifications.add(map);
            } while (cursor.moveToNext());
        }

        return classifications;
    }

    private class Database extends SQLiteOpenHelper {
        private static final int VERSION = 43;
        private static final String DB_NAME = "water_billing_app_database";

        public Database(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_sql = "CREATE TABLE IF NOT EXISTS " + tablename + "("
                    + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + class_cat + " TEXT ," + cu_m_min + " TEXT ," + cu_m_max + " TEXT ,"
                    + amount + " TEXT ," + increment_by + " TEXT ,"
                    + updated_at + " TEXT ," + created_at + " TEXT ,"
                    + " TEXT " + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + tablename);
        }
    }
}