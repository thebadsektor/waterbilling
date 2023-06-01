package com.westframework.waterbillingapp.ui.screens.generatebill;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Filter;
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
import androidx.core.content.ContextCompat;
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
import com.westframework.waterbillingapp.data.application.helpers.DBHelper;
import com.westframework.waterbillingapp.ui.screens.MainActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BrowseApplicationActivity extends AppCompatActivity {

    DBHelper controller = new DBHelper(this);
    AlertDialog.Builder builder;
    Dialog dialog;
    private SearchView searchView;
    AppCompatImageView btnBack;
    CardView toggle1, toggle2, toggle3;
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
        //getSupportActionBar().setTitle("WS Water Billing");
        setContentView(R.layout.activity_browse_applications);

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
            public boolean onQueryTextSubmit(String s) {return false;}

            @Override
            public boolean onQueryTextChange(String s) {
                applicationAdapter.getFilter(searchMode).filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                    }
                });
                return true;
            }
        });

        builder = new AlertDialog.Builder(BrowseApplicationActivity.this);
        builder.setView(R.layout.progress);
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        dialog = builder.create();

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
                Intent intent = new Intent(BrowseApplicationActivity.this, ViewGenerateBillActivity.class);
                intent.putExtra("id", application.getId());
                intent.putExtra("applicationNo", application.getApplicationNo());
                intent.putExtra("firstName", application.getFirstName());
                intent.putExtra("lastName",application.getLastName());
                intent.putExtra("middleInitial",application.getMiddleInitial());
                intent.putExtra("meterNo",application.getMeterNo());
                intent.putExtra("houseNo",application.getHouseNo());
                intent.putExtra("buildingNo",application.getBuildingNo());
                intent.putExtra("street",application.getStreet());
                intent.putExtra("brgy",application.getBrgy());
                intent.putExtra("applicationDate",application.getApplicationDate());
                intent.putExtra("applicationType",application.getApplicationType());
                intent.putExtra("orStatus",application.getOrStatus());
                intent.putExtra("status",application.getStatus());
                intent.putExtra("updated_at",application.getUpdatedAt());
                intent.putExtra("created_at",application.getCreatedAt());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        //getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
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
            String applicationNo = data.getStringExtra("applicationNo");
            String firstName = data.getStringExtra("firstName");
            String lastName = data.getStringExtra("lastName");
            String middleInitial = data.getStringExtra("middleInitial");
            String meterNo = data.getStringExtra("meterNo");

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


            ActivityCompat.requestPermissions(BrowseApplicationActivity.this,
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
        Toast.makeText(BrowseApplicationActivity.this, message, Toast.LENGTH_LONG).show();

    }
}

