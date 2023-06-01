package com.westframework.waterbillingapp.ui.screens.application;

import android.Manifest;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.application.Application;
import com.westframework.waterbillingapp.data.application.ApplicationAdapter;
import com.westframework.waterbillingapp.data.application.ApplicationViewModel;
import com.westframework.waterbillingapp.data.application.helpers.CSVHelper;
import com.westframework.waterbillingapp.data.application.helpers.DBHelper;

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

public class ApplicationMainActivity extends AppCompatActivity implements CSVHelper.InsertListener, CSVHelper.ExportListener{

    DBHelper controller = new DBHelper(this);
    AlertDialog.Builder builder;
    Dialog dialog;
    private SearchView searchView;
    CardView toggle1, toggle2, toggle3;
    AppCompatImageView btnBack;
    int searchMode = 1;

    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "";
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    private View mLayout;
    ActivityResultLauncher<Intent> filePicker;

    private ApplicationViewModel applicationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ApplicationAdapter applicationAdapter = new ApplicationAdapter();
        recyclerView.setAdapter(applicationAdapter);

        applicationViewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);
        applicationViewModel.getApplications().observe(this, new Observer<List<Application>>() {
            @Override
            public void onChanged(List<Application> applications) {
                applicationAdapter.setApplications(applications);
            }
        });

        mLayout = findViewById(R.id.main_layout);

        toggle1 = findViewById(R.id.toggleSearchMeter);
        toggle2 = findViewById(R.id.toggleSearchAppNum);
        toggle3 = findViewById(R.id.toggleSearchFullName);

        toggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 1;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                searchView.setQueryHint("Search by Meter Number...");
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });

        toggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 2;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                searchView.setQueryHint("Search by Application Number...");
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });

        toggle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMode = 3;
                toggle1.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle2.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_3));
                toggle3.setCardBackgroundColor(getResources().getColor(R.color.dashboard_item_2));
                searchView.setQueryHint("Search by Customer Name...");
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });
        searchView  = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return true;}

            @Override
            public boolean onQueryTextChange(String s) {
                applicationAdapter.getFilter(searchMode).filter(s);
                return true;
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        builder = new AlertDialog.Builder(ApplicationMainActivity.this);
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
                    ReadCsvFile(ApplicationMainActivity.this, f);
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
               applicationViewModel.delete(applicationAdapter.getApplications(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        applicationAdapter.setOnItemClickListerner(new ApplicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Application application) {
                Intent intent = new Intent(ApplicationMainActivity.this, ViewApplicationActivity.class);
                intent.putExtra("id", application.getId());
                intent.putExtra("application_no", application.getApplicationNo());
                intent.putExtra("lname",application.getLastName());
                intent.putExtra("mname",application.getMiddleInitial());
                intent.putExtra("fname", application.getFirstName());
                intent.putExtra("meter_no",application.getMeterNo());
                intent.putExtra("house_no",application.getHouseNo());
                intent.putExtra("building_no",application.getBuildingNo());
                intent.putExtra("street",application.getStreet());
                intent.putExtra("barangay",application.getBrgy());
                intent.putExtra("application_date",application.getApplicationDate());
                intent.putExtra("classification",application.getApplicationType());
                intent.putExtra("or_status",application.getOrStatus());
                intent.putExtra("updated_at",application.getUpdatedAt());
                intent.putExtra("created_at",application.getCreatedAt());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.application_main_menu, menu);
        //getMenuInflater().inflate(R.menu.new_menu, menu);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.top_menu:
//                Intent intent = new Intent(ApplicationMainActivity.this, AddApplicationActivity.class);
//                startActivityForResult(intent, 1);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            String applicationNo = data.getStringExtra("application_no");
            String firstName = data.getStringExtra("fname");
            String lastName = data.getStringExtra("lname");
            String middleInitial = data.getStringExtra("mname");
            String meterNo = data.getStringExtra("ctc_no");

            Application application = new Application(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            applicationViewModel.insert(application);
            Toast.makeText(getApplicationContext(), "Application Saved", Toast.LENGTH_LONG).show();
        }

        else if(requestCode == 2 && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra("titleLast");
            String description = data.getStringExtra("descriptionLast");
            int id = data.getIntExtra("appId", -1);

            Application application = new Application(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            application.setId(id);
            applicationViewModel.update(application);
        }

    }

    //----CSV Import START
    private void ReadCsvFile(ApplicationMainActivity applicationMainActivity, File file) throws FileNotFoundException {

        if(file.exists()){
            if(file.canRead()){
//                Toast.makeText(ApplicationMainActivity.this, String.valueOf(file), Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(ApplicationMainActivity.this, "File Unreadable", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(ApplicationMainActivity.this, "File Not Found", Toast.LENGTH_LONG).show();
        }
        DBHelper dbAdapter = new DBHelper(this);
        FileReader fr = new FileReader(file);
        dbAdapter.open();
        dbAdapter.delete();
        dbAdapter.close();
        dbAdapter.open();

        setDialog(true);

        new CSVHelper.InsertAsyncTask(dbAdapter, fr, (CSVHelper.InsertListener) this).execute();
    }
    //----CSV Import END

    //----CSV Export START
    private void exportCSV() {

        DBHelper dbAdapter;

        if (CheckPermission()) {

            dbAdapter = new DBHelper(this);

            Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmm");
            String todayFormatted = format.format(today.getTime());
            String csvPath = Environment.getExternalStorageDirectory().toString() + "/exported_csv/";
            String filename = "application_records_"+todayFormatted + ".csv";
            File file = new File(csvPath, filename);
            try {
                Files.createDirectories(Paths.get(csvPath));
                new CSVHelper.ExportAsyncTask(dbAdapter, file, (CSVHelper.ExportListener) this).execute();
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

            if (fileType == extensionXLS)
                fileIntent.setType("application/vnd.ms-excel");
            else
                fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

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


            ActivityCompat.requestPermissions(ApplicationMainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

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
        Toast.makeText(ApplicationMainActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onInsertPerformed(Boolean result) {
        DBHelper dbAdapter = new DBHelper(this);
        dbAdapter.close();
        setDialog(false);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onExportPerformed(Boolean result, String path) {
        setDialog(false);
        Toast.makeText(this, "CSV Created: " + path, Toast.LENGTH_LONG).show();
    }
}

