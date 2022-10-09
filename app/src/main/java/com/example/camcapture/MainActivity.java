package com.example.camcapture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button button;
    Button button_exit;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button_exit = findViewById(R.id.button_exit);

        // Check if we have permission to access the phone's camera. If Not, ask user for permission.
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        // If "Not Interested" button is clicked, Exit the application
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        // If "Capture!" button is clicked, open the camera to click picture
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // "startActivityForResult" OR "onActivityResult" function, in order to capture the result in bitmap
                //startForResult.launch(intent);
                startActivityForResult(intent, 100);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            // Start an intent to go to the next screen with the bitmap image to upload
            Intent intent1 = new Intent(getApplicationContext(), UploadActivity.class);
            intent1.putExtra("data", bitmap);
            startActivity(intent1);
        }
    }
}