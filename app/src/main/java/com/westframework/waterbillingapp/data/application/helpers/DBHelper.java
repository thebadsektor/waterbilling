package com.westframework.waterbillingapp.data.application.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBHelper {
    String TAG = "DBAdapter";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 43;
    private static final String DB_NAME = "water_billing_app_database";
    public static final String Tablename = "ws_application_tbl";

    //Application Fields
    public static final String id = "id";// 0 integer
    public static final String application_no = "application_no";// 1 text(String)
    public static final String application_date = "application_date";// 1 text(String)
    public static final String lname = "lname";// 2 integer
    public static final String mname = "mname";// 2 integer
    public static final String fname = "fname";// 2 integer
    public static final String consumer_type = "consumer_type";// 2 integer
    public static final String house_no = "house_no";// 2 integer
    public static final String building_no = "building_no";// 2 integer
    public static final String street = "street";// 2 integer
    public static final String barangay = "barangay";// 2 integer
    public static final String ctc_no = "ctc_no";// 2 integer
    public static final String or_status = "or_status";// 2 integer
    public static final String status = "status";// 2 integer

    public DBHelper(Context context) {
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
        db.execSQL("delete from " + Tablename);
    }

    public Cursor getAllRow(String table) {
        return db.query(table, null, null, null, null, null, id);
    }

    public ArrayList<HashMap<String, String>> getAllApplications() {
        ArrayList<HashMap<String, String>> waterBills;
        waterBills = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + Tablename;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(application_no, cursor.getString(1));
                map.put(application_date, cursor.getString(2));
                map.put(lname, cursor.getString(3));
                map.put(mname, cursor.getString(4));
                map.put(fname, cursor.getString(5));
                map.put(consumer_type, cursor.getString(6));
                map.put(house_no, cursor.getString(7));
                map.put(building_no, cursor.getString(8));
                map.put(street, cursor.getString(9));
                map.put(barangay, cursor.getString(10));
                map.put(ctc_no, cursor.getString(11));
                map.put(or_status, cursor.getString(12));
                map.put(status, cursor.getString(13));
                waterBills.add(map);
            } while (cursor.moveToNext());
        }
        return waterBills;
    }

    private class Database extends SQLiteOpenHelper {
        private static final int VERSION = 43;
        private static final String DB_NAME = "water_billing_app_database";

        public Database(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_sql = "CREATE TABLE IF NOT EXISTS " + Tablename + "("
                    + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + application_no + " TEXT ," + application_date + " TEXT ," + lname + " TEXT ,"
                    +  mname + " TEXT ," + fname + " TEXT ,"
                    + consumer_type + " TEXT ,"
                    + house_no + " TEXT ," + building_no + " TEXT ," + street + " TEXT ," + barangay
                    + ctc_no + " TEXT ,"
                    + or_status + " TEXT ," + status + " TEXT ,"
                    + " TEXT " + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Tablename);
        }

    }
}
