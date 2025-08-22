package com.customers.grocerystoresforcustomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.customers.grocerystoresforcustomer.Model.PasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class Customer_Registration extends AppCompatActivity {

    private EditText userename,useremailid, usercontact,userpassword, userconfirmpassword;
    private Button registerBtn;
    private TextView userLoginPage;
    private Toolbar toolbar;
    Intent intent;
    Spinner usercity,usersubcity;
    PasswordValidator passwordValidator;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    FirebaseAuth firebaseAuth;
    String txt_userename, txt_useremailid, txt_usercity,txt_usersubcity, txt_usercontact, txt_userpassword, txt_userconfirmpassword;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> subadapter;
    ArrayList<String> spinnerdatalist,spinnersubdatalist;
    String cityselected,cityget,subcityselected,subcityget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__registration);
        setupUIviews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customers-Shop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Customer_Registration.this, CustomerLogin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();


        //Fetch city data from database
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Cities");
        databaseReference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerdatalist = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    cityselected = dataSnapshot1.getKey();
                    spinnerdatalist.add(cityselected);

                }
                adapter = new ArrayAdapter<String>(Customer_Registration.this,R.layout.spinner_items,spinnerdatalist);
                adapter.setDropDownViewResource(R.layout.spinner_items);
                usercity.setAdapter(adapter);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityget = (String) parent.getItemAtPosition(position);

                databaseReference2 = FirebaseDatabase.getInstance().getReference("Cities").child(cityget);
                databaseReference2.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        spinnersubdatalist = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            subcityselected = dataSnapshot1.child("cityname").getValue(String.class);
                            spinnersubdatalist.add(subcityselected);

                        }
                        subadapter = new ArrayAdapter<String>(Customer_Registration.this,R.layout.spinner_items,spinnersubdatalist);
                        subadapter.setDropDownViewResource(R.layout.spinner_items);
                        usersubcity.setAdapter(subadapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                usersubcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subcityget = (String) parent.getItemAtPosition(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        txt_userename = userename.getText().toString();
        txt_useremailid = useremailid.getText().toString().trim();
        txt_usercity = cityget;
        txt_usersubcity = subcityget;
        txt_usercontact = usercontact.getText().toString();
        txt_userpassword = userpassword.getText().toString();
        txt_userconfirmpassword = userconfirmpassword.getText().toString();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordValidator =  new PasswordValidator();
                txt_userename = userename.getText().toString();
                txt_useremailid = useremailid.getText().toString().trim();
                txt_usercity = cityget;
                txt_usersubcity = subcityget;
                txt_usercontact = usercontact.getText().toString();
                txt_userpassword = userpassword.getText().toString();
                txt_userconfirmpassword = userconfirmpassword.getText().toString();


                if(txt_userename.isEmpty() || txt_useremailid.isEmpty()  ||
                        txt_usercontact.isEmpty() || txt_userpassword.isEmpty() || txt_userconfirmpassword.isEmpty())
                {
                    Toast.makeText(Customer_Registration.this,"All fileds are required!",Toast.LENGTH_SHORT).show();
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(txt_useremailid).matches()){
                    Toast.makeText(Customer_Registration.this,"Please enter valid email_id.",Toast.LENGTH_SHORT).show();
                }

                else if (txt_usercontact.length() < 10 ){
                    Toast.makeText(Customer_Registration.this,"Please enter valid Mobile Number.",Toast.LENGTH_SHORT).show();
                }

                else if (!passwordValidator.validate(txt_userpassword)){
                    Toast.makeText(Customer_Registration.this,"Password Must be 8-15 alphanumeric.",Toast.LENGTH_SHORT).show();
                }

                else if (txt_userpassword.equals(txt_userconfirmpassword)){
                    registration();

                }

                else{
                    Toast.makeText(Customer_Registration.this,"password and confirm password should be same!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        userLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Customer_Registration.this, CustomerLogin.class));


            }
        });

    }


    private void registration(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details");
        databaseReference.orderByChild("ShopperEmail").equalTo(txt_useremailid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null){

                    Toast.makeText(Customer_Registration.this,"This email_id is already exits as shoppers." ,Toast.LENGTH_SHORT).show();

                }
                else{
                    register();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void register() {
        final ProgressDialog progressDialog = new ProgressDialog(Customer_Registration.this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(txt_useremailid, txt_userpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("customerName", txt_userename);
                            hashMap.put("customerEmailid", txt_useremailid);
                            hashMap.put("customerCity",txt_usercity);
                            hashMap.put("customerarelocation",txt_usersubcity);
                            hashMap.put("customerContact", txt_usercontact);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status","offline");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(Customer_Registration.this, "Registered successfully. Please check you email for verification.",
                                                                Toast.LENGTH_SHORT).show();
                                                        intent = new Intent(Customer_Registration.this, CustomerLogin.class);
                                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);

                                                    }
                                                });


                                    }
                                    progressDialog.dismiss();

                                }
                            });
                        } else {
                            Toast.makeText(Customer_Registration.this, "You have alredy account with this email_id.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    private void setupUIviews() {
        userename = findViewById(R.id.userename);
        useremailid = findViewById(R.id.useremailid);
        usercity = findViewById(R.id.usercity);
        usersubcity = findViewById(R.id.usersubcity);
        usercontact = findViewById(R.id.usercontact);
        userpassword = findViewById(R.id.userpassword);
        userconfirmpassword = findViewById(R.id.userconfirmpassword);
        registerBtn = findViewById(R.id.registerBtn);
        userLoginPage =  findViewById(R.id.gotologin);
        toolbar = findViewById(R.id.toolbar_customer_registration);
    }

}