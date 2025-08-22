package com.customers.grocerystoresforcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerLogin extends AppCompatActivity {

    EditText emailid,userpassword;
    TextView gotologin,forgotten_password;
    Button loginBtn;
    ProgressBar progressBar;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        toolbar = findViewById(R.id.toolbar_customer_login);
        gotologin = findViewById(R.id.gotologin);
        loginBtn = findViewById(R.id.loginBtn);
        emailid = findViewById(R.id.emailid);
        userpassword = findViewById(R.id.userpassword);
        progressBar = findViewById(R.id.progressbar);
        forgotten_password = findViewById(R.id.forgotten_password);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grocery Stores");
        FirebaseAuth.getInstance().signOut();

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text_emailid = emailid.getText().toString().trim();
                final String text_userpassword = userpassword.getText().toString();

                if(TextUtils.isEmpty(text_emailid) || TextUtils.isEmpty(text_userpassword) )
                {
                    Toast.makeText(CustomerLogin.this,"All fileds are required!",Toast.LENGTH_SHORT).show();
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(text_emailid).matches()){
                    Toast.makeText(CustomerLogin.this,"Please enter valid email_id.",Toast.LENGTH_SHORT).show();
                }

                else{

                    progressBar.setVisibility(View.VISIBLE);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details");
                    databaseReference.orderByChild("ShopperEmail").equalTo(text_emailid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                Toast.makeText(CustomerLogin.this, "This email_id is registerred as shoppers. Go back and Login as Shopper. ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            } else {
                                firebaseAuth.signInWithEmailAndPassword(text_emailid, text_userpassword)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {

                                                        Intent intent = new Intent(CustomerLogin.this, Customer_Home.class);
                                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        Toast.makeText(CustomerLogin.this, "Please first verify your email address.", Toast.LENGTH_SHORT).show();

                                                    }
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(CustomerLogin.this, "Incorrect email_id or Password!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerLogin.this, Customer_Registration.class));

            }
        });

        forgotten_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forgotten_Password forgotten_password = new Forgotten_Password();
                forgotten_password.show(getSupportFragmentManager(),"Forgotten Password");
                return;
            }
        });


    }

    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Click two times to close an activity", Toast.LENGTH_SHORT).show(); }
        mBackPressed = System.currentTimeMillis();
    }
}

