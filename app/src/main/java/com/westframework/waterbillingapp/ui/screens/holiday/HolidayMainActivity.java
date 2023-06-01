package com.westframework.waterbillingapp.ui.screens.holiday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.holiday.Holiday;
import com.westframework.waterbillingapp.data.holiday.HolidayAdapter;
import com.westframework.waterbillingapp.data.holiday.HolidayViewModel;

import java.util.List;

public class HolidayMainActivity extends AppCompatActivity {

    private HolidayViewModel holidayViewModel;
    AppCompatImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final HolidayAdapter holidayAdapter = new HolidayAdapter();
        recyclerView.setAdapter(holidayAdapter);

        holidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class);
        holidayViewModel.getHolidays().observe(this, new Observer<List<Holiday>>() {
            @Override
            public void onChanged(List<Holiday> holidays) {
                holidayAdapter.setHolidays(holidays);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}