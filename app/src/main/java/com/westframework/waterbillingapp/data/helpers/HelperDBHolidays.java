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

public class HelperDBHolidays {
    String TAG = "DBAdapter";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 43;
    private static final String DB_NAME = "water_billing_app_database";
    public static final String tablename = "ws_holidays";

    //Classifications fields
    public static final String id = "id";
    public static final String holiday_name = "holiday_name";
    public static final String holiday_month = "holiday_month";
    public static final String holiday_date = "holiday_date";
    public static final String holiday_year = "holiday_year";

    public HelperDBHolidays(Context context) {
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

    public ArrayList<HashMap<String, String>> getHolidays() {
        ArrayList<HashMap<String, String>> holidays;
        holidays = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + tablename;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(holiday_name, cursor.getString(1));
                map.put(holiday_month, cursor.getString(2));
                map.put(holiday_date, cursor.getString(3));
                map.put(holiday_year, cursor.getString(4));
                holidays.add(map);
            } while (cursor.moveToNext());
        }
        return holidays;
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
                    + holiday_name + " TEXT ," + holiday_month + " TEXT ," + holiday_date + " TEXT ,"
                    + holiday_year + " TEXT ,"
                    + " TEXT " + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + tablename);
        }
    }
}