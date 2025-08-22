package com.customers.grocerystoresforcustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private LinearLayout alert_Relative_layout;
    private RelativeLayout image_relativelayout,app_name_relativelayout;
    private TextView splash_alert_message;

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            alert_Relative_layout = findViewById(R.id.alert_Relative_layout);
            image_relativelayout = findViewById(R.id.image_relativelayout);
            app_name_relativelayout = findViewById(R.id.app_name_relativelayout);
            splash_alert_message = findViewById(R.id.splash_alert_message);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    image_relativelayout.setVisibility(View.GONE);
                    app_name_relativelayout.setVisibility(View.GONE);
                    if(firebaseUser != null && firebaseUser.isEmailVerified()){
                        alert_Relative_layout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                splash_alert_message.setVisibility(View.VISIBLE);
                            }
                        },7000);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details");
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status","online");
                        databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap);
                        startActivity(new Intent(Splash.this,Customer_Home.class));
                    }
                    else {
                        startActivity(new Intent(Splash.this, CustomerLogin.class));
                    }
                }
            },SPLASH_TIME_OUT);
        }
    }