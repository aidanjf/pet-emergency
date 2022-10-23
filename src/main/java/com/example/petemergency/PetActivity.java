package com.example.petemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PetActivity extends AppCompatActivity {

    public static String petType;
    public static String ailment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] pets = {"Dog", "Cat", "Rabbit", "Rodent", "Lizard"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, pets);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterSpinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pets_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);


        Button next = (Button) findViewById(R.id.next_button1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petType = spinner.getSelectedItem().toString();
                startActivity(new Intent(PetActivity.this, AilmentActivity.class));
            }
        });

    }
}