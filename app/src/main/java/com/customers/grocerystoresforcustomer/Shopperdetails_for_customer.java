package com.customers.grocerystoresforcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.Model.Shopper_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Shopperdetails_for_customer extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private TextView Shopper_Name, emailid, contact, shopname,shopaddress,shoplandmark,landmarkhead,shopsubcity,shopcity,shoppincode;
    Intent intent;
    Toolbar toolbar;
    CircleImageView profile;
    ImageView landmarkhead_belowline;
    Button order_book_button;
    String get_shopper_id;
    String passimageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopperdetails_for_customer);
        getfindViewById();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grocery Stores");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Shopperdetails_for_customer.this,Customer_Home.class));
                Shopperdetails_for_customer.this.finish();
            }
        });

        order_book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Shopperdetails_for_customer.this, Message.class);
                intent.putExtra("id",get_shopper_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

            }
        });

        intent = getIntent();
        get_shopper_id = intent.getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details").child(get_shopper_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shopper_Details shopper_details = dataSnapshot.getValue(Shopper_Details.class);
                Shopper_Name.setText(shopper_details.getShoppersname());
                emailid.setText(shopper_details.getShopperEmail());
                contact.setText(shopper_details.getShopperContact());
                shopname.setText(shopper_details.getShopName());
                shopaddress.setText(shopper_details.getShopAddress());
                if(shopper_details.getShoplandmark().length() < 1)
                {
                    shoplandmark.setVisibility(View.GONE);
                    landmarkhead.setVisibility(View.GONE);
                    landmarkhead_belowline.setVisibility(View.GONE);

                }
                else
                    {
                    shoplandmark.setVisibility(View.VISIBLE);
                    landmarkhead.setVisibility(View.VISIBLE);
                    landmarkhead_belowline.setVisibility(View.VISIBLE);
                    shoplandmark.setText(shopper_details.getShoplandmark());
                }
                shopsubcity.setText(shopper_details.getShopsubcity());

                shopcity.setText(shopper_details.getShopcity());
                shoppincode.setText(shopper_details.getShopPincode());
                if(shopper_details.getImageURL().equals("default")){
                    profile.setImageResource(R.drawable.man);
                    passimageurl = shopper_details.getImageURL();
                }else {
                    passimageurl = shopper_details.getImageURL();
                    Glide.with(Shopperdetails_for_customer.this).load(shopper_details.getImageURL()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shopperdetails_for_customer.this, ShowFullImageToCustomer.class);
                intent.putExtra("imagepath",passimageurl);
                startActivity(intent);
            }
        });
    }
    private void getfindViewById(){
        toolbar = findViewById(R.id.toolbar_shoppers_view_details);
        order_book_button = findViewById(R.id.order_book_button);
        Shopper_Name = findViewById(R.id.Shopper_Name);
        emailid = findViewById(R.id.emailid);
        contact = findViewById(R.id.contact);
        shopname = findViewById(R.id.shopname);
        shopaddress = findViewById(R.id.shopaddress);
        shoplandmark = findViewById(R.id.shoplandmark);
        landmarkhead = findViewById(R.id.landmarkhead);
        landmarkhead_belowline = findViewById(R.id.landmarkhead_belowline);
        shopsubcity = findViewById(R.id.shopsubcity);
        shopcity = findViewById(R.id.shopcity);
        shoppincode = findViewById(R.id.shoppincode);
        profile = findViewById(R.id.profile);

    }
    protected void onPause(){
        super.onPause();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","offline");
        FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid()).updateChildren(hashMap);
    }
    protected void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","online");
        FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid()).updateChildren(hashMap);
    }
   protected void onRestart(){
        super.onRestart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","online");
       FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid()).updateChildren(hashMap);
    }

    public void onBackPressed(){
        Intent intent = new Intent(Shopperdetails_for_customer.this,Customer_Home.class);
        startActivity(intent);
        Shopperdetails_for_customer.this.finish();
    }

}
