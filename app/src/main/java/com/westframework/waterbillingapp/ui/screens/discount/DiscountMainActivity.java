package com.westframework.waterbillingapp.ui.screens.discount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.classification.Classification;
import com.westframework.waterbillingapp.data.classification.ClassificationAdapter;
import com.westframework.waterbillingapp.data.classification.ClassificationViewModel;
import com.westframework.waterbillingapp.data.discount.Discount;
import com.westframework.waterbillingapp.data.discount.DiscountAdapter;
import com.westframework.waterbillingapp.data.discount.DiscountViewModel;

import java.util.List;

public class DiscountMainActivity extends AppCompatActivity {

    private DiscountViewModel discountViewModel;
    AppCompatImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final DiscountAdapter discountAdapter = new DiscountAdapter();
        recyclerView.setAdapter(discountAdapter);

        discountViewModel = ViewModelProviders.of(this).get(DiscountViewModel.class);
        discountViewModel.getDiscounts().observe(this, new Observer<List<Discount>>() {
            @Override
            public void onChanged(List<Discount> discounts) {
                discountAdapter.setDiscounts(discounts);
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