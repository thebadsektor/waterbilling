package com.westframework.waterbillingapp.ui.screens.classification;

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

import java.util.List;

public class ClassificationMainActivity extends AppCompatActivity {

    private ClassificationViewModel classificationViewModel;
    AppCompatImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ClassificationAdapter discountAdapter = new ClassificationAdapter();
        recyclerView.setAdapter(discountAdapter);

        classificationViewModel = ViewModelProviders.of(this).get(ClassificationViewModel.class);
        classificationViewModel.getClassifications().observe(this, new Observer<List<Classification>>() {
            @Override
            public void onChanged(List<Classification> classifications) {
                discountAdapter.setClassifications(classifications);
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