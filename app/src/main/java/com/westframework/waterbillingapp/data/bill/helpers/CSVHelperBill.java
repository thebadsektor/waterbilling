package com.westframework.waterbillingapp.data.bill.helpers;

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

public class CSVHelperBill {

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

    public static class InsertAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public DBHelperBill dbAdapter;
        public FileReader file;
        public InsertListener insertListener;

        public InsertAsyncTask(DBHelperBill a, FileReader b, InsertListener insertListener){
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
                String[] str = line.split(",", 22);

                ContentValues contentValues = new ContentValues();
                contentValues.put(application_no, str[0].toString());
                contentValues.put(read_date, str[1].toString());
                contentValues.put(due_date, str[2].toString());
                contentValues.put(full_name, str[3].toString());
                contentValues.put(bill_address, str[4].toString());
                contentValues.put(classification, str[5].toString());
                contentValues.put(meter_read, str[6].toString());
                contentValues.put(senior_ids, str[7].toString());
                contentValues.put(reading_month, str[8].toString());
                contentValues.put(pre_reading, str[9].toString());
                contentValues.put(actual_reading, str[10].toString());
                contentValues.put(cu_m_used, str[11].toString());
                contentValues.put(discount, str[12].toString());
                contentValues.put(penalty, str[13].toString());
                contentValues.put(discount_amount, str[14].toString());
                contentValues.put(bill_amount, str[15].toString());
                contentValues.put(status, str[16].toString());
                contentValues.put(or_num, str[17].toString());
                contentValues.put(or_amount, str[18].toString());
                contentValues.put(or_date, str[19].toString());
                contentValues.put(bill_cat, str[20].toString());
                contentValues.put(bill_status, str[21].toString());
                count ++;
                try {
                    if (dbAdapter.insert("ws_water_bill", contentValues) < 0) {
                        //return null;
                    }
                } catch (Exception ex) {
                    Log.d("Exception in importing", ex.getMessage().toString());
                }

            }
                String size = String.valueOf(dbAdapter.getAllWaterBills().size());
                Log.d("Inserted", "Bills in DB " + size);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            insertListener.onInsertBillPerformed(result);
        }
    }

    public static interface InsertListener{
        public void onInsertBillPerformed(Boolean result);
    }

    public static class ExportAsyncTask extends AsyncTask<Void, Boolean, Boolean>{

        public DBHelperBill dbAdapter;
        public ExportListener exportListener;
        public File file;

        public ExportAsyncTask(DBHelperBill a, File b, ExportListener exportListener){
            dbAdapter = a;
            file = b;
            this.exportListener = exportListener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<HashMap<String, String>> bills = dbAdapter.getAllWaterBills();

            List<String> headers = bills.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());

            try (FileWriter writer = new FileWriter(file, true)) {

                List<String> hList = new ArrayList<String>();
                String h1 = "", h2 = "", h3 = "", h4 = "", h5 = "",
                        h6 = "", h7 = "", h8 = "", h9 = "", h10 = "",
                        h11 = "", h12 = "", h13 = "", h14 = "", h15 = "",
                        h16 = "", h17 = "", h18 = "", h19 = "", h20 = "",
                        h21 = "", h22 = "";
                String v1 = "", v2 = "", v3 = "", v4 = "", v5 = "",
                        v6 = "", v7 = "", v8 = "", v9 = "", v10 = "",
                        v11 = "", v12 = "", v13 = "", v14 = "", v15 = "",
                        v16 = "", v17 = "", v18 = "", v19 = "", v20 = "",
                        v21 = "", v22 = "";
                for (String string : headers) {
                    if("application_no".equals(string)) h1 = string;
                    if("read_date".equals(string)) h2 = string;
                    if("due_date".equals(string)) h3 = string;
                    if("full_name".equals(string)) h4 = string;
                    if("bill_address".equals(string)) h5 = string;
                    if("classification".equals(string)) h6 = string;
                    if("meter_read".equals(string)) h7= string;
                    if("senior_ids".equals(string)) h8 = string;
                    if("reading_month".equals(string)) h9 = string;
                    if("pre_reading".equals(string)) h10 = string;
                    if("actual_reading".equals(string)) h11 = string;
                    if("cu_m_used".equals(string)) h12 = string;
                    if("discount".equals(string)) h13 = string;
                    if("penalty".equals(string)) h14 = string;
                    if("discount_amount".equals(string)) h15 = string;
                    if("bill_amount".equals(string)) h16 = string;
                    if("status".equals(string)) h17 = string;
                    if("or_num".equals(string)) h18 = string;
                    if("or_amount".equals(string)) h19 = string;
                    if("or_date".equals(string)) h20 = string;
                    if("bill_cat".equals(string)) h21 = string;
                    if("bill_status".equals(string)) h22 = string;
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
                hList.add(h14.toString());
                hList.add(h15.toString());
                hList.add(h16.toString());
                hList.add(h17.toString());
                hList.add(h18.toString());
                hList.add(h19.toString());
                hList.add(h20.toString());
                hList.add(h21.toString());
                hList.add(h22.toString());
                writer.write(hList.toString().replace("[", "").replace("]", ""));
                writer.write("\r\n");
                List<String> vList;
                for (HashMap<String, String> map : bills){
                     vList = new ArrayList<String>();
                    for (Map.Entry<String, String> entry : map.entrySet()){
                        if("application_no".equals(entry.getKey())) v1 = entry.getValue();
                        if("read_date".equals(entry.getKey())) v2= entry.getValue();
                        if("due_date".equals(entry.getKey())) v3= entry.getValue();
                        if("full_name".equals(entry.getKey())) v4= entry.getValue();
                        if("bill_address".equals(entry.getKey())) v5= entry.getValue();
                        if("classification".equals(entry.getKey())) v6= entry.getValue();
                        if("meter_read".equals(entry.getKey())) v7= entry.getValue();
                        if("senior_ids".equals(entry.getKey())) v8= entry.getValue();
                        if("reading_month".equals(entry.getKey())) v9= entry.getValue();
                        if("pre_reading".equals(entry.getKey())) v10 = entry.getValue();
                        if("actual_reading".equals(entry.getKey())) v11= entry.getValue();
                        if("cu_m_used".equals(entry.getKey())) v12= entry.getValue();
                        if("discount".equals(entry.getKey())) v13= entry.getValue();
                        if("penalty".equals(entry.getKey())) v14= entry.getValue();
                        if("discount_amount".equals(entry.getKey())) v15= entry.getValue();
                        if("bill_amount".equals(entry.getKey())) v16= entry.getValue();
                        if("status".equals(entry.getKey())) v17= entry.getValue();
                        if("or_num".equals(entry.getKey())) v18= entry.getValue();
                        if("or_amount".equals(entry.getKey())) v19= entry.getValue();
                        if("or_date".equals(entry.getKey())) v20= entry.getValue();
                        if("bill_cat".equals(entry.getKey())) v21= entry.getValue();
                        if("bill_status".equals(entry.getKey())) v22= entry.getValue();
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
                    vList.add(v14.toString());
                    vList.add(v15.toString());
                    vList.add(v16.toString());
                    vList.add(v17.toString());
                    vList.add(v18.toString());
                    vList.add(v19.toString());
                    vList.add(v20.toString());
                    vList.add(v21.toString());
                    vList.add(v22.toString());
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
            exportListener.onExportBillPerformed(result, path);
        }
    }

    public static interface ExportListener{
        public void onExportBillPerformed(Boolean result, String file);
    }

}
