package com.westframework.waterbillingapp.ui.screens.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.westframework.waterbillingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddBillActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    Button cancel;
    Button save;
    //--date picker
    private Button mPickDateButton;
    private TextView mTextViewReadDate;
    //---------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);
        cancel = findViewById(R.id.buttonCancel);
        save = findViewById(R.id.buttonSave);

        //--date picker
        mPickDateButton = findViewById(R.id.picker_read_date);
        mTextViewReadDate = findViewById(R.id.textViewReadDate);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Set Read Date");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String timeStamp = s.format(new Date());

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis(selection);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate  = format.format(calendar.getTime());
                        mTextViewReadDate.setText(formattedDate);
                    }
                });
        //---------------

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Nothing Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBill();
            }
        });
    }

    private void saveBill() {
        String billTitle = title.getText().toString();
        String billDescription = description.getText().toString();
        String readDate = mTextViewReadDate.getText().toString();
        Intent i = new Intent();
        i.putExtra("billTitle", billTitle);
        i.putExtra("billDescription", billDescription);
        i.putExtra("readDate", readDate);
        setResult(RESULT_OK, i);
        finish();
    }
}