package com.westframework.waterbillingapp.ui.screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;
import com.westframework.waterbillingapp.data.amnesty.AmnestyDao;
import com.westframework.waterbillingapp.data.application.ApplicationDao;
import com.westframework.waterbillingapp.data.application.helpers.CSVHelper;
import com.westframework.waterbillingapp.data.application.helpers.DBHelper;
import com.westframework.waterbillingapp.data.bill.WaterBillDao;
import com.westframework.waterbillingapp.data.bill.helpers.CSVHelperBill;
import com.westframework.waterbillingapp.data.bill.helpers.DBHelperBill;
import com.westframework.waterbillingapp.data.classification.ClassificationDao;
import com.westframework.waterbillingapp.data.discount.DiscountDao;
import com.westframework.waterbillingapp.data.helpers.HelperCSVAmnesties;
import com.westframework.waterbillingapp.data.helpers.HelperCSVClassifications;
import com.westframework.waterbillingapp.data.helpers.HelperCSVDiscounts;
import com.westframework.waterbillingapp.data.helpers.HelperCSVHolidays;
import com.westframework.waterbillingapp.data.helpers.HelperDBAmnesties;
import com.westframework.waterbillingapp.data.helpers.HelperDBClassifications;
import com.westframework.waterbillingapp.data.helpers.HelperDBDiscounts;
import com.westframework.waterbillingapp.data.helpers.HelperDBHolidays;
import com.westframework.waterbillingapp.data.holiday.HolidayDao;
import com.westframework.waterbillingapp.ui.screens.amnesty.AmnestyMainActivity;
import com.westframework.waterbillingapp.ui.screens.application.ApplicationMainActivity;
import com.westframework.waterbillingapp.ui.screens.bill.BillMainActivity;
import com.westframework.waterbillingapp.ui.screens.classification.ClassificationMainActivity;
import com.westframework.waterbillingapp.ui.screens.discount.DiscountMainActivity;
import com.westframework.waterbillingapp.ui.screens.generatebill.BrowseApplicationActivity;
import com.westframework.waterbillingapp.ui.screens.holiday.HolidayMainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements
        CSVHelperBill.InsertListener, CSVHelperBill.ExportListener,
        CSVHelper.InsertListener, CSVHelper.ExportListener,
        HelperCSVClassifications.InsertListener, HelperCSVClassifications.ExportListener,
        HelperCSVDiscounts.InsertListener, HelperCSVDiscounts.ExportListener,
        HelperCSVAmnesties.InsertListener, HelperCSVAmnesties.ExportListener,
        HelperCSVHolidays.InsertListener, HelperCSVHolidays.ExportListener {

    AppCompatImageView btnGenerateBill;
    CardView btnBillsMain, btnApplicationsMain;
    ConstraintLayout btnClassificationsMain, btnDiscountsMain,
    btnHolidaysMain, btnAmnestyMain;
    ConstraintLayout mLayout;
    TextView tvDashBillCount, tvDashAppCount,
            dashItem1number, dashItem4number,
            dashItem3number, dashItem5number,
            tvDashOptions;
    LinearLayout btnImport, btnExport;
    Button btnImportBills, btnImportApplications, btnImportClassifications,
            btnImportDiscounts, btnImportAmnesties, btnImportHolidays, btnRunMigration,
            btnExportBills, btnExportApplications, btnExportClassifications,
            btnExportDiscounts, btnExportAmnesties, btnExportHolidays, btnExportAll,
            btnPurgeDb;
    AlertDialog.Builder builder;
    Dialog dialog;
    String importMode = "bills";
    String exportMode = "bills";

    ActivityResultLauncher<Intent> filePicker;
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WaterBilliingAppDatabase db = WaterBilliingAppDatabase.getInstance(getApplication());
        WaterBillDao waterBillDao = db.waterBillDao();
        ApplicationDao applicationDao = db.applicationDao();
        ClassificationDao classificationDao = db.classificationDao();
        DiscountDao discountDao = db.discountDao();
        AmnestyDao amnestyDao = db.amnestyDao();
        HolidayDao holidayDao = db.holidayDao();


        new GetCountAsyncTask(waterBillDao, applicationDao, classificationDao, discountDao, amnestyDao, holidayDao, MainActivity.this).execute();

        mLayout = findViewById(R.id.mLayout);
        btnImport = findViewById(R.id.dashItem2);
        btnExport = findViewById(R.id.dashItem3);
        btnGenerateBill = findViewById(R.id.btnGenerateBill);
        btnBillsMain = findViewById(R.id.btnCardBillsMain);
        btnApplicationsMain = findViewById(R.id.btnApplicationsMain);
        btnClassificationsMain = findViewById(R.id.btnClassificationsMain);
        btnDiscountsMain = findViewById(R.id.btnDiscountsMain);
        btnHolidaysMain = findViewById(R.id.btnHolidaysMain);
        btnAmnestyMain = findViewById(R.id.btnAmnestyMain);
        tvDashOptions = findViewById(R.id.tvDashOptions);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress);
        dialog = builder.create();

        //btnImport START
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Import Options");
                builder.setMessage("Import records from CSV files.");
                builder.setCancelable(true);

                // Set the layout using the setView() method
                view = getLayoutInflater().inflate(R.layout.layout_main_import, null);
                builder.setView(view);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the "Option 2" button is clicked
                    }
                });

                // Create and show the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btnImportBills = view.findViewById(R.id.btnImportBills);
                btnImportApplications = view.findViewById(R.id.btnImportApplications);
                btnImportClassifications = view.findViewById(R.id.btnImportClassifications);
                btnImportDiscounts = view.findViewById(R.id.btnImportDiscounts);
                btnImportAmnesties = view.findViewById(R.id.btnImportAmnesties);
                btnImportHolidays = view.findViewById(R.id.btnImportHolidays);
                btnRunMigration = view.findViewById(R.id.btnRunMigration);

                btnImportBills.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "bills";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnImportApplications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "apps";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnImportClassifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "class";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnImportDiscounts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "discounts";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnImportAmnesties.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "amnesties";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnImportHolidays.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "holidays";
                        //Toast.makeText(MainActivity.this, importMode, Toast.LENGTH_LONG).show();
                        OpenFilePicker();
                    }
                });

                btnRunMigration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        importMode = "migrate";
                        Toast.makeText(MainActivity.this, "Coming soon...", Toast.LENGTH_LONG).show();
                        //OpenFilePicker();
                    }
                });
            }
        });
        //btnImport END
        //btnExport START
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Export Options");
                builder.setMessage("Export records to CSV files.");
                builder.setCancelable(true);

                // Set the layout using the setView() method
                view = getLayoutInflater().inflate(R.layout.layout_main_export, null);
                builder.setView(view);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the "Option 2" button is clicked
                    }
                });

                // Create and show the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btnExportBills = view.findViewById(R.id.btnExportBills);
                btnExportApplications = view.findViewById(R.id.btnExportApplications);
                btnExportClassifications = view.findViewById(R.id.btnExportClassifications);
                btnExportDiscounts = view.findViewById(R.id.btnExportDiscounts);
                btnExportAmnesties = view.findViewById(R.id.btnExportAmnesties);
                btnExportHolidays = view.findViewById(R.id.btnExportHolidays);
                btnExportAll = view.findViewById(R.id.btnExportAll);

                btnExportBills.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "bills";
                        exportCSV();
                    }
                });

                btnExportApplications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "apps";
                        exportCSV();
                    }
                });

                btnExportClassifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "class";
                        exportCSV();
                    }
                });

                btnExportDiscounts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "discounts";
                        exportCSV();
                    }
                });

                btnExportAmnesties.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "amnesties";
                        exportCSV();
                    }
                });

                btnExportHolidays.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "holidays";
                        exportCSV();
                    }
                });

                btnExportAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exportMode = "all";
                        exportCSV();
                    }
                });
            }
        });
        //btnExport END
        btnGenerateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BrowseApplicationActivity.class);
                startActivity(i);
            }
        });

        btnBillsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BillMainActivity.class);
                startActivity(i);
            }
        });

        btnApplicationsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ApplicationMainActivity.class);
                startActivity(i);
            }
        });

        btnClassificationsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ClassificationMainActivity.class);
                startActivity(i);
            }
        });

        btnDiscountsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DiscountMainActivity.class);
                startActivity(i);
            }
        });

        btnHolidaysMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HolidayMainActivity.class);
                startActivity(i);
            }
        });

        btnAmnestyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AmnestyMainActivity.class);
                startActivity(i);
            }
        });

        tvDashOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Options");
                builder.setMessage("More tools for WS Water Billing.");
                builder.setCancelable(true);

                // Set the layout using the setView() method
                view = getLayoutInflater().inflate(R.layout.layout_dash_options, null);
                builder.setView(view);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the "Option 2" button is clicked
                    }
                });

                // Create and show the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btnPurgeDb = view.findViewById(R.id.btnPurgeDb);

                btnPurgeDb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Are you sure you want to purge the database?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User confirmed action, proceed with database purge
                                    Toast.makeText(MainActivity.this, "Purging...", Toast.LENGTH_LONG).show();
                                    // Add your code for purging the database here
                                }
                            })
                            .setNegativeButton("Close", null)
                            .show();
                    }
                });
            }
        });

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        File f = new File(uri.getPath());
                        try {
                            ReadCsvFile(MainActivity.this, f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                Snackbar.make(mLayout, R.string.storage_access_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this,
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
            //lbl.setText("No activity can handle picking a file. Showing alternatives.");
        }
    }

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

    //----CSV Import START
    private void ReadCsvFile(MainActivity mainActivity, File file) throws FileNotFoundException {

        if(file.exists()){
            if(file.canRead()){
                //Toast.makeText(BillMainActivity.this, String.valueOf(file), Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }else{
            Toast.makeText(MainActivity.this, "File Not Found", Toast.LENGTH_LONG).show();
        }

        DBHelperBill dbAdapter = new DBHelperBill(this);
        FileReader fr = new FileReader(file);

        setDialog(true);

        switch(importMode) {
            case "bills":
                dbAdapter.open();
                dbAdapter.delete();
                dbAdapter.close();
                dbAdapter.open();
                new CSVHelperBill.InsertAsyncTask(dbAdapter, fr, (CSVHelperBill.InsertListener) this).execute();
                break;
            case "apps":
                DBHelper dbAppAdapter = new DBHelper(this);
                FileReader frApp = new FileReader(file);
                dbAppAdapter.open();
                dbAppAdapter.delete();
                dbAppAdapter.close();
                dbAppAdapter.open();
                new CSVHelper.InsertAsyncTask(dbAppAdapter, frApp, (CSVHelper.InsertListener) this).execute();
                break;
            case "class":
                HelperDBClassifications dbClassAdapter = new HelperDBClassifications(this);
                FileReader frClass = new FileReader(file);
                dbClassAdapter.open();
                dbClassAdapter.delete();
                dbClassAdapter.close();
                dbClassAdapter.open();
                new HelperCSVClassifications.InsertAsyncTask(dbClassAdapter, frClass, (HelperCSVClassifications.InsertListener) this).execute();
                break;
            case "discounts":
                HelperDBDiscounts dbDiscountAdapter = new HelperDBDiscounts(this);
                FileReader frDiscount = new FileReader(file);
                dbDiscountAdapter.open();
                dbDiscountAdapter.delete();
                dbDiscountAdapter.close();
                dbDiscountAdapter.open();
                new HelperCSVDiscounts.InsertAsyncTask(dbDiscountAdapter, frDiscount, (HelperCSVDiscounts.InsertListener) this).execute();
                break;
            case "amnesties":
                HelperDBAmnesties dbAmnestyAdapter = new HelperDBAmnesties(this);
                FileReader frAmnesty = new FileReader(file);
                dbAmnestyAdapter.open();
                dbAmnestyAdapter.delete();
                dbAmnestyAdapter.close();
                dbAmnestyAdapter.open();
                new HelperCSVAmnesties.InsertAsyncTask(dbAmnestyAdapter, frAmnesty, (HelperCSVAmnesties.InsertListener) this).execute();
                break;
            case "holidays":
                HelperDBHolidays dbHolidayAdapter = new HelperDBHolidays(this);
                FileReader frHoliday = new FileReader(file);
                dbHolidayAdapter.open();
                dbHolidayAdapter.delete();
                dbHolidayAdapter.close();
                dbHolidayAdapter.open();
                new HelperCSVHolidays.InsertAsyncTask(dbHolidayAdapter, frHoliday, (HelperCSVHolidays.InsertListener) this).execute();
                break;
            case "migrate":
                //
                break;
        }
    }
    //----CSV Import END
    //----CSV Export START
    private void exportCSV() {

        DBHelperBill dbAdapter;

        if (CheckPermission()) {

            dbAdapter = new DBHelperBill(this);

            Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HHmm");
            String todayFormatted = format.format(today.getTime());
            String csvPath = Environment.getExternalStorageDirectory().toString() + "/exported_csv/";
            String filename = "";
            File file = null;

            try {
                Files.createDirectories(Paths.get(csvPath));
            } catch (IOException e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

            try {

                switch (exportMode){
                    case "bills":
                        filename = "billing_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Files.createDirectories(Paths.get(csvPath));
                        new CSVHelperBill.ExportAsyncTask(dbAdapter, file, (CSVHelperBill.ExportListener) this).execute();
                        break;
                    case "apps":
                        DBHelper dbAppAdapter = new DBHelper(this);
                        filename = "application_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Files.createDirectories(Paths.get(csvPath));
                        new CSVHelper.ExportAsyncTask(dbAppAdapter, file, (CSVHelper.ExportListener) this).execute();
                        break;
                    case "class":
                        HelperDBClassifications dbClassAdapter = new HelperDBClassifications(this);
                        filename = "classification_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Files.createDirectories(Paths.get(csvPath));
                        new HelperCSVClassifications.ExportAsyncTask(dbClassAdapter, file, (HelperCSVClassifications.ExportListener) this).execute();
                        break;
                    case "discounts":
                        HelperDBDiscounts dbDiscountsAdapter = new HelperDBDiscounts(this);
                        filename = "discount_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Files.createDirectories(Paths.get(csvPath));
                        new HelperCSVDiscounts.ExportAsyncTask(dbDiscountsAdapter, file, (HelperCSVDiscounts.ExportListener) this).execute();
                        break;
                    case "amnesties":
                        HelperDBAmnesties dbAmnestyAdapter = new HelperDBAmnesties(this);
                        filename = "amnesty_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Log.d("amnesties", String.valueOf(file));
                        Files.createDirectories(Paths.get(csvPath));
                        new HelperCSVAmnesties.ExportAsyncTask(dbAmnestyAdapter, file, (HelperCSVAmnesties.ExportListener) this).execute();
                        break;
                    case "holidays":
                        HelperDBHolidays dbHolidayAdapter = new HelperDBHolidays(this);
                        filename = "holiday_records_"+todayFormatted + ".csv";
                        file = new File(csvPath, filename);
                        Files.createDirectories(Paths.get(csvPath));
                        new HelperCSVHolidays.ExportAsyncTask(dbHolidayAdapter, file, (HelperCSVHolidays.ExportListener) this).execute();
                        break;
                    case "all":
                        exportAllCSV();
                        break;
                }
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
    //----CSV Export ALL START
    private void exportAllCSV() {

        if (CheckPermission()) {

            Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HHmm");
            String todayFormatted = format.format(today.getTime());
            String csvPath = Environment.getExternalStorageDirectory().toString() + "/exported_csv/";
            String filename = "";
            File file = null;

            try {
                DBHelperBill dbAdapter = new DBHelperBill(this);
                filename = "billing_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                Files.createDirectories(Paths.get(csvPath));
                new CSVHelperBill.ExportAsyncTask(dbAdapter, file, (CSVHelperBill.ExportListener) this).execute();

                DBHelper dbAppAdapter = new DBHelper(this);
                filename = "application_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                new CSVHelper.ExportAsyncTask(dbAppAdapter, file, (CSVHelper.ExportListener) this).execute();

                HelperDBClassifications dbClassAdapter = new HelperDBClassifications(this);
                filename = "classification_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                new HelperCSVClassifications.ExportAsyncTask(dbClassAdapter, file, (HelperCSVClassifications.ExportListener) this).execute();

                HelperDBDiscounts dbDiscountsAdapter = new HelperDBDiscounts(this);
                filename = "discount_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                new HelperCSVDiscounts.ExportAsyncTask(dbDiscountsAdapter, file, (HelperCSVDiscounts.ExportListener) this).execute();

                HelperDBAmnesties dbAmnestyAdapter = new HelperDBAmnesties(this);
                filename = "amnesty_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                Log.d("amnesties", String.valueOf(file));
                new HelperCSVAmnesties.ExportAsyncTask(dbAmnestyAdapter, file, (HelperCSVAmnesties.ExportListener) this).execute();

                HelperDBHolidays dbHolidayAdapter = new HelperDBHolidays(this);
                filename = "holiday_records_"+todayFormatted + ".csv";
                file = new File(csvPath, filename);
                new HelperCSVHolidays.ExportAsyncTask(dbHolidayAdapter, file, (HelperCSVHolidays.ExportListener) this).execute();

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
    //----CSV Export ALL END

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
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInsertBillPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Billing Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, BillMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onInsertPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Application Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, ApplicationMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onInsertClassPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Classification Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, ClassificationMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onInsertDiscountPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Application Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, DiscountMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onInsertAmnestiesPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Amnesty Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, AmnestyMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onInsertHolidaysPerformed(Boolean result) {
        DBHelperBill dbAdapter = new DBHelperBill(this);
        dbAdapter.close();
        setDialog(false);
        String message = "Holiday Records Import Done";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, HolidayMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onExportBillPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExportPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExportClassPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExportDiscountPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExportAmnestiesPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExportHolidaysPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }

    private static class GetCountAsyncTask extends AsyncTask<Void, Void, int[]> {
        private WaterBillDao waterBillDao;
        private ApplicationDao applicationDao;
        private ClassificationDao classificationDao;
        private DiscountDao discountDao;
        private AmnestyDao amnestyDao;
        private HolidayDao holidayDao;
        private MainActivity mainActivity;

        private GetCountAsyncTask(WaterBillDao waterBillDao, ApplicationDao applicationDao,
                                  ClassificationDao classificationDao, DiscountDao discountDao,
                                  AmnestyDao amnestyDao, HolidayDao holidayDao,
                                  MainActivity mainActivity) {
            this.waterBillDao = waterBillDao;
            this.applicationDao = applicationDao;
            this.classificationDao = classificationDao;
            this.discountDao = discountDao;
            this.amnestyDao = amnestyDao;
            this.holidayDao = holidayDao;
            this.mainActivity = mainActivity;
        }

        @Override
        protected int[] doInBackground(Void... voids) {
            int[] counts = new int[6];
            counts[0] = waterBillDao.getCount();
            counts[1] = applicationDao.getCount();
            counts[2] = classificationDao.getCount();
            counts[3] = discountDao.getCount();
            counts[4] = amnestyDao.getCount();
            counts[5] = holidayDao.getCount();
            return counts;
        }

        @Override
        protected void onPostExecute(int[] counts) {
            super.onPostExecute(counts);
            mainActivity.updateUI(counts);
        }
    }

    public void updateUI(int[] counts) {
        tvDashBillCount = findViewById(R.id.tvDashBillCount);
        tvDashAppCount = findViewById(R.id.tvDashAppCount);
        dashItem1number = findViewById(R.id.dashItem1number);
        dashItem4number = findViewById(R.id.dashItem4number);
        dashItem3number = findViewById(R.id.dashItem3number);
        dashItem5number = findViewById(R.id.dashItem5number);

        tvDashBillCount.setText(String.valueOf(counts[0]));
        tvDashAppCount.setText(String.valueOf(counts[1]));
        dashItem1number.setText(String.valueOf(counts[2]));
        dashItem4number.setText(String.valueOf(counts[3]));
        dashItem3number.setText(String.valueOf(counts[4]));
        dashItem5number.setText(String.valueOf(counts[5]));
    }
}