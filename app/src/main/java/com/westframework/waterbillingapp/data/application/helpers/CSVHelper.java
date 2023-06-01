package com.westframework.waterbillingapp.data.application.helpers;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVHelper {

    public static final String Tablename = "MyTable1";
    public static final String id = "id";// 0 integer
    public static final String application_no = "application_no";// 1 text(String)
    public static final String application_date = "application_date";// 1 text(String)
    public static final String lname = "lname";// 2 text
    public static final String mname = "mname";// 4 integer
    public static final String fname = "fname";// 3 text
    public static final String consumer_type = "consumer_type";// 1 text(String)
    public static final String house_no = "house_no";// 3 text
    public static final String building_no = "building_no";// 3 text
    public static final String street = "street";// 3 text
    public static final String barangay = "barangay";// 3 text
    public static final String water_meter_num = "water_meter_num";// 2 text
    public static final String or_status = "or_status";// 2 text
    public static final String status = "status";// 2 text

    public static class InsertAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public DBHelper dbAdapter;
        public FileReader file;
        public InsertListener insertListener;

        public InsertAsyncTask(DBHelper a, FileReader b, InsertListener insertListener){
            dbAdapter = a;
            file = b;
            this.insertListener = insertListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            BufferedReader buffer = new BufferedReader(file);
            String line = "";
            try {
                buffer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true){
                try {
                    if (!((line = buffer.readLine()) != null)) break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.e("line", line);
                String[] str = line.split(",", 13);

                ContentValues contentValues = new ContentValues();
                contentValues.put(application_no, str[0].toString());
                contentValues.put(application_date, str[1].toString());
                contentValues.put(lname, str[2].toString());
                contentValues.put(mname, str[3].toString());
                contentValues.put(fname, str[4].toString());
                contentValues.put(consumer_type, str[5].toString());
                contentValues.put(house_no, str[6].toString());
                contentValues.put(building_no, str[7].toString());
                contentValues.put(street, str[8].toString());
                contentValues.put(barangay, str[9].toString());
                contentValues.put(water_meter_num, str[10].toString());
                contentValues.put(or_status, str[11].toString());
                contentValues.put(status, str[12].toString());

                try {
                    if (dbAdapter.insert("ws_application_tbl", contentValues) < 0) {
                        //return null;
                    }
                } catch (Exception ex) {
                    Log.d("Exception in importing", ex.getMessage().toString());
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            insertListener.onInsertPerformed(result);
        }
    }

    public static interface InsertListener{
        public void onInsertPerformed(Boolean result);
    }

    public static class ExportAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public DBHelper dbAdapter;
        public CSVHelper.ExportListener exportListener;
        public File file;

        public ExportAsyncTask(DBHelper a, File b, CSVHelper.ExportListener exportListener){
            dbAdapter = a;
            file = b;
            this.exportListener = exportListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<HashMap<String, String>> applications = dbAdapter.getAllApplications();

            List<String> headers = applications.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());

            try (FileWriter writer = new FileWriter(file, true)) {

                List<String> hList = new ArrayList<String>();
                String h1 = "", h2 = "", h3 = "", h4 = "", h5 = "",
                        h6 = "", h7 = "", h8 = "", h9 = "", h10 = "",
                        h11 = "", h12 = "", h13 = "";
                String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "",
                        v6 = "", v7 = "", v8 = "", v9 = "", v10 = "",
                        v11 = "", v12 = "", v13 = "";
                for (String string : headers) {
                    if("application_no".equals(string)) h1 = string;
                    if("application_date".equals(string)) h2 = string;
                    if("lname".equals(string)) h3 = string;
                    if("mname".equals(string)) h4 = string;
                    if("fname".equals(string)) h5 = string;
                    if("consumer_type".equals(string)) h6 = string;
                    if("house_no".equals(string)) h7= string;
                    if("building_no".equals(string)) h8 = string;
                    if("street".equals(string)) h9 = string;
                    if("barangay".equals(string)) h10 = string;
                    if("water_meter_num".equals(string)) h11 = string;
                    if("or_status".equals(string)) h12 = string;
                    if("status".equals(string)) h13 = string;
                }
                hList.add(h1.toString());
                hList.add(h2.toString());
                hList.add(h3.toString());
                hList.add(h4.toString());
                hList.add(h5.toString());
                hList.add(h6.toString());
                hList.add(h7.toString());
                hList.add(h8.toString());
                hList.add(h9.toString());
                hList.add(h10.toString());
                hList.add(h11.toString());
                hList.add(h12.toString());
                hList.add(h13.toString());
                writer.write(hList.toString().replace("[", "").replace("]", ""));
                writer.write("\r\n");
                List<String> vList;
                for (HashMap<String, String> map : applications){
                    vList = new ArrayList<String>();
                    for (Map.Entry<String, String> entry : map.entrySet()){
                        if("application_no".equals(entry.getKey())) v1 = entry.getValue();
                        if("application_date".equals(entry.getKey())) v2= entry.getValue();
                        if("lname".equals(entry.getKey())) v3= entry.getValue();
                        if("mname".equals(entry.getKey())) v4= entry.getValue();
                        if("fname".equals(entry.getKey())) v5= entry.getValue();
                        if("consumer_type".equals(entry.getKey())) v6= entry.getValue();
                        if("house_no".equals(entry.getKey())) v7= entry.getValue();
                        if("building_no".equals(entry.getKey())) v8= entry.getValue();
                        if("street".equals(entry.getKey())) v9= entry.getValue();
                        if("barangay".equals(entry.getKey())) v10 = entry.getValue();
                        if("water_meter_num".equals(entry.getKey())) v11= entry.getValue();
                        if("or_status".equals(entry.getKey())) v12= entry.getValue();
                        if("status".equals(entry.getKey())) v13= entry.getValue();
                    }
                    vList.add(v1.toString());
                    vList.add(v2.toString());
                    vList.add(v3.toString());
                    vList.add(v4.toString());
                    vList.add(v5.toString());
                    vList.add(v6.toString());
                    vList.add(v7.toString());
                    vList.add(v8.toString());
                    vList.add(v9.toString());
                    vList.add(v10.toString());
                    vList.add(v11.toString());
                    vList.add(v12.toString());
                    vList.add(v13.toString());
                    writer.write(vList.toString().replace("[", "").replace("]", ""));
                    writer.write("\r\n");
                }
                Log.d("writer", String.valueOf(writer));
                dbAdapter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("onPostExecute", String.valueOf(result));
            String path = String.valueOf(file);
            exportListener.onExportPerformed(result, path);
        }
    }

    public static interface ExportListener{
        public void onExportPerformed(Boolean result, String path);
    }
}
