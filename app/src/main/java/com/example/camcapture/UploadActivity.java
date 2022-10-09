package com.example.camcapture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {
    ImageView imageview;
    Button button_upload;
    Bitmap bitmap;
    TextView textview;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // Get the intent that started this activity, and extract the image
        Intent intent1 = getIntent();
        bitmap = (Bitmap) intent1.getParcelableExtra("data");
        imageview = findViewById(R.id.imageview);
        imageview.setImageBitmap(bitmap);

        // Convert bitmap image to a String in order to upload to the server.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        //String encodedImage = Base64.encodeToString(convertToBytes(), Base64.DEFAULT);
        button_upload = findViewById(R.id.button_upload);
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat = spinner.getSelectedItem().toString();
                OkHttpClient okHttpClient = new OkHttpClient();
                //RequestBody formbody = new FormBody.Builder().add("image", encodedImage).add("category", cat).build();
                RequestBody formbody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("encodedImage", encodedImage).addFormDataPart("category", cat).build();
                Request request = new Request.Builder().url("https://664c-129-219-21-3.ngrok.io").post(formbody).build();
                //Request request = new Request.Builder().url("http://127.0.0.1:5000").post(formbody).build();

                /*HttpUrl.Builder urlBuilder = HttpUrl.parse("http://127.0.0.1:5000").newBuilder();
                urlBuilder.addQueryParameter(encodedImage, cat);
                String url = urlBuilder.build().toString();
                Request request = new Request.Builder().url(url).build();*/

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (Response response = okHttpClient.newCall(request).execute()){
                            if(!response.isSuccessful())
                            {
                                //Toast.makeText(UploadActivity.this, "Server down", Toast.LENGTH_LONG).show();
                                throw new IOException("Server down " + response);
                            }
                            else
                            {
                                Headers responseHeaders = response.headers();
                                for(int i=0; i<responseHeaders.size(); i++)
                                {
                                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                }
                                System.out.println(response.body().toString());
                                Intent intentF = new Intent(getApplicationContext(), FinalActivity.class);
                                //intentF.putExtra("data", bitmap);
                                startActivity(intentF);
                                //Toast.makeText(UploadActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                //Toast.makeText(UploadActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();

               /* okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadActivity.this, "Server down", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        textview = findViewById(R.id.textView2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    textview.setText(response.body().toString());
                            }
                        });
                    }
                });*/
                //Intent intentF = new Intent(getApplicationContext(), FinalActivity.class);
                //intentF.putExtra("data", bitmap);
                //startActivity(intentF);
            }
        });
    }

    /*public byte[] convertToBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }*/
}