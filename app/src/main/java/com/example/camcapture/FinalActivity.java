package com.example.camcapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FinalActivity extends AppCompatActivity {
    Button button_yes;
    Button button_no;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        Toast.makeText(FinalActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
        Button button_yes = findViewById(R.id.button2);
        Button button_no = findViewById(R.id.button3);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentStart);
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(1);
            }
        });
    }
}