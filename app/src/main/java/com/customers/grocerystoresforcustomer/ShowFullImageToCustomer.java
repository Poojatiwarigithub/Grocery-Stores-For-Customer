package com.customers.grocerystoresforcustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class ShowFullImageToCustomer extends AppCompatActivity {

    Toolbar image_toolbar;
    ImageView imageview;
    Intent intent;
    String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_image_to_customer);
        image_toolbar = findViewById(R.id.image_toolbar);
        imageview = findViewById(R.id.imageview);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(deviceWidth, deviceWidth);

        imageview.setLayoutParams(lp);

        setSupportActionBar(image_toolbar);
        getSupportActionBar().setTitle("Profile photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       image_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        intent = getIntent();
        imagepath = intent.getStringExtra("imagepath");

        if(imagepath.equals("default")){
            imageview.setImageResource(R.drawable.man);
        }else {
            Glide.with(ShowFullImageToCustomer.this).load(imagepath).into(imageview);
        }

    }
}
