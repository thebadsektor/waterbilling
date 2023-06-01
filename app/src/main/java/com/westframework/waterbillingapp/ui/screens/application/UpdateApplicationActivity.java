package com.westframework.waterbillingapp.ui.screens.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.westframework.waterbillingapp.R;

public class UpdateApplicationActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    Button cancel;
    Button save;
    int appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_application);

        title = findViewById(R.id.editTextTitleUpdate);
        description = findViewById(R.id.editTextDescriptionUpdate);
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
                updateNote();
            }
        });
    }

    public void updateNote(){
        String titleLast = title.getText().toString();
        String descriptionLast = description.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("titleLast", titleLast);
        intent.putExtra("descriptionLast", descriptionLast);
        if(appId != -1)
        {
            intent.putExtra("appId", appId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void getData()
    {
        Intent i = getIntent();
        appId = i.getIntExtra("id", -1);
        String appTitle = i.getStringExtra("title");
        String appDescription = i.getStringExtra("description");

        title.setText(appTitle);
        description.setText(appDescription);
    }
}