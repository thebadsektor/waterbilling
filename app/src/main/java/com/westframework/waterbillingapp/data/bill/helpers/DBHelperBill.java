package com.westframework.waterbillingapp.data.bill.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelperBill {
    String TAG = "DBAdapter";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 43;
    private static final String DB_NAME = "water_billing_app_database";
    public static final String water_bill_table = "ws_water_bill";
    public static final String holidays_table = "ws_holidays";
    public static final String amnesties_table = "ws_amnesty";
    public static final String classifications_table = "ws_classification_tbl";

    //Water Bills fields
    public static final String id = "id";// 0 integer
    public static final String application_no = "application_no";// 1 text(String)
    public static final String read_date = "read_date";
    public static final String due_date = "due_date";
    public static final String full_name = "full_name";
    public static final String bill_address = "bill_address";
    public static final String classification = "classification";
    public static final String meter_read = "meter_read";
    public static final String senior_ids = "senior_ids";
    public static final String reading_month = "reading_month";
    public static final String pre_reading = "pre_reading";
    public static final String actual_reading = "actual_reading";
    public static final String cu_m_used = "cu_m_used";
    public static final String discount = "discount";
    public static final String penalty = "penalty";
    public static final String discount_amount = "discount_amount";
    public static final String bill_amount = "bill_amount";
    public static final String status = "status";
    public static final String or_num = "or_num";
    public static final String or_amount = "or_amount";
    public static final String or_date = "or_date";
    public static final String bill_cat = "bill_cat";
    public static final String bill_status = "bill_status";
    public static final String updated_at = "updated_at";
    public static final String created_at = "created_at";

    //Holidays fields
    public static final String holiday_id = "id";// 0 integer
    public static final String holiday_name = "holiday_name";// 0 integer
    public static final String holiday_month = "holiday_month";// 0 integer
    public static final String holiday_date = "holiday_date";// 0 integer
    public static final String holiday_year = "holiday_year";// 0 integer
    public static final String holiday_updated_at = "updated_at";
    public static final String holiday_created_at = "created_at";

    //Amnesty fields
    public static final String amnesty_id = "id";
    public static final String ordinance_name = "ordinance_name";
    public static final String bill_month = "bill_month";
    public static final String amnesty_desc = "amnesty_desc";
    public static final String bill_year = "bill_year";
    public static final String amnesty_updated_at = "updated_at";
    public static final String amnesty_created_at = "created_at";

    //Classifications fields
    public static final String class_id = "id";
    public static final String class_cat = "class_cat";
    public static final String cu_m_min = "cu_m_min";
    public static final String cu_m_max = "cu_m_max";
    public static final String amount = "amount";
    public static final String increment_by = "increment_by";
    public static final String class_updated_at = "class_updated_at";
    public static final String class_created_at = "class_created_at";

    public DBHelperBill(Context context) {
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
        db.execSQL("delete from " + water_bill_table);
    }

    public Cursor getAllRow(String table) {
        return db.query(table, null, null, null, null, null, id);
    }

    public ArrayList<HashMap<String, String>> getAllWaterBills() {
        ArrayList<HashMap<String, String>> waterBills;
        waterBills = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + water_bill_table;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(application_no, cursor.getString(1));
                map.put(read_date, cursor.getString(2));
                map.put(due_date, cursor.getString(3));
                map.put(full_name, cursor.getString(4));
                map.put(bill_address, cursor.getString(5));
                map.put(classification, cursor.getString(6));
                map.put(meter_read, cursor.getString(7));
                map.put(senior_ids, cursor.getString(8));
                map.put(reading_month, cursor.getString(9));
                map.put(pre_reading, cursor.getString(10));
                map.put(actual_reading, cursor.getString(11));
                map.put(cu_m_used, cursor.getString(12));
                map.put(discount, cursor.getString(13));
                map.put(penalty, cursor.getString(14));
                map.put(discount_amount, cursor.getString(15));
                map.put(bill_amount, cursor.getString(16));
                map.put(status, cursor.getString(17));
                map.put(or_num, cursor.getString(18));
                map.put(or_amount, cursor.getString(19));
                map.put(or_date, cursor.getString(20));
                map.put(bill_cat, cursor.getString(21));
                map.put(bill_status, cursor.getString(22));
                map.put(updated_at, cursor.getString(23));
                map.put(created_at, cursor.getString(24));
                waterBills.add(map);
            } while (cursor.moveToNext());
        }
        return waterBills;
    }

    public int billMonthExists(String application_no, String reading_month){
        String countQuery = "SELECT * FROM " + water_bill_table + " WHERE application_no = '" + application_no + "' AND reading_month = '" + reading_month + "' ORDER BY id ASC";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<HashMap<String, String>> getUnpaidByAppNum(String appNum) {
        ArrayList<HashMap<String, String>> unpaidBills;
        unpaidBills = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + water_bill_table + " WHERE application_no = '" + appNum + "' AND (status = 'Unpaid' OR status = 'unpaid') AND (bill_amount != '0' AND bill_amount != '') ORDER BY id ASC LIMIT 9";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(id, cursor.getString(0));
                map.put(application_no, cursor.getString(1));
                map.put(read_date, cursor.getString(2));
                map.put(due_date, cursor.getString(3));
                map.put(full_name, cursor.getString(4));
                map.put(bill_address, cursor.getString(5));
                map.put(classification, cursor.getString(6));
                map.put(meter_read, cursor.getString(7));
                map.put(senior_ids, cursor.getString(8));
                map.put(reading_month, cursor.getString(9));
                map.put(pre_reading, cursor.getString(10));
                map.put(actual_reading, cursor.getString(11));
                map.put(cu_m_used, cursor.getString(12));
                map.put(discount, cursor.getString(13));
                map.put(penalty, cursor.getString(14));
                map.put(discount_amount, cursor.getString(15));
                map.put(bill_amount, cursor.getString(16));
                map.put(status, cursor.getString(17));
                map.put(or_num, cursor.getString(18));
                map.put(or_amount, cursor.getString(19));
                map.put(or_date, cursor.getString(20));
                map.put(bill_cat, cursor.getString(21));
                map.put(bill_status, cursor.getString(22));
                map.put(updated_at, cursor.getString(23));
                map.put(created_at, cursor.getString(24));
                unpaidBills.add(map);
            } while (cursor.moveToNext());
        }
        return unpaidBills;
    }

    public ArrayList<HashMap<String, String>> getHolidays() {
        ArrayList<HashMap<String, String>> holidays;
        holidays = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT holiday_month,holiday_date,holiday_year FROM " + holidays_table;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(holiday_month, cursor.getString(0));
                map.put(holiday_date, cursor.getString(1));
                map.put(holiday_year, cursor.getString(2));
                holidays.add(map);
            } while (cursor.moveToNext());
        }
        return holidays;
    }

    public ArrayList<HashMap<String, String>> getAllAmnesties() {
        ArrayList<HashMap<String, String>> amnesties;
        amnesties = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + amnesties_table + " ORDER BY id ASC";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(amnesty_id, cursor.getString(0));
                map.put(ordinance_name, cursor.getString(1));
                map.put(bill_month, cursor.getString(2));
                map.put(amnesty_desc, cursor.getString(3));
                map.put(bill_year, cursor.getString(4));
                map.put(amnesty_updated_at, cursor.getString(5));
                map.put(amnesty_created_at, cursor.getString(6));
                amnesties.add(map);
            } while (cursor.moveToNext());
        }
        return amnesties;
    }

    public int getPreviousReading(String application_no){
        int preRead = 0;
        String selectQuery = "SELECT actual_reading FROM " + water_bill_table + " WHERE application_no = '" + application_no + "' ORDER BY id DESC LIMIT 1";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                preRead = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return preRead;
    }

    public int getLastBillId(){
        int id = 0;

        String selectQuery = "SELECT seq FROM sqlite_sequence WHERE name = '" + water_bill_table + "'";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return id;
    }

    public ArrayList<HashMap<String, String>> getClassifications(String appType) {
        ArrayList<HashMap<String, String>> classifications;
        classifications = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + classifications_table + " WHERE class_cat = '" + appType + "'ORDER BY cu_m_min ASC";
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
                map.put(class_updated_at, cursor.getString(6));
                map.put(class_created_at, cursor.getString(6));
                classifications.add(map);
            } while (cursor.moveToNext());
        }

        return classifications;
    }

    public ArrayList<HashMap<String, String>> getClass(String appType, double used) {
        ArrayList<HashMap<String, String>> classifications;
        classifications = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + classifications_table + " WHERE class_cat = '" + appType + "' and ( '" + used + "'  >= cu_m_min and '" + used + "' <= cu_m_max ) ORDER BY cu_m_min ASC LIMIT 1";
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
                map.put(class_updated_at, cursor.getString(6));
                map.put(class_created_at, cursor.getString(6));
                classifications.add(map);
            } while (cursor.moveToNext());
        }

        return classifications;
    }

    public ArrayList<HashMap<String, String>> getAmnestiesByReadingMonth(String reading_month) {
        ArrayList<HashMap<String, String>> amnesties;
        amnesties = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + amnesties_table + " WHERE (bill_month || ' ' || bill_year) = '" + reading_month + "' ORDER BY id ASC";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(amnesty_id, cursor.getString(0));
                map.put(ordinance_name, cursor.getString(1));
                map.put(bill_month, cursor.getString(2));
                map.put(amnesty_desc, cursor.getString(3));
                map.put(bill_year, cursor.getString(4));
                map.put(amnesty_updated_at, cursor.getString(5));
                map.put(amnesty_created_at, cursor.getString(6));
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
            String create_sql = "CREATE TABLE IF NOT EXISTS " + water_bill_table + "("
                    + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + application_no + " TEXT ," + read_date + " TEXT ," + due_date + " TEXT ,"
                    + full_name + " TEXT ," + bill_address + " TEXT ," + classification + " TEXT ,"
                    + meter_read + " TEXT ," + senior_ids + " TEXT ," + reading_month + " TEXT ,"
                    + pre_reading + " TEXT ," + actual_reading + " TEXT ," + cu_m_used + " TEXT ,"
                    + discount + " TEXT ," + penalty + " TEXT ," + discount_amount + " TEXT ,"
                    + bill_amount + " TEXT ," + status + " TEXT ," + or_num + " TEXT ,"
                    + or_amount + " TEXT ," + or_date + " TEXT ," + bill_cat + " TEXT ,"
                    + bill_status + " TEXT ," + updated_at + " TEXT ," + created_at + " TEXT ,"
                    + " TEXT " + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + water_bill_table);
        }

    }
}