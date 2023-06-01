package com.westframework.waterbillingapp.data.helpers;

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

public class HelperCSVHolidays {

    public static final String id = "id";// 0 integer
    public static final String holiday_name = "holiday_name";// 1 text(String)
    public static final String holiday_month = "holiday_month";
    public static final String holiday_date = "holiday_date";
    public static final String holiday_year = "holiday_year";

    public static class InsertAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public HelperDBHolidays dbAdapter;
        public FileReader file;
        public InsertListener insertListener;

        public InsertAsyncTask(HelperDBHolidays a, FileReader b, InsertListener insertListener){
            dbAdapter = a;
            file = b;
            this.insertListener = insertListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int count = 0;
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
                String[] str = line.split(",", 5);

                ContentValues contentValues = new ContentValues();
                contentValues.put(holiday_name, str[0].toString());
                contentValues.put(holiday_month, str[1].toString());
                contentValues.put(holiday_date, str[2].toString());
                contentValues.put(holiday_year, str[3].toString());
                count ++;
                try {
                    if (dbAdapter.insert("ws_holidays", contentValues) < 0) {
                        //return null;
                    }
                } catch (Exception ex) {
                    Log.d("Exception in importing", ex.getMessage().toString());
                }
            }
                String size = String.valueOf(dbAdapter.getHolidays().size());
                Log.d("Inserted", "Discount Record in DB " + size);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            insertListener.onInsertHolidaysPerformed(result);
        }
    }

    public static interface InsertListener{
        public void onInsertHolidaysPerformed(Boolean result);
    }

    public static class ExportAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public HelperDBHolidays dbAdapter;
        public ExportListener exportListener;
        public File file;

        public ExportAsyncTask(HelperDBHolidays a, File b, ExportListener exportListener){
            dbAdapter = a;
            file = b;
            this.exportListener = exportListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<HashMap<String, String>> bills = dbAdapter.getHolidays();

            List<String> headers = bills.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());

            try (FileWriter writer = new FileWriter(file, true)) {

                List<String> hList = new ArrayList<String>();
                String h1 = "", h2 = "", h3 = "", h4 = "";
                String v1 = "", v2 = "", v3 = "", v4 = "";
                for (String string : headers) {
                    if("holiday_name".equals(string)) h1 = string;
                    if("holiday_month".equals(string)) h2 = string;
                    if("holiday_date".equals(string)) h3 = string;
                    if("holiday_year".equals(string)) h4 = string;
                }
                hList.add(h1.toString());
                hList.add(h2.toString());
                hList.add(h3.toString());
                hList.add(h4.toString());
                writer.write(hList.toString().replace("[", "").replace("]", ""));
                writer.write("\r\n");
                List<String> vList;
                for (HashMap<String, String> map : bills){
                     vList = new ArrayList<String>();
                    for (Map.Entry<String, String> entry : map.entrySet()){
                        if("holiday_name".equals(entry.getKey())) v1 = entry.getValue();
                        if("holiday_month".equals(entry.getKey())) v2= entry.getValue();
                        if("holiday_date".equals(entry.getKey())) v3= entry.getValue();
                        if("holiday_year".equals(entry.getKey())) v4= entry.getValue();
                    }
                    vList.add(v1.toString());
                    vList.add(v2.toString());
                    vList.add(v3.toString());
                    vList.add(v4.toString());
                    writer.write(vList.toString().replace("[", "").replace("]", ""));
                    writer.write("\r\n");
                }
                dbAdapter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String path = String.valueOf(file);
            exportListener.onExportHolidaysPerformed(result, path);
        }
    }

    public static interface ExportListener{
        public void onExportHolidaysPerformed(Boolean result, String file);
    }

}
