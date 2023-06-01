package com.westframework.waterbillingapp.ui.screens.bill;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.widget.SearchView;

import android.widget.Filter;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.application.helpers.DBHelper;
import com.westframework.waterbillingapp.data.bill.WaterBill;
import com.westframework.waterbillingapp.data.bill.WaterBillAdapter;
import com.westframework.waterbillingapp.data.bill.WaterBillViewModel;
import com.westframework.waterbillingapp.data.bill.helpers.CSVHelperBill;
import com.westframework.waterbillingapp.data.bill.helpers.DBHelperBill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BillMainActivity extends AppCompatActivity implements CSVHelperBill.InsertListener, CSVHelperBill.ExportListener {

    DBHelper controller = new DBHelper(this);
    AlertDialog.Builder builder;
    Dialog dialog;
    private SearchView searchView;
    AppCompatImageView btnBack;
    CardView toggle1, toggle2, toggle3, toggle4;
    int searchMode = 1;

    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "";
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    private View mLayout;
    ActivityResultLauncher<Intent> filePicker;

    private WaterBillViewModel waterBillViewModel;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final WaterBillAdapter waterBillAdapter = new WaterBillAdapter();
        recyclerView.setAdapter(waterBillAdapter);

        waterBillViewModel = ViewModelProviders.of(this).get(WaterBillViewModel.class);
        waterBillViewModel.getWaterBills().observe(this, new Observer<List<WaterBill>>() {
            @Override
            public void onChanged(List<WaterBill> waterBills) {
                waterBillAdapter.setWaterBills(waterBills);
            }
        });

        toggle1 = findViewById(R.id.toggleSearchMeter);
        toggle2 = findViewById(R.id.toggleSearchAppNum);
        toggle3 = findViewById(R.id.toggleSearchFullName);
        toggle4 = findViewById(R.id.toggleSearchOr);

        toggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 1;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle4.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                searchView.setQueryHint("Search by Meter Number...");
            }
        });

        toggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 2;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle4.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                searchView.setQueryHint("Search by Application Number...");
            }
        });

        toggle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 3;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                toggle4.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                searchView.setQueryHint("Search by Customer Name...");
            }
        });

        toggle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 4;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle4.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                searchView.setQueryHint("Search by OR Number...");
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mLayout = findViewById(R.id.main_layout);
        searchView  = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                waterBillAdapter.getFilter(searchMode).filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                    }
                });
                return true;
            }
        });

        builder = new AlertDialog.Builder(BillMainActivity.this);
        builder.setView(R.layout.progress);
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        dialog = builder.create();

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        File f = new File(uri.getPath());
                        try {
                            ReadCsvFile(BillMainActivity.this, f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //Touch events
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               waterBillViewModel.delete(waterBillAdapter.getWaterBills(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        waterBillAdapter.setOnItemClickListerner(new WaterBillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WaterBill bill) {
                Intent intent = new Intent(BillMainActivity.this, ViewBillActivity.class);
                intent.putExtra("id", bill.getId());
                intent.putExtra("application_no",bill.getApplicationNo());
                intent.putExtra("full_name", bill.getFullName());
                intent.putExtra("bill_address", bill.getBillAddress());
                intent.putExtra("classification", bill.getClassification());
                intent.putExtra("meter_read",bill.getMeterRead());
                intent.putExtra("reading_month", bill.getReadingMonth());
                intent.putExtra("read_date",bill.getReadDate());
                intent.putExtra("due_date",bill.getDueDate());
                intent.putExtra("pre_reading",bill.getPreReading());
                intent.putExtra("actual_reading",bill.getActualReading());
                intent.putExtra("cu_m_used", bill.getUsed());
                intent.putExtra("or_num", bill.getOrNum());
                intent.putExtra("or_amount", bill.getOrAmount());
                intent.putExtra("arrears", bill.getArrears());
                intent.putExtra("others", bill.getOthers());
                intent.putExtra("senior_ids", bill.getSeniorIds());
                intent.putExtra("discount_amount", bill.getDiscountAmount());
                intent.putExtra("bill_amount", bill.getBillAmount());
                intent.putExtra("status", bill.getStatus());
                intent.putExtra("surcharge", bill.getSurcharge());
                intent.putExtra("payment_after_due", bill.getPaymentAfterDue());
                intent.putExtra("updated_at", bill.getUpdatedAt());
                intent.putExtra("created_at", bill.getCreatedAt());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bill_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_import_csv) {
            OpenFilePicker();
        } else if (id == R.id.action_export_csv) {
            exportCSV();
        }
        return super.onOptionsItemSelected(item);
    }

    //----CSV Import START
    private void ReadCsvFile(BillMainActivity billMainActivity, File file) throws FileNotFoundException {

       if(file.exists()){
           if(file.canRead()){
//               Toast.makeText(BillMainActivity.this, String.valueOf(file), Toast.LENGTH_LONG).show();
           }else{
               Intent intent = new Intent();
               intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
               Uri uri = Uri.fromParts("package", this.getPackageName(), null);
               intent.setData(uri);
               startActivity(intent);
           }
       }else{
           Toast.makeText(BillMainActivity.this, "File Not Found", Toast.LENGTH_LONG).show();
       }
        DBHelperBill dbAdapter = new DBHelperBill(this);
        FileReader fr = new FileReader(file);
        Log.d("FileReader in ReadCSVFile", String.valueOf(fr));
        dbAdapter.open();
        dbAdapter.delete();
        dbAdapter.close();
        dbAdapter.open();

        setDialog(true);
        new CSVHelperBill.InsertAsyncTask(dbAdapter, fr, (CSVHelperBill.InsertListener) this).execute();

    }
    //----CSV Import END

    //----CSV Export START
    private void exportCSV() {

        DBHelperBill dbAdapter;

        if (CheckPermission()) {

            dbAdapter = new DBHelperBill(this);

            Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmm");
            String todayFormatted = format.format(today.getTime());
            String csvPath = Environment.getExternalStorageDirectory().toString() + "/exported_csv/";
            String filename = "billing_records_"+todayFormatted + ".csv";
            File file = new File(csvPath, filename);
            try {
                Files.createDirectories(Paths.get(csvPath));
                new CSVHelperBill.ExportAsyncTask(dbAdapter, file, (CSVHelperBill.ExportListener) this).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }

    }
    //----CSV Export END

    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(mLayout, R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermission();
                }
            }).show();

            return false;
        }

    }

    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            fileIntent.setType("text/csv");
            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Toast("ChooseFile error: " + ex.getMessage().toString(), ex);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker();
            } else {
                Snackbar.make(mLayout, R.string.storage_access_denied,
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(BillMainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

            ActivityCompat.requestPermissions( this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    }, 1
            );

        } else {
            Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }

    public void OpenFilePicker() {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
        } catch (ActivityNotFoundException e) {
//            lbl.setText("No activity can handle picking a file. Showing alternatives.");
        }
    }

    private void setDialog(boolean show){
        if (show){
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            dialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    void Toast(String message, Exception ex) {
        if (ex != null)
            Log.e("Error", ex.getMessage().toString());
        Toast.makeText(BillMainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInsertBillPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onExportBillPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }
    //----Excel Import END
}