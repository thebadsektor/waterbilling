package com.westframework.waterbillingapp.ui.screens.amnesty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.amnesty.Amnesty;
import com.westframework.waterbillingapp.data.amnesty.AmnestyAdapter;
import com.westframework.waterbillingapp.data.amnesty.AmnestyViewModel;
import com.westframework.waterbillingapp.data.bill.WaterBill;
import com.westframework.waterbillingapp.data.bill.WaterBillAdapter;
import com.westframework.waterbillingapp.data.holiday.Holiday;
import com.westframework.waterbillingapp.data.holiday.HolidayAdapter;
import com.westframework.waterbillingapp.data.holiday.HolidayViewModel;
import com.westframework.waterbillingapp.ui.screens.bill.BillMainActivity;
import com.westframework.waterbillingapp.ui.screens.bill.UpdateBillActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AmnestyMainActivity extends AppCompatActivity {

    private AmnestyViewModel amnestyViewModel;
    AppCompatImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amnesty_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AmnestyAdapter amnestyAdapter = new AmnestyAdapter();
        recyclerView.setAdapter(amnestyAdapter);

        amnestyViewModel = ViewModelProviders.of(this).get(AmnestyViewModel.class);
        amnestyViewModel.getAmnesties().observe(this, new Observer<List<Amnesty>>() {
            @Override
            public void onChanged(List<Amnesty> amnesties) {
                amnestyAdapter.setAmnesties(amnesties);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                amnestyViewModel.delete(amnestyAdapter.getAmnesties(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        amnestyAdapter.setOnItemClickListerner(new AmnestyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Amnesty amnesty) {
                Intent intent = new Intent(AmnestyMainActivity.this, UpdateAmnestyActivity.class);
                intent.putExtra("id", amnesty.getId());
                intent.putExtra("ordinance_name", amnesty.getOrdinanceName());
                intent.putExtra("bill_month", amnesty.getBillMonth());
                intent.putExtra("bill_year", amnesty.getBillYear());
                intent.putExtra("description", amnesty.getDesc());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = s.format(new Date());

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
//            String title = data.getStringExtra("billTitle");
//            String description = data.getStringExtra("billDescription");
//
//            WaterBill waterBill = new WaterBill(title, description);
//            waterBillViewModel.insert(waterBill);
//            Toast.makeText(getApplicationContext(), "Bill Saved", Toast.LENGTH_LONG).show();
        }

        else if(requestCode == 2 && resultCode == RESULT_OK)
        {
            String ordinance_name = data.getStringExtra("ordinance_name");
            String bill_month = data.getStringExtra("bill_month");
            String bill_year = data.getStringExtra("bill_year");
            String description = data.getStringExtra("description");
            int id = data.getIntExtra("amnestyId", -1);

            Amnesty amnesty = new Amnesty(
                    ordinance_name,
                    bill_month,
                    description,
                    bill_year,
                    timeStamp,
                    timeStamp );
            amnesty.setId(id);
            amnestyViewModel.update(amnesty);

            Toast.makeText(getApplicationContext(), "Entry '" + id + " 'Updated", Toast.LENGTH_LONG).show();
        }

    }
}