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

public class HelperDBAmnesties {
    String TAG = "DBAdapter";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 43;
    private static final String DB_NAME = "water_billing_app_database";
    public static final String tablename = "ws_amnesty";

    //Classifications fields
    public static final String id = "id";
    public static final String ordinance_name = "ordinance_name";
    public static final String bill_month = "bill_month";
    public static final String bill_year = "bill_year";
    public static final String amnesty_desc = "amnesty_desc";

    public HelperDBAmnesties(Context context) {
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

    public ArrayList<HashMap<String, String>> getAmnesties() {
        ArrayList<HashMap<String, String>> amnesties;
        amnesties = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + tablename;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(ordinance_name, cursor.getString(1));
                map.put(bill_month, cursor.getString(2));
                map.put(bill_year, cursor.getString(3));
                map.put(amnesty_desc, cursor.getString(4));
                amnesties.add(map);
            } while (cursor.moveToNext());
        }

        return amnesties;
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
                    + ordinance_name + " TEXT ," + bill_month + " TEXT ," + bill_year + " TEXT ,"
                    + amnesty_desc + " TEXT ,"
                    + " TEXT " + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + tablename);
        }
    }
}