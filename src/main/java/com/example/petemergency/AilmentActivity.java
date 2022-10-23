package com.example.petemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AilmentActivity extends AppCompatActivity {

    public static String ailment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Spinner spinner = findViewById(R.id.spinner_ailments);
        String[] ailments = {"Diarrhea", "Itchy Skin", "Vomiting", "Ear Infection", "Physical Pain"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, ailments);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapterSpinner);


        Button next = (Button) findViewById(R.id.next_button2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ailment = spinner.getSelectedItem().toString();
                startActivity(new Intent(AilmentActivity.this, SeverityInfoActivity.class));
            }
        });
    }
}