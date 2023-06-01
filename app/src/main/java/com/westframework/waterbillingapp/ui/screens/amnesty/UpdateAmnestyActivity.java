package com.westframework.waterbillingapp.ui.screens.amnesty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.westframework.waterbillingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateAmnestyActivity extends AppCompatActivity {

    int amnestyId;
    private TextInputEditText ordinanceName;
    private AutoCompleteTextView billMonth;
    private AutoCompleteTextView billYear;
    private TextInputEditText description;
    private Button save;
    private Button cancel;
    //---------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_amnesty);

        ordinanceName = findViewById(R.id.etOrdinanceName);
        billMonth = findViewById(R.id.acBillMonth);
        billYear = findViewById(R.id.acBillYear);
        description = findViewById(R.id.etDescription);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(UpdateAmnestyActivity.this, R.layout.months_dropdown_item, months);
        billMonth.setAdapter(monthsAdapter);
        billMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               Log.d((String) adapterView.getItemAtPosition(i));
            }
        });

        String[] years = {"2023", "2022", "2021", "2020", "2019", "2018"};
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(UpdateAmnestyActivity.this, R.layout.years_dropdown_item, years);
        billYear.setAdapter(yearsAdapter);
        billYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               Log.d((String) adapterView.getItemAtPosition(i));
            }
        });

        cancel = findViewById(R.id.buttonCancelUpdate);
        save = findViewById(R.id.buttonSaveUpdate);

        getData();

        cancel.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Nothing Updated", Toast.LENGTH_LONG).show();
            finish();
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(CheckAllFields()){
                    updateBill();
                }
            }
        });
    }

    public void updateBill(){

        String ordinanceNameLast = ordinanceName.getText().toString();
        String billMonthLast = billMonth.getText().toString();
        String billYearLast = billYear.getText().toString();
        String descriptionLast = description.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("ordinance_name", ordinanceNameLast);
        intent.putExtra("bill_month", billMonthLast);
        intent.putExtra("bill_year", billYearLast);
        intent.putExtra("description", descriptionLast);

        if(amnestyId != -1)
        {
            intent.putExtra("amnestyId", amnestyId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void getData()
    {
        Intent i = getIntent();
        amnestyId = i.getIntExtra("id", -1);
        String amOrdinanceName = i.getStringExtra("ordinance_name");
        String amBillMonth = i.getStringExtra("bill_month");
        String amBillYear = i.getStringExtra("bill_year");
        String amDescription = i.getStringExtra("description");
        ordinanceName.setText(amOrdinanceName);
        billMonth.setText(amBillMonth);
        billYear.setText(amBillYear);
        description.setText(amDescription);
    }

    private boolean CheckAllFields() {
        if (ordinanceName.length() == 0) { ordinanceName.setError("This field is required"); return false;}
        if (billMonth.length() == 0) { billMonth.setError("This field is required"); return false;}
        if (billYear.length() == 0) { billYear.setError("This field is required"); return false;}
        return true;
    }
}