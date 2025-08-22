package com.customers.grocerystoresforcustomer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.CustomerLogin;
import com.customers.grocerystoresforcustomer.ImageSelectUpload;
import com.customers.grocerystoresforcustomer.Model.Customersdetails;
import com.customers.grocerystoresforcustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomerDetailsFragment extends Fragment {

    private TextView customer_Name, emailid, contact,customercity,customersubcity;
    private Button logoutbtn;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private ImageView edit;
    private CircleImageView profile;
    String oldprofilepic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);
        customer_Name = view.findViewById(R.id.customer_Name);
        emailid = view.findViewById(R.id.emailid);
        contact = view.findViewById(R.id.contact);
        customercity = view.findViewById(R.id.customercity);
        customersubcity = view.findViewById(R.id.customersubcity);
        logoutbtn = view.findViewById(R.id.logoutbtn);
        edit = view.findViewById(R.id.edit);
        profile = view.findViewById(R.id.profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        withouteditbuttonclicked();
        editbuttonclicked();

       profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageSelectUpload.class);
                startActivity(intent);
            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status","offline");
                databaseReference.updateChildren(hashMap);
                Intent intent = new Intent(CustomerDetailsFragment.this.getContext(),CustomerLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;

    }


    //Fetch Data and Display
    public void withouteditbuttonclicked() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                customer_Name.setText(customersdetails.getCustomerName());
                emailid.setText(customersdetails.getCustomerEmailid());
                contact.setText(customersdetails.getCustomerContact());
                customercity.setText(customersdetails.getCustomerCity());
                customersubcity.setText(customersdetails.getCustomerarelocation());
                if(customersdetails.getImageURL().equals("default")){
                    profile.setImageResource(R.drawable.man);
                }else {
                    oldprofilepic = customersdetails.getImageURL();
                    Glide.with(getContext()).load(customersdetails.getImageURL()).into(profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    // Update Customer Name and City
    public void editbuttonclicked() {

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBoxForCustomersrDetailsUpdate dbcdu = new DialogBoxForCustomersrDetailsUpdate();
                dbcdu.show(getActivity().getSupportFragmentManager(), "Update Customers Details");

            }
        });

    }
}
