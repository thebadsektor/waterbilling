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

public class HelperCSVClassifications {

    public static final String id = "id";// 0 integer
    public static final String class_cat = "class_cat";// 1 text(String)
    public static final String cu_m_min = "cu_m_min";
    public static final String cu_m_max = "cu_m_max";
    public static final String amount = "amount";
    public static final String increment_by = "increment_by";
    public static final String updated_at = "updated_at";
    public static final String created_at = "created_at";

    public static class InsertAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public HelperDBClassifications dbAdapter;
        public FileReader file;
        public InsertListener insertListener;

        public InsertAsyncTask(HelperDBClassifications a, FileReader b, InsertListener insertListener){
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
                contentValues.put(class_cat, str[0].toString());
                contentValues.put(cu_m_min, str[1].toString());
                contentValues.put(cu_m_max, str[2].toString());
                contentValues.put(amount, str[3].toString());
                contentValues.put(increment_by, str[4].toString());
                count ++;
                try {
                    if (dbAdapter.insert("ws_classification_tbl", contentValues) < 0) {
                        //return null;
                    }
                } catch (Exception ex) {
                    Log.d("Exception in importing", ex.getMessage().toString());
                }
            }
                String size = String.valueOf(dbAdapter.getClassifications().size());
                Log.d("Inserted", "Bills in DB " + size);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("onPostExecute", String.valueOf(result));
            insertListener.onInsertClassPerformed(result);
        }
    }

    public static interface InsertListener{
        public void onInsertClassPerformed(Boolean result);
    }

    public static class ExportAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public HelperDBClassifications dbAdapter;
        public ExportListener exportListener;
        public File file;

        public ExportAsyncTask(HelperDBClassifications a, File b, ExportListener exportListener){
            dbAdapter = a;
            file = b;
            this.exportListener = exportListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("doInBackground Export CSV", "Export CSV");
            ArrayList<HashMap<String, String>> bills = dbAdapter.getClassifications();
            Log.d("bills", String.valueOf(bills));
            Log.d("doInBackground File", String.valueOf(file));

            List<String> headers = bills.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());
            Log.d("doInBackground headers", String.valueOf(headers));

            try (FileWriter writer = new FileWriter(file, true)) {

                List<String> hList = new ArrayList<String>();
                String h1 = "", h2 = "", h3 = "", h4 = "", h5 = "";
                String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "";
                for (String string : headers) {
                    if("class_cat".equals(string)) h1 = string;
                    if("cu_m_min".equals(string)) h2 = string;
                    if("cu_m_max".equals(string)) h3 = string;
                    if("amount".equals(string)) h4 = string;
                    if("increment_by".equals(string)) h5 = string;
                }
                hList.add(h1.toString());
                hList.add(h2.toString());
                hList.add(h3.toString());
                hList.add(h4.toString());
                hList.add(h5.toString());
                writer.write(hList.toString().replace("[", "").replace("]", ""));
                writer.write("\r\n");
                List<String> vList;
                for (HashMap<String, String> map : bills){
                     vList = new ArrayList<String>();
                    for (Map.Entry<String, String> entry : map.entrySet()){
                        if("class_cat".equals(entry.getKey())) v1 = entry.getValue();
                        if("cu_m_min".equals(entry.getKey())) v2= entry.getValue();
                        if("cu_m_max".equals(entry.getKey())) v3= entry.getValue();
                        if("amount".equals(entry.getKey())) v4= entry.getValue();
                        if("increment_by".equals(entry.getKey())) v5= entry.getValue();
                    }
                    vList.add(v1.toString());
                    vList.add(v2.toString());
                    vList.add(v3.toString());
                    vList.add(v4.toString());
                    vList.add(v5.toString());
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
            exportListener.onExportClassPerformed(result, path);
        }
    }

    public static interface ExportListener{
        public void onExportClassPerformed(Boolean result, String file);
    }

}
