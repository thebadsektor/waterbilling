package com.westframework.waterbillingapp.ui.screens.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.westframework.waterbillingapp.R;

public class AddApplicationActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    Button cancel;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_application);

        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);
        cancel = findViewById(R.id.buttonCancel);
        save = findViewById(R.id.buttonSave);

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
                saveApp();
            }
        });
    }

    private void saveApp() {
        String appTitle = title.getText().toString();
        String appDescription = description.getText().toString();
        Intent i = new Intent();
        i.putExtra("appTitle", appTitle);
        i.putExtra("appDescription", appDescription);
        setResult(RESULT_OK, i);
        finish();
    }
}