package com.westframework.waterbillingapp.ui.screens.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.ui.screens.bill.UpdateBillActivity;
import com.westframework.waterbillingapp.ui.screens.bill.ViewBillActivity;

public class ViewApplicationActivity extends AppCompatActivity {
    int appId;
    TextView title, appnum, address, meter, fullname, applicationDate, type,
            orStatus;
    AppCompatImageView btnBack;
    AppCompatImageView btnEditBill;

    String appApplicationNo;
    String appLastName;
    String appMiddleInitial;
    String appFirstName;
    String appAppType;
    String appMeterNo;
    String appHouseNo;
    String appBNo;
    String appStreet;
    String appBrgy;
    String appOrStatus;
    String appAppDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);

        title = findViewById(R.id.tvTitle);
        appnum = findViewById(R.id.tvAppNum);
        address = findViewById(R.id.tvAddress);
        meter = findViewById(R.id.tvMeterNo);
        applicationDate = findViewById(R.id.tvAppDate);
        fullname = findViewById(R.id.tvFullName);
        type = findViewById(R.id.tvClassification);
        orStatus = findViewById(R.id.tvOrStatus);
        btnEditBill = findViewById(R.id.btnBillEdit);

        getData();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnEditBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewApplicationActivity.this, UpdateApplicationActivity.class);
                intent.putExtra("application_no", appApplicationNo);
                intent.putExtra("last_name", appLastName);
                intent.putExtra("middle_initial", appMiddleInitial);
                intent.putExtra("first_name", appFirstName);
                intent.putExtra("classification", appAppType);
                intent.putExtra("meter_no", appMeterNo);
                intent.putExtra("house_no", appHouseNo);
                intent.putExtra("building_no", appBNo);
                intent.putExtra("street", appStreet);
                intent.putExtra("brgy", appBrgy);
                intent.putExtra("or_status", appOrStatus);
                intent.putExtra("app_date", appAppDate);

                startActivityForResult(intent, 1);
            }
        });
    }

    private void getData() {
        Intent i = getIntent();
        appId = i.getIntExtra("id", -1);
        String appApplicationNo = i.getStringExtra("application_no");
        String appLastName = i.getStringExtra("lname");
        String appMiddleInitial = i.getStringExtra("mname");
        String appFirstName = i.getStringExtra("fname");
        String appAppType = i.getStringExtra("classification");
        String appMeterNo = i.getStringExtra("meter_no");
        String appHouseNo = i.getStringExtra("house_no");
        String appBNo = i.getStringExtra("building_no");
        String appStreet = i.getStringExtra("street");
        String appBrgy = i.getStringExtra("barangay");
        String appOrStatus = i.getStringExtra("or_status");
        String appAppDate = i.getStringExtra("application_date");
        String appType = i.getStringExtra("application_type");
        String appStatus = i.getStringExtra("status");
        String appUpdatedAt = i.getStringExtra("updated_at");
        String appCreatedAt = i.getStringExtra("created_at");

        title.setText(appApplicationNo);
        appnum.setText(appApplicationNo);
        fullname.setText(appFirstName + " " + appMiddleInitial + " " + appLastName);
        address.setText(appHouseNo + " " + appBNo + " " + appStreet + " " + appBrgy);
        type.setText(appAppType);
        meter.setText(appMeterNo);
        applicationDate.setText(appAppDate);
        orStatus.setText(appOrStatus);
    }
}