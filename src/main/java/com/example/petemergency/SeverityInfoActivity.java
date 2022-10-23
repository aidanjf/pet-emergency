package com.example.petemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeverityInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_severity_info);

        Button find_vet = (Button) findViewById(R.id.find_vet_button);
        find_vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeverityInfoActivity.this, FindVetMapsActivity.class));
            }
        });

        /*Button get_help = (Button) findViewById(R.id.help_button);
        get_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SeverityInfoActivity.this, GetHelpActivity.class));
            }
        });*/

    }
}